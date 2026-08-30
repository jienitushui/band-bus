package uno.keyin.bus.location

import android.location.Location
import android.content.Context

/** 持久化最近一次有效坐标，供首页和手表 [request_location] 优先复用。 */
object PhoneLocationCache {

    private const val PREFS_NAME = "bus_location_cache"
    private const val KEY_LAT = "lat"
    private const val KEY_LNG = "lng"
    private const val KEY_FIX_TIME = "fix_time"
    private const val KEY_STORED_AT = "stored_at"
    private const val KEY_PROVIDER = "provider"
    private const val KEY_ACCURACY = "accuracy"

    /** 与 [LocationRelayService]、手动发坐标共用：缓存写入后的墙钟新鲜度上限 */
    const val QUICK_REPLY_MAX_WALL_MS: Long = 120_000L

    /** 定位点 [Location.getTime] 相对当前时间的上限（与墙钟一起满足才可秒回） */
    const val QUICK_REPLY_MAX_FIX_AGE_MS: Long = 120_000L

    private val lock = Any()
    private var cached: Location? = null
    private var storedAtWallMs: Long = 0L

    fun put(context: Context, location: Location) {
        synchronized(lock) {
            cached = location
            storedAtWallMs = System.currentTimeMillis()
            context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putLong(KEY_LAT, location.latitude.toBits())
                .putLong(KEY_LNG, location.longitude.toBits())
                .putLong(KEY_FIX_TIME, location.time)
                .putLong(KEY_STORED_AT, storedAtWallMs)
                .putString(KEY_PROVIDER, location.provider.orEmpty())
                .putInt(KEY_ACCURACY, if (location.hasAccuracy()) location.accuracy.toBits() else 0)
                .apply()
        }
    }

    /**
     * @param maxWallMs 写入缓存后经过的时间上限
     * @param maxFixAgeMs 定位点 [Location.getTime] 相对当前时间上限（两点都满足才可用）
     */
    fun peekForQuickReply(context: Context, maxWallMs: Long, maxFixAgeMs: Long): Location? {
        synchronized(lock) {
            val loc = cached ?: restore(context) ?: return null
            val now = System.currentTimeMillis()
            if (now - storedAtWallMs > maxWallMs) return null
            if (now - loc.time > maxFixAgeMs) return null
            return loc
        }
    }

    private fun restore(context: Context): Location? {
        val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedAt = prefs.getLong(KEY_STORED_AT, 0L)
        val fixTime = prefs.getLong(KEY_FIX_TIME, 0L)
        if (savedAt <= 0L || fixTime <= 0L || !prefs.contains(KEY_LAT) || !prefs.contains(KEY_LNG)) {
            return null
        }
        val location = Location(prefs.getString(KEY_PROVIDER, "cache").orEmpty().ifBlank { "cache" }).apply {
            latitude = Double.fromBits(prefs.getLong(KEY_LAT, 0L))
            longitude = Double.fromBits(prefs.getLong(KEY_LNG, 0L))
            time = fixTime
            val accuracyBits = prefs.getInt(KEY_ACCURACY, 0)
            if (accuracyBits != 0) accuracy = Float.fromBits(accuracyBits)
        }
        cached = location
        storedAtWallMs = savedAt
        return location
    }
}
