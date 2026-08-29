package uno.keyin.bus

import java.util.concurrent.Semaphore

object StationLinesRepository {
    private const val CACHE_TTL_MS = 3 * 60 * 1000L

    private data class CacheEntry(val lines: List<BusLineUi>, val expiresAt: Long)

    private val lock = Any()
    private val cache = mutableMapOf<String, CacheEntry>()
    private val pending = mutableMapOf<String, MutableList<(Result<List<BusLineUi>>) -> Unit>>()
    private val requestSlots = Semaphore(2)

    fun load(
        city: CityConfig,
        stationName: String,
        lat: String,
        lng: String,
        force: Boolean = false,
        callback: (Result<List<BusLineUi>>) -> Unit,
    ) {
        val key = "${city.version}|$stationName|$lat|$lng"
        val cached = synchronized(lock) {
            cache[key]?.takeIf { !force && it.expiresAt > System.currentTimeMillis() }
        }
        if (cached != null) {
            callback(Result.success(cached.lines))
            return
        }

        synchronized(lock) {
            val callbacks = pending[key]
            if (callbacks != null) {
                callbacks += callback
                return
            }
            pending[key] = mutableListOf(callback)
        }

        BusApiClient.executor.execute {
            requestSlots.acquireUninterruptibly()
            val result = try {
                runCatching { BusApiClient.loadStationLines(city, stationName, lat, lng) }
            } finally {
                requestSlots.release()
            }
            val callbacks = synchronized(lock) {
                if (result.isSuccess) {
                    cache[key] = CacheEntry(result.getOrThrow(), System.currentTimeMillis() + CACHE_TTL_MS)
                }
                pending.remove(key).orEmpty()
            }
            callbacks.forEach { it(result) }
        }
    }
}
