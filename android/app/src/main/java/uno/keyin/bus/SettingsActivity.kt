package uno.keyin.bus

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import uno.keyin.bus.databinding.ActivitySettingsBinding
import uno.keyin.bus.location.PhoneLocationCache
import uno.keyin.bus.location.PhoneLocationHelper
import uno.keyin.bus.wear.XmsWearSdkBridge
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SettingsActivity : AppCompatActivity() {

    companion object {
        private const val KEY_EXCLUDE_FROM_RECENTS = "exclude_from_recents"
    }

    private lateinit var binding: ActivitySettingsBinding
    private var currentNodeId: String? = null
    private val logBuf = StringBuilder()
    private val timeFmt = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val mainHandler = Handler(Looper.getMainLooper())
    private val resumeRefreshRunnable = Runnable { refreshNodes() }

    private var sendLocationAfterLocationGrant = false
    private var sendLocationInFlight = false

    private val backgroundLocationLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            appendLog(getString(R.string.log_background_location_granted))
        } else {
            appendLog(getString(R.string.log_background_location_denied))
        }
    }

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { grants ->
        val fine = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarse = grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val ok = fine || coarse
        if (ok) {
            appendLog(
                buildString {
                    append("定位权限已授予")
                    if (fine) append("（含精确位置）") else append("（仅大致位置，建议同时允许精确位置）")
                },
            )
            appendLog(getString(R.string.log_location_precise_hint))
            maybeRequestBackgroundLocationAfterForegroundGrant()
            if (sendLocationAfterLocationGrant) {
                sendLocationAfterLocationGrant = false
                sendCurrentLocationToWatch()
            }
        } else {
            appendLog("定位权限被拒绝，请到系统设置中为本应用开启位置权限")
            sendLocationAfterLocationGrant = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingsToolbar.setNavigationOnClickListener { finish() }

        binding.title.text = getString(R.string.settings_relay_section_title)
        refreshSdkBanner()
        runInterconnectSelfCheck()

        binding.btnRefreshNodes.setOnClickListener { refreshNodes() }
        binding.btnRequestPermission.setOnClickListener { requestWearPermission() }
        binding.btnRegisterListener.setOnClickListener { registerListener() }
        binding.btnRequestLocationPermission.setOnClickListener { requestLocationPermissionOnly() }
        binding.btnSendLocation.setOnClickListener { onSendLocationClicked() }
        binding.btnSend.setOnClickListener { sendMessage() }

        setupExcludeFromRecentsSwitch()
        setupKeepaliveActions()
        setupNotificationWatchdogUi()
        setupRelayHeartbeatUi()
        setupWatchGpsDeferUi()
        setupVibeCodingUi()
        setupWatchDebugUiSwitch()
        refreshBatteryOptBanner()

        appendLog("已进入设置页。")
        refreshNodes()
    }

    override fun onResume() {
        super.onResume()
        applyRecentTasksExcludeFromRecents(
            prefsUi().getBoolean(KEY_EXCLUDE_FROM_RECENTS, false),
        )
        refreshBatteryOptBanner()
        mainHandler.removeCallbacks(resumeRefreshRunnable)
        mainHandler.postDelayed(resumeRefreshRunnable, 450)
    }

    override fun onPause() {
        saveVibeStatusApiUrlIfChanged()
        super.onPause()
    }

    override fun onDestroy() {
        mainHandler.removeCallbacks(resumeRefreshRunnable)
        super.onDestroy()
    }

    private fun prefsUi() = getSharedPreferences(RelayUiPrefs.PREFS_NAME, MODE_PRIVATE)

    private fun setupExcludeFromRecentsSwitch() {
        val exclude = prefsUi().getBoolean(KEY_EXCLUDE_FROM_RECENTS, false)
        binding.switchExcludeFromRecents.isChecked = exclude
        applyRecentTasksExcludeFromRecents(exclude)
        binding.switchExcludeFromRecents.setOnCheckedChangeListener { _, checked ->
            prefsUi().edit().putBoolean(KEY_EXCLUDE_FROM_RECENTS, checked).apply()
            applyRecentTasksExcludeFromRecents(checked)
            appendLog(
                if (checked) {
                    getString(R.string.log_exclude_from_recents_on)
                } else {
                    getString(R.string.log_exclude_from_recents_off)
                },
            )
        }
    }

    private fun setupKeepaliveActions() {
        binding.btnBatteryOptimization.setOnClickListener { requestIgnoreBatteryOptimizations() }
        binding.btnMiuiAutostart.setOnClickListener { openMiuiAutostartPage() }
    }

    private fun setupNotificationWatchdogUi() {
        binding.switchNotifWatchdog.isChecked = RelayUiPrefs.isNotificationWatchdogEnabled(this)
        binding.switchNotifWatchdog.setOnCheckedChangeListener { _, checked ->
            prefsUi().edit().putBoolean(RelayUiPrefs.KEY_NOTIF_WATCHDOG_ENABLED, checked).apply()
            notifyRelayServicePrefsChanged()
            appendLog(
                if (checked) {
                    getString(R.string.log_notif_watchdog_enabled)
                } else {
                    getString(R.string.log_notif_watchdog_disabled)
                },
            )
        }

        val choices = RelayUiPrefs.WATCHDOG_INTERVAL_CHOICES_SEC
        val labels = choices.map { sec ->
            when {
                sec < 60 -> "${sec} 秒"
                sec % 60 == 0 -> "${sec / 60} 分钟"
                else -> "${sec} 秒"
            }
        }
        binding.spinnerNotifWatchdogInterval.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, labels)

        val savedSec = RelayUiPrefs.getNotificationWatchdogIntervalSec(this)
        val idx = choices.indexOf(savedSec).let { i ->
            if (i >= 0) i else choices.indexOf(RelayUiPrefs.DEFAULT_INTERVAL_SEC).coerceAtLeast(0)
        }

        binding.spinnerNotifWatchdogInterval.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    val sec = choices[position]
                    val prev = RelayUiPrefs.getNotificationWatchdogIntervalSec(this@SettingsActivity)
                    if (sec == prev) return
                    prefsUi().edit().putInt(RelayUiPrefs.KEY_NOTIF_WATCHDOG_INTERVAL_SEC, sec).apply()
                    notifyRelayServicePrefsChanged()
                    appendLog(getString(R.string.log_notif_watchdog_interval, sec))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        binding.spinnerNotifWatchdogInterval.setSelection(idx)
    }

    private fun setupRelayHeartbeatUi() {
        binding.switchRelayHeartbeat.isChecked = RelayUiPrefs.isRelayHeartbeatEnabled(this)
        binding.switchRelayHeartbeat.setOnCheckedChangeListener { _, checked ->
            prefsUi().edit().putBoolean(RelayUiPrefs.KEY_RELAY_HEARTBEAT_ENABLED, checked).apply()
            notifyRelayServicePrefsChanged()
            appendLog(
                if (checked) {
                    getString(R.string.log_relay_heartbeat_enabled)
                } else {
                    getString(R.string.log_relay_heartbeat_disabled)
                },
            )
        }

        val choices = RelayUiPrefs.RELAY_HEARTBEAT_INTERVAL_CHOICES_SEC
        val labels = choices.map { sec ->
            when {
                sec < 60 -> "${sec} 秒"
                sec % 60 == 0 -> "${sec / 60} 分钟"
                else -> "${sec} 秒"
            }
        }
        binding.spinnerRelayHeartbeatInterval.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, labels)

        val savedSec = RelayUiPrefs.getRelayHeartbeatIntervalSec(this)
        val idx = choices.indexOf(savedSec).let { i ->
            if (i >= 0) i else choices.indexOf(RelayUiPrefs.DEFAULT_HEARTBEAT_INTERVAL_SEC).coerceAtLeast(0)
        }

        binding.spinnerRelayHeartbeatInterval.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    val sec = choices[position]
                    val prev = RelayUiPrefs.getRelayHeartbeatIntervalSec(this@SettingsActivity)
                    if (sec == prev) return
                    prefsUi().edit().putInt(RelayUiPrefs.KEY_RELAY_HEARTBEAT_INTERVAL_SEC, sec).apply()
                    notifyRelayServicePrefsChanged()
                    appendLog(getString(R.string.log_relay_heartbeat_interval, sec))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        binding.spinnerRelayHeartbeatInterval.setSelection(idx)
    }

    private fun setupWatchGpsDeferUi() {
        val choices = RelayUiPrefs.WATCH_GPS_AFTER_PHONE_FAILURES_CHOICES
        val labels = choices.map { n -> "$n 次" }
        binding.spinnerWatchGpsDeferFailures.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, labels)

        val saved = RelayUiPrefs.getWatchGpsAfterPhoneFailures(this)
        val idx = choices.indexOf(saved).let { i ->
            if (i >= 0) {
                i
            } else {
                choices.indexOf(RelayUiPrefs.DEFAULT_WATCH_GPS_AFTER_PHONE_FAILURES).coerceAtLeast(0)
            }
        }

        binding.spinnerWatchGpsDeferFailures.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    val n = choices[position]
                    val prev = RelayUiPrefs.getWatchGpsAfterPhoneFailures(this@SettingsActivity)
                    if (n == prev) return
                    prefsUi().edit().putInt(RelayUiPrefs.KEY_WATCH_GPS_AFTER_PHONE_FAILURES, n).apply()
                    notifyRelayServicePrefsChanged()
                    appendLog(getString(R.string.log_watch_gps_defer, n))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        binding.spinnerWatchGpsDeferFailures.setSelection(idx)
    }

    private fun setupVibeCodingUi() {
        binding.inputVibeStatusApiUrl.setText(RelayUiPrefs.getVibeStatusApiUrl(this))
        binding.switchWatchDefaultHomeVibe.isChecked = RelayUiPrefs.isWatchDefaultHomeVibe(this)
        binding.switchWatchDefaultHomeVibe.setOnCheckedChangeListener { _, checked ->
            prefsUi().edit().putBoolean(RelayUiPrefs.KEY_WATCH_DEFAULT_HOME_VIBE, checked).apply()
            notifyRelayServicePrefsChanged()
            appendLog(
                if (checked) {
                    getString(R.string.log_watch_default_home_vibe_on)
                } else {
                    getString(R.string.log_watch_default_home_vibe_off)
                },
            )
        }
    }

    private fun saveVibeStatusApiUrlIfChanged() {
        val url = binding.inputVibeStatusApiUrl.text?.toString()?.trim().orEmpty()
        if (url.isEmpty()) return
        val prev = RelayUiPrefs.getVibeStatusApiUrl(this)
        if (url == prev) return
        prefsUi().edit().putString(RelayUiPrefs.KEY_VIBE_STATUS_API_URL, url).apply()
        notifyRelayServicePrefsChanged()
        appendLog(getString(R.string.log_vibe_status_api_saved, url))
    }

    private fun setupWatchDebugUiSwitch() {
        binding.switchWatchDebugUi.isChecked = RelayUiPrefs.isShowWatchDebugUi(this)
        binding.switchWatchDebugUi.setOnCheckedChangeListener { _, checked ->
            prefsUi().edit().putBoolean(RelayUiPrefs.KEY_SHOW_WATCH_DEBUG_UI, checked).apply()
            notifyRelayServicePrefsChanged()
            appendLog(
                if (checked) {
                    getString(R.string.log_watch_debug_ui_on)
                } else {
                    getString(R.string.log_watch_debug_ui_off)
                },
            )
        }
    }

    private fun notifyRelayServicePrefsChanged() {
        if (!XmsWearSdkBridge.isSdkOnClasspath()) return
        if (!PhoneLocationHelper.hasLocationPermission(this)) return
        runCatching {
            val i = Intent(this, LocationRelayService::class.java).apply {
                action = LocationRelayService.ACTION_REAPPLY_RELAY_PREFS
            }
            ContextCompat.startForegroundService(this, i)
        }
    }

    private fun refreshBatteryOptBanner() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val pm = getSystemService(POWER_SERVICE) as PowerManager
            val ok = pm.isIgnoringBatteryOptimizations(packageName)
            binding.batteryOptStatus.text = if (ok) {
                getString(R.string.battery_opt_ok)
            } else {
                getString(R.string.battery_opt_need)
            }
        } else {
            binding.batteryOptStatus.text = getString(R.string.battery_opt_legacy)
        }
    }

    private fun requestIgnoreBatteryOptimizations() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            refreshBatteryOptBanner()
            return
        }
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        if (pm.isIgnoringBatteryOptimizations(packageName)) {
            appendLog("电池优化已对本应用忽略限制")
            refreshBatteryOptBanner()
            return
        }
        try {
            startActivity(
                Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = Uri.parse("package:$packageName")
                },
            )
            appendLog(getString(R.string.log_battery_whitelist_launched))
        } catch (_: Exception) {
            try {
                startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
                appendLog(getString(R.string.log_battery_whitelist_launched))
            } catch (_: Exception) {
                openAppDetailSettingsFallback()
            }
        }
    }

    private fun openAppDetailSettingsFallback() {
        try {
            startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                },
            )
            appendLog("已打开应用信息页，请在「省电」「后台」相关项中允许后台")
        } catch (_: Exception) {
            appendLog("无法打开设置页，请手动在系统设置中找到本应用")
        }
    }

    private fun openMiuiAutostartPage() {
        val candidates = listOf(
            android.content.ComponentName(
                "com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity",
            ),
            android.content.ComponentName(
                "com.miui.securitycenter",
                "com.miui.powerkeeper.ui.HiddenAppsConfigActivity",
            ),
        )
        for (cn in candidates) {
            try {
                startActivity(Intent().setComponent(cn))
                appendLog(getString(R.string.log_miui_autostart_launched))
                return
            } catch (_: Exception) {
                continue
            }
        }
        appendLog(getString(R.string.log_miui_autostart_unavailable))
        openAppDetailSettingsFallback()
    }

    private fun applyRecentTasksExcludeFromRecents(exclude: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        runCatching {
            am.appTasks.forEach { task ->
                task.setExcludeFromRecents(exclude)
            }
        }
    }

    private fun runInterconnectSelfCheck() {
        val r = InterconnectDebugVerifier.verify(this)
        if (r.allOkForInterconnect) {
            binding.interconnectSelfCheck.text = getString(R.string.verify_title_ok)
            binding.interconnectSelfCheck.setBackgroundColor(Color.argb(40, 76, 175, 80))
            binding.interconnectSelfCheck.setTextColor(Color.rgb(27, 94, 32))
            appendLog("[互联自检] 包名=${packageName} ✓  签名公钥与 assets/certificate.pem ✓")
        } else {
            binding.interconnectSelfCheck.text = getString(R.string.verify_title_fail) + "\n" + r.detailLine
            binding.interconnectSelfCheck.setBackgroundColor(Color.argb(40, 211, 47, 47))
            binding.interconnectSelfCheck.setTextColor(Color.rgb(183, 28, 28))
            appendLog("[互联自检] 未通过：${r.detailLine}")
        }
        r.sha256ApkCert?.let { appendLog("[互联自检] APK 签名证书 SHA-256: $it") }
        r.sha256AssetCert?.let { appendLog("[互联自检] assets 证书 SHA-256: $it") }
        if (r.certAssetPresent && r.sha256ApkCert != null && r.sha256AssetCert != null && r.sha256ApkCert != r.sha256AssetCert) {
            appendLog("[互联自检] 两处 SHA-256 不同即表示签名与快应用证书不一致。")
        }
    }

    private fun refreshSdkBanner() {
        val ok = XmsWearSdkBridge.isSdkOnClasspath()
        binding.sdkStatus.text = if (ok) {
            getString(R.string.sdk_status_ok)
        } else {
            getString(R.string.sdk_status_missing)
        }
    }

    private fun appendLog(line: String) {
        val stamp = timeFmt.format(Date())
        logBuf.append('[').append(stamp).append("] ").append(line).append('\n')
        if (logBuf.length > 12000) {
            logBuf.delete(0, logBuf.length - 8000)
        }
        binding.logView.text = logBuf.toString()
        binding.settingsScroll.post { binding.settingsScroll.fullScroll(View.FOCUS_DOWN) }
    }

    private fun refreshNodes() {
        binding.nodeStatus.text = getString(R.string.node_querying)
        XmsWearSdkBridge.fetchConnectedNodeIds(
            this,
            onResult = { ids ->
                if (ids.isEmpty()) {
                    currentNodeId = null
                    binding.nodeStatus.text = getString(R.string.node_none)
                    appendLog(getString(R.string.log_no_node_checklist))
                } else {
                    currentNodeId = ids.first()
                    binding.nodeStatus.text = getString(R.string.node_one, currentNodeId!!)
                    appendLog("已连接节点: ${ids.joinToString()}")
                }
            },
            onError = { msg ->
                binding.nodeStatus.text = getString(R.string.node_error, msg)
                appendLog("查询节点失败: $msg")
            },
        )
    }

    private fun requestWearPermission() {
        val id = currentNodeId ?: run {
            appendLog("请先刷新并获取节点")
            return
        }
        appendLog("正在申请 DEVICE_MANAGER / NOTIFY …")
        XmsWearSdkBridge.requestDeviceManagerPermission(
            this,
            id,
            onOk = { appendLog("权限申请流程已返回（请在小米穿戴中确认第三方授权）") },
            onErr = { appendLog("申请权限失败: $it") },
        )
    }

    private fun registerListener() {
        appendLog(getString(R.string.log_relay_listener_hint))
    }

    private fun requestLocationPermissionOnly() {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ),
        )
    }

    private fun maybeRequestBackgroundLocationAfterForegroundGrant() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        appendLog(getString(R.string.log_requesting_background_location))
        backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }

    private fun onSendLocationClicked() {
        if (currentNodeId == null) {
            appendLog("请先刷新并获取节点")
            return
        }
        if (!PhoneLocationHelper.hasLocationPermission(this)) {
            appendLog("需要定位权限，正在弹出系统授权…")
            sendLocationAfterLocationGrant = true
            requestLocationPermissionOnly()
            return
        }
        sendCurrentLocationToWatch()
    }

    private fun sendCurrentLocationToWatch() {
        val id = currentNodeId ?: run {
            appendLog("请先刷新并获取节点")
            return
        }
        if (!PhoneLocationHelper.hasLocationPermission(this)) {
            appendLog("未获得定位权限")
            return
        }
        val quick = PhoneLocationCache.peekForQuickReply(
            this,
            PhoneLocationCache.QUICK_REPLY_MAX_WALL_MS,
            PhoneLocationCache.QUICK_REPLY_MAX_FIX_AGE_MS,
        )
        if (quick != null) {
            appendLog("使用近期缓存位置（中继预热），后台刷新中…")
            val json = PhoneLocationPayload.toJson(quick)
            appendLog("→ 发送位置: $json")
            XmsWearSdkBridge.sendTextToNode(
                this@SettingsActivity,
                id,
                json,
                onOk = { appendLog("位置已发送") },
                onErr = { appendLog("发送失败: $it") },
            )
            PhoneLocationHelper.fetchBestLocation(this, highAccuracy = true) { }
            return
        }
        if (sendLocationInFlight) {
            appendLog("正在获取当前位置，请稍候…")
            return
        }
        sendLocationInFlight = true
        appendLog("正在获取当前位置…")
        PhoneLocationHelper.fetchBestLocation(this, highAccuracy = true) { loc ->
            mainHandler.post {
                sendLocationInFlight = false
                if (loc == null) {
                    appendLog("定位失败：无可用位置（请打开 GPS 或到室外重试）")
                    return@post
                }
                val json = PhoneLocationPayload.toJson(loc)
                appendLog("→ 发送位置: $json")
                XmsWearSdkBridge.sendTextToNode(
                    this@SettingsActivity,
                    id,
                    json,
                    onOk = { appendLog("位置已发送") },
                    onErr = { appendLog("发送失败: $it") },
                )
            }
        }
    }

    private fun sendMessage() {
        val id = currentNodeId ?: run {
            appendLog("请先刷新并获取节点")
            return
        }
        val text = binding.inputMessage.text?.toString()?.trim().orEmpty()
        if (text.isEmpty()) {
            appendLog("请输入要发送的文本")
            return
        }
        appendLog("→ 发送: $text")
        XmsWearSdkBridge.sendTextToNode(
            this,
            id,
            text,
            onOk = { appendLog("发送成功") },
            onErr = { appendLog("发送失败: $it") },
        )
    }
}
