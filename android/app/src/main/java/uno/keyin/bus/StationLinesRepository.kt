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
                runCatching { loadResolvedLines(city, stationName, lat, lng) }
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

    private fun loadResolvedLines(
        city: CityConfig,
        stationName: String,
        lat: String,
        lng: String,
    ): List<BusLineUi> {
        val resolved = runCatching {
            BusApiClient.loadStationPlatforms(city, stationName, lat, lng)
        }.getOrDefault(emptyList())
        val platforms = resolved.ifEmpty {
            listOf(StationPlatform(stationName, lat, lng))
        }
        var lastFailure: Throwable? = null
        var successfulQueries = 0
        val lines = buildList {
            platforms.forEachIndexed { index, platform ->
                runCatching {
                    BusApiClient.loadStationLines(city, platform.name, platform.lat, platform.lng)
                }.onSuccess { values ->
                    successfulQueries += 1
                    val label = if (platforms.size > 1) "${index + 1}号站台" else ""
                    addAll(values.map { line ->
                        line.copy(
                            platformName = platform.name,
                            platformLat = platform.lat,
                            platformLng = platform.lng,
                            platformLabel = label,
                        )
                    })
                }.onFailure { lastFailure = it }
            }
        }
        if (successfulQueries == 0 && lastFailure != null) throw lastFailure!!
        return lines.distinctBy { "${it.id}|${it.directionCode}|${it.direction}" }
    }
}
