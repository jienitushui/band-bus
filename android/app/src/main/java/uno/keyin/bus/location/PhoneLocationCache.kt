package uno.keyin.bus.location

import android.location.Location

/**
 * 中继服务进程内缓存最近一次有效坐标，供手表 [request_location] 秒回，避免每次冷启 Fused 单次定位。
 */
object PhoneLocationCache {

    /** 与 [LocationRelayService]、手动发坐标共用：缓存写入后的墙钟新鲜度上限 */
    const val QUICK_REPLY_MAX_WALL_MS: Long = 75_000L

    /** 定位点 [Location.getTime] 相对当前时间的上限（与墙钟一起满足才可秒回） */
    const val QUICK_REPLY_MAX_FIX_AGE_MS: Long = 120_000L

    private val lock = Any()
    private var cached: Location? = null
    private var storedAtWallMs: Long = 0L

    fun put(location: Location) {
        synchronized(lock) {
            cached = location
            storedAtWallMs = System.currentTimeMillis()
        }
    }

    /**
     * @param maxWallMs 写入缓存后经过的时间上限
     * @param maxFixAgeMs 定位点 [Location.getTime] 相对当前时间上限（两点都满足才可用）
     */
    fun peekForQuickReply(maxWallMs: Long, maxFixAgeMs: Long): Location? {
        synchronized(lock) {
            val loc = cached ?: return null
            val now = System.currentTimeMillis()
            if (now - storedAtWallMs > maxWallMs) return null
            if (now - loc.time > maxFixAgeMs) return null
            return loc
        }
    }
}
