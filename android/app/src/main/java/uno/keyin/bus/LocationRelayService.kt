package uno.keyin.bus

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlin.math.min
import kotlin.jvm.Volatile
import java.util.concurrent.atomic.AtomicBoolean
import org.json.JSONObject
import android.location.Location
import uno.keyin.bus.location.PhoneLocationCache
import uno.keyin.bus.location.PhoneLocationHelper
import uno.keyin.bus.wear.XmsWearSdkBridge

/**
 * 前台服务：锁屏/后台仍监听手表经穿戴发来的 JSON（如 [TYPE_REQUEST_LOCATION]、[TYPE_WATCH_APP_SESSION]），回传 phone_location。
 * 会话活跃时周期性高精度预热；超过 [LOCATION_SESSION_ACTIVE_WINDOW_MS] 无位置相关消息后降为长间隔 + 低精度。
 */
class LocationRelayService : Service() {

    private val mainHandler = Handler(Looper.getMainLooper())
    private var listenerRegisteredForNode: String? = null

    /**
     * 手表最近一次「前台会话」或 [request_location] 的时间；超过 [LOCATION_SESSION_ACTIVE_WINDOW_MS] 视为空闲，
     * 预热改为长间隔 + 低精度以省电。
     */
    @Volatile
    private var lastLocationSessionSignalAtMs: Long = 0L

    /** 与最后一次 notify/startForeground 文案一致，通知被划掉后用于重建 */
    private var lastNotificationText: String = ""

    /** 当前 Wear 节点已成功推送过「主动首包」后不再重复，直至节点断开或切换 */
    private var lastProactiveSnapshotNodeId: String? = null

    private val periodicRefresh = object : Runnable {
        override fun run() {
            periodicRefreshCount++
            val forceRelisten = periodicRefreshCount % FORCE_LISTENER_REFRESH_EVERY_N_TICKS == 0
            refreshNodeAndRegisterListener(forceRelisten = forceRelisten)
            mainHandler.postDelayed(this, 15000L)
        }
    }

    /** 每 N 次周期刷新（15s×N）在同节点上强制 unregister+register，避免通知被划掉后 SDK 监听已死但仍跳过注册 */
    private var periodicRefreshCount = 0

    private val warmupRunning = AtomicBoolean(false)
    private val locationWarmupRunnable = object : Runnable {
        override fun run() {
            val active = isLocationSessionActive()
            val nextDelay = if (active) WARMUP_INTERVAL_ACTIVE_MS else WARMUP_INTERVAL_IDLE_MS
            mainHandler.postDelayed(this, nextDelay)
            if (!PhoneLocationHelper.hasLocationPermission(applicationContext)) return
            if (!warmupRunning.compareAndSet(false, true)) return
            PhoneLocationHelper.fetchBestLocation(applicationContext, highAccuracy = active) { _ ->
                mainHandler.post { warmupRunning.set(false) }
            }
        }
    }

    private fun touchLocationSessionFromWatch() {
        lastLocationSessionSignalAtMs = System.currentTimeMillis()
    }

    private fun isLocationSessionActive(): Boolean {
        val t = lastLocationSessionSignalAtMs
        if (t == 0L) return false
        return System.currentTimeMillis() - t < LOCATION_SESSION_ACTIVE_WINDOW_MS
    }

    /** 手表刚进入前台时尽快按「活跃」节奏跑一轮预热 */
    private fun rescheduleLocationWarmupSoon() {
        mainHandler.removeCallbacks(locationWarmupRunnable)
        mainHandler.postDelayed(locationWarmupRunnable, 600L)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_REFRESH_NOTIF_WATCHDOG,
            ACTION_REAPPLY_RELAY_PREFS -> applyRelayUiPrefsFromIntent()
        }
        return START_STICKY
    }

    private fun applyRelayUiPrefsFromIntent() {
        rescheduleNotificationWatchdog()
        rescheduleHeartbeat()
        mainHandler.post { refreshNodeAndRegisterListener(forceRelisten = true) }
    }

    override fun onCreate() {
        super.onCreate()
        ensureChannel()
        lastNotificationText = getString(R.string.relay_notif_default)
        startForeground(NOTIFICATION_ID, buildNotification(lastNotificationText))
        XmsWearSdkBridge.registerWearServiceConnectionListener(
            applicationContext,
            onConnected = {
                mainHandler.post { refreshNodeAndRegisterListener(forceRelisten = true) }
            },
            onDisconnected = {
                listenerRegisteredForNode = null
                lastProactiveSnapshotNodeId = null
                updateNotification(getString(R.string.relay_notif_wear_disconnected))
            },
        )
        mainHandler.post { refreshNodeAndRegisterListener() }
        mainHandler.postDelayed(periodicRefresh, 15000L)
        rescheduleNotificationWatchdog()
        rescheduleHeartbeat()
        mainHandler.postDelayed(locationWarmupRunnable, WARMUP_FIRST_DELAY_MS)
    }

    override fun onDestroy() {
        mainHandler.removeCallbacks(periodicRefresh)
        mainHandler.removeCallbacks(locationWarmupRunnable)
        mainHandler.removeCallbacks(notificationWatchdog)
        mainHandler.removeCallbacks(heartbeatRunnable)
        listenerRegisteredForNode?.let { nid ->
            XmsWearSdkBridge.unregisterMessageListener(applicationContext, nid, {}, {})
        }
        listenerRegisteredForNode = null
        XmsWearSdkBridge.unregisterWearServiceConnectionListener(applicationContext)
        super.onDestroy()
    }

    private val notificationWatchdog = object : Runnable {
        override fun run() {
            val ctx = applicationContext
            val intervalMs = RelayUiPrefs.getNotificationWatchdogIntervalSec(ctx) * 1000L
            if (RelayUiPrefs.isNotificationWatchdogEnabled(ctx)) {
                val nm = ContextCompat.getSystemService(ctx, NotificationManager::class.java)
                val stillThere = nm?.activeNotifications?.any { it.id == NOTIFICATION_ID } == true
                if (!stillThere) {
                    startForeground(NOTIFICATION_ID, buildNotification(lastNotificationText))
                    mainHandler.post { refreshNodeAndRegisterListener(forceRelisten = true) }
                }
            }
            mainHandler.postDelayed(this, intervalMs)
        }
    }

    private val heartbeatRunnable = object : Runnable {
        override fun run() {
            val intervalMs = RelayUiPrefs.getRelayHeartbeatIntervalSec(applicationContext) * 1000L
            sendHeartbeatPayloadIfPossible()
            mainHandler.postDelayed(this, intervalMs)
        }
    }

    private fun sendHeartbeatPayloadIfPossible() {
        if (!RelayUiPrefs.isRelayHeartbeatEnabled(applicationContext)) return
        val nid = listenerRegisteredForNode ?: return
        val payload = JSONObject().apply {
            put("type", TYPE_RELAY_HEARTBEAT)
            put("ts", System.currentTimeMillis())
            put("watchGpsAfterPhoneFailures", RelayUiPrefs.getWatchGpsAfterPhoneFailures(applicationContext))
            put("showWatchDebugUi", RelayUiPrefs.isShowWatchDebugUi(applicationContext))
            put("vibeStatusApiUrl", RelayUiPrefs.getVibeStatusApiUrl(applicationContext))
            put(
                "watchDefaultHome",
                if (RelayUiPrefs.isWatchDefaultHomeVibe(applicationContext)) "vibe" else "bus",
            )
        }.toString()
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        val tag = "${packageName}:RelayHeartbeat"
        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag)
        wl.acquire(3500)
        try {
            XmsWearSdkBridge.sendTextToNode(
                applicationContext,
                nid,
                payload,
                onOk = {},
                onErr = {},
            )
        } finally {
            runCatching { if (wl.isHeld) wl.release() }
        }
    }

    private fun rescheduleHeartbeat() {
        mainHandler.removeCallbacks(heartbeatRunnable)
        if (!RelayUiPrefs.isRelayHeartbeatEnabled(applicationContext)) return
        val intervalMs = RelayUiPrefs.getRelayHeartbeatIntervalSec(applicationContext) * 1000L
        val firstDelay = min(8000L, intervalMs)
        mainHandler.postDelayed(heartbeatRunnable, firstDelay)
    }

    private fun rescheduleNotificationWatchdog() {
        mainHandler.removeCallbacks(notificationWatchdog)
        val intervalMs = RelayUiPrefs.getNotificationWatchdogIntervalSec(applicationContext) * 1000L
        if (!RelayUiPrefs.isNotificationWatchdogEnabled(applicationContext)) {
            return
        }
        mainHandler.postDelayed(notificationWatchdog, intervalMs)
    }

    private fun refreshNodeAndRegisterListener(forceRelisten: Boolean = false) {
        XmsWearSdkBridge.fetchConnectedNodeIds(
            applicationContext,
            onResult = { ids ->
                val id = ids.firstOrNull()
                if (id == null) {
                    listenerRegisteredForNode?.let { nid ->
                        XmsWearSdkBridge.unregisterMessageListener(applicationContext, nid, {}, {})
                    }
                    listenerRegisteredForNode = null
                    lastProactiveSnapshotNodeId = null
                    updateNotification(getString(R.string.relay_notif_no_node))
                    return@fetchConnectedNodeIds
                }

                if (!forceRelisten && id == listenerRegisteredForNode) {
                    return@fetchConnectedNodeIds
                }

                val previousId = listenerRegisteredForNode
                if (previousId != null && previousId != id) {
                    lastProactiveSnapshotNodeId = null
                }
                val oldToUnregister = when {
                    previousId == null -> null
                    previousId == id -> id
                    else -> previousId
                }

                fun attachListener() {
                    listenerRegisteredForNode = id
                    registerListener(id)
                }

                if (oldToUnregister == null) {
                    attachListener()
                    return@fetchConnectedNodeIds
                }

                XmsWearSdkBridge.unregisterMessageListener(
                    applicationContext,
                    oldToUnregister,
                    onDone = {
                        mainHandler.post {
                            attachListener()
                        }
                    },
                    onErr = {
                        mainHandler.post {
                            attachListener()
                        }
                    },
                )
            },
            onError = {
                updateNotification(getString(R.string.relay_notif_node_err))
            },
        )
    }

    private fun registerListener(nodeId: String) {
        XmsWearSdkBridge.registerMessageListener(
            applicationContext,
            nodeId,
            onBytes = { _, bytes -> handleWatchPayload(nodeId, bytes) },
            onOk = {
                updateNotification(getString(R.string.relay_notif_listening))
                mainHandler.post { maybeSendProactiveLocationSnapshot(nodeId) }
            },
            onErr = {
                updateNotification(getString(R.string.relay_notif_listen_fail, it))
            },
        )
    }

    private fun maybeSendProactiveLocationSnapshot(nodeId: String) {
        if (nodeId != listenerRegisteredForNode) return
        if (lastProactiveSnapshotNodeId == nodeId) return
        if (!PhoneLocationHelper.hasLocationPermission(applicationContext)) return

        /** 重连首包：始终高精度，避免手表已开页但会话消息未到、会话窗口未激活时仍走平衡模式导致长时间无坐标 */
        touchLocationSessionFromWatch()
        PhoneLocationHelper.fetchBestLocation(applicationContext, highAccuracy = true) { loc ->
            mainHandler.post {
                val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
                val tag = "${packageName}:ProactiveLoc"
                val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag)
                wl.acquire(5000)
                try {
                    if (loc == null || nodeId != listenerRegisteredForNode) return@post
                    val payload = PhoneLocationPayload.toJson(loc, null, PhoneLocationPayload.SOURCE_PROACTIVE)
                    XmsWearSdkBridge.sendTextToNode(
                        applicationContext,
                        nodeId,
                        payload,
                        onOk = {
                            lastProactiveSnapshotNodeId = nodeId
                            updateNotification(getString(R.string.relay_notif_proactive_sent))
                        },
                        onErr = {
                            updateNotification(getString(R.string.relay_notif_proactive_fail, it))
                        },
                    )
                } finally {
                    runCatching { if (wl.isHeld) wl.release() }
                }
            }
        }
    }

    private fun handleWatchPayload(nodeId: String, bytes: ByteArray) {
        val raw = runCatching { String(bytes, Charsets.UTF_8) }.getOrElse { return }
        val json = runCatching { JSONObject(raw) }.getOrElse { return }
        when (json.optString("type")) {
            TYPE_WATCH_APP_SESSION -> {
                touchLocationSessionFromWatch()
                rescheduleLocationWarmupSoon()
                return
            }
            TYPE_REQUEST_LOCATION -> handleWatchLocationRequest(nodeId, json)
            else -> return
        }
    }

    private fun handleWatchLocationRequest(nodeId: String, json: JSONObject) {
        touchLocationSessionFromWatch()
        val requestId = json.optString("requestId", "")
        if (!PhoneLocationHelper.hasLocationPermission(applicationContext)) {
            sendError(nodeId, requestId, "no_location_permission")
            updateNotification(getString(R.string.relay_notif_need_loc_perm))
            return
        }

        val sessionActive = isLocationSessionActive()
        val quick = PhoneLocationCache.peekForQuickReply(
            PhoneLocationCache.QUICK_REPLY_MAX_WALL_MS,
            PhoneLocationCache.QUICK_REPLY_MAX_FIX_AGE_MS,
        )
        if (quick != null) {
            deliverPhoneLocation(nodeId, requestId, quick)
            PhoneLocationHelper.fetchBestLocation(applicationContext, highAccuracy = sessionActive) { }
            return
        }

        /** 无缓存秒回时手表正在等 UI：直接高精度，避免「正在获取定位」长时间无结果 */
        PhoneLocationHelper.fetchBestLocation(applicationContext, highAccuracy = true) { loc ->
            mainHandler.post {
                if (loc == null) {
                    sendError(nodeId, requestId, "location_unavailable")
                    updateNotification(getString(R.string.relay_notif_loc_fail))
                    return@post
                }
                deliverPhoneLocation(nodeId, requestId, loc)
            }
        }
    }

    private fun deliverPhoneLocation(nodeId: String, requestId: String, loc: Location) {
        val payload = PhoneLocationPayload.toJson(loc, requestId.ifBlank { null })
        XmsWearSdkBridge.sendTextToNode(
            applicationContext,
            nodeId,
            payload,
            onOk = {
                updateNotification(getString(R.string.relay_notif_sent))
            },
            onErr = {
                updateNotification(getString(R.string.relay_notif_send_fail, it))
            },
        )
    }

    private fun sendError(nodeId: String, requestId: String, code: String) {
        val err = JSONObject().apply {
            put("type", TYPE_PHONE_LOCATION_ERROR)
            put("requestId", requestId)
            put("code", code)
            put("ts", System.currentTimeMillis())
        }.toString()
        XmsWearSdkBridge.sendTextToNode(applicationContext, nodeId, err, {}, {})
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = getSystemService(NotificationManager::class.java) ?: return
        val ch = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.relay_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = getString(R.string.relay_channel_desc)
        }
        nm.createNotificationChannel(ch)
    }

    private fun buildNotification(content: String): Notification {
        val launch = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setContentTitle(getString(R.string.relay_notif_title))
            .setContentText(content)
            .setContentIntent(launch)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification(content: String) {
        lastNotificationText = content
        val nm = ContextCompat.getSystemService(this, NotificationManager::class.java) ?: return
        nm.notify(NOTIFICATION_ID, buildNotification(content))
    }

    companion object {
        private const val CHANNEL_ID = "band_bus_location_relay"
        private const val NOTIFICATION_ID = 7101

        /** MainActivity 修改守护间隔/开关后触发，重新调度定时检查 */
        const val ACTION_REFRESH_NOTIF_WATCHDOG = "uno.keyin.bus.action.REFRESH_NOTIF_WATCHDOG"

        /** MainActivity 修改中继相关偏好后触发：通知守护 + 心跳间隔等 */
        const val ACTION_REAPPLY_RELAY_PREFS = "uno.keyin.bus.action.REAPPLY_RELAY_PREFS"

        /** 手表侧忽略；经 Wear 发往手表以保持消息通道活跃（锁屏休眠后易超时） */
        const val TYPE_RELAY_HEARTBEAT = "relay_heartbeat"

        /** 手表侧：与 [TYPE_REQUEST_LOCATION] 一并经穿戴消息发往手机，表示快应用进入前台以恢复高精度预热 */
        const val TYPE_WATCH_APP_SESSION = "watch_app_session"

        const val TYPE_REQUEST_LOCATION = "request_location"
        const val TYPE_PHONE_LOCATION_ERROR = "phone_location_error"

        /** 每 15s 一轮；每 4 轮（约 60s）在同节点上强制重绑 Wear 消息监听 */
        private const val FORCE_LISTENER_REFRESH_EVERY_N_TICKS = 4

        /** 无「前台会话 / 索要位置」超过该时间后，预热改为低精度 + 长间隔 */
        private const val LOCATION_SESSION_ACTIVE_WINDOW_MS = 5 * 60 * 1000L

        /** 会话活跃：较短间隔 + 高精度单次定位 */
        private const val WARMUP_INTERVAL_ACTIVE_MS = 22_000L

        /** 会话空闲：降低定位频率与精度以省电 */
        private const val WARMUP_INTERVAL_IDLE_MS = 120_000L

        private const val WARMUP_FIRST_DELAY_MS = 400L

        fun start(context: Context) {
            val i = Intent(context, LocationRelayService::class.java)
            ContextCompat.startForegroundService(context, i)
        }
    }
}
