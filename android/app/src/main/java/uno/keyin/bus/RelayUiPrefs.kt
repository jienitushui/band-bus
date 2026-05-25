package uno.keyin.bus

import android.content.Context

/** 与 MainActivity / LocationRelayService 共用的界面与中继偏好（同一 SharedPreferences 文件）。 */
object RelayUiPrefs {
    const val PREFS_NAME = "band_bus_ui"

    const val KEY_NOTIF_WATCHDOG_ENABLED = "notif_watchdog_enabled"
    const val KEY_NOTIF_WATCHDOG_INTERVAL_SEC = "notif_watchdog_interval_sec"

    const val KEY_RELAY_HEARTBEAT_ENABLED = "relay_heartbeat_enabled"
    const val KEY_RELAY_HEARTBEAT_INTERVAL_SEC = "relay_heartbeat_interval_sec"

    /** 手表每次点击定位：连续多少次未拿到手机回包后，才启用本机 GPS（手表会缓存手机心跳下发的值）。 */
    const val KEY_WATCH_GPS_AFTER_PHONE_FAILURES = "watch_gps_after_phone_failures"

    /** 是否在手表首页显示互联调试条、坐标调试条（经心跳下发，手表本地缓存）。 */
    const val KEY_SHOW_WATCH_DEBUG_UI = "show_watch_debug_ui"

    /** 手表轮询 Cursor 状态 API（GET /status），经心跳下发。须为 http:// 局域网地址。 */
    const val KEY_VIBE_STATUS_API_URL = "vibe_status_api_url"

    /** 手表启动后默认进入 Vibe Coding 状态页而非公交首页。 */
    const val KEY_WATCH_DEFAULT_HOME_VIBE = "watch_default_home_vibe"

    const val DEFAULT_VIBE_STATUS_API_URL = "http://127.0.0.1:3000/status"

    const val DEFAULT_INTERVAL_SEC = 60
    const val MIN_INTERVAL_SEC = 15
    const val MAX_INTERVAL_SEC = 3600

    /** 可选的检查间隔（秒），用于 Spinner 与校验。 */
    val WATCHDOG_INTERVAL_CHOICES_SEC = intArrayOf(15, 30, 60, 120, 300, 600)

    val RELAY_HEARTBEAT_INTERVAL_CHOICES_SEC = intArrayOf(30, 45, 60, 90, 120, 180)

    const val DEFAULT_HEARTBEAT_INTERVAL_SEC = 45
    const val MIN_HEARTBEAT_INTERVAL_SEC = 20
    const val MAX_HEARTBEAT_INTERVAL_SEC = 600

    const val DEFAULT_WATCH_GPS_AFTER_PHONE_FAILURES = 2
    const val MIN_WATCH_GPS_AFTER_PHONE_FAILURES = 1
    const val MAX_WATCH_GPS_AFTER_PHONE_FAILURES = 20

    val WATCH_GPS_AFTER_PHONE_FAILURES_CHOICES = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 10, 15, 20)

    fun isNotificationWatchdogEnabled(context: Context): Boolean =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_NOTIF_WATCHDOG_ENABLED, true)

    fun getNotificationWatchdogIntervalSec(context: Context): Int {
        val raw = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_NOTIF_WATCHDOG_INTERVAL_SEC, DEFAULT_INTERVAL_SEC)
        return raw.coerceIn(MIN_INTERVAL_SEC, MAX_INTERVAL_SEC)
    }

    fun isRelayHeartbeatEnabled(context: Context): Boolean =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_RELAY_HEARTBEAT_ENABLED, true)

    fun getRelayHeartbeatIntervalSec(context: Context): Int {
        val raw = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_RELAY_HEARTBEAT_INTERVAL_SEC, DEFAULT_HEARTBEAT_INTERVAL_SEC)
        return raw.coerceIn(MIN_HEARTBEAT_INTERVAL_SEC, MAX_HEARTBEAT_INTERVAL_SEC)
    }

    fun getWatchGpsAfterPhoneFailures(context: Context): Int {
        val raw = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_WATCH_GPS_AFTER_PHONE_FAILURES, DEFAULT_WATCH_GPS_AFTER_PHONE_FAILURES)
        return raw.coerceIn(MIN_WATCH_GPS_AFTER_PHONE_FAILURES, MAX_WATCH_GPS_AFTER_PHONE_FAILURES)
    }

    fun isShowWatchDebugUi(context: Context): Boolean =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_SHOW_WATCH_DEBUG_UI, false)

    fun getVibeStatusApiUrl(context: Context): String {
        val raw = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_VIBE_STATUS_API_URL, DEFAULT_VIBE_STATUS_API_URL)
            ?.trim()
            .orEmpty()
        return raw.ifEmpty { DEFAULT_VIBE_STATUS_API_URL }
    }

    fun isWatchDefaultHomeVibe(context: Context): Boolean =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_WATCH_DEFAULT_HOME_VIBE, false)
}
