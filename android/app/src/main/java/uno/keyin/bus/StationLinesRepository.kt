package uno.keyin.bus

import android.content.Context
import android.util.Base64
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.Semaphore

object StationLinesRepository {
    private const val CACHE_TTL_MS = 15_000L
    private const val STALE_FALLBACK_MAX_AGE_MS = 30 * 60 * 1000L
    private const val PREFS_NAME = "bus_station_lines_cache"

    private data class CacheEntry(val lines: List<BusLineUi>, val savedAt: Long) {
        fun isFresh(now: Long): Boolean = now - savedAt <= CACHE_TTL_MS
        fun canFallback(now: Long): Boolean = now - savedAt <= STALE_FALLBACK_MAX_AGE_MS
    }

    private val lock = Any()
    private val cache = mutableMapOf<String, CacheEntry>()
    private val pending = mutableMapOf<String, MutableList<(Result<List<BusLineUi>>) -> Unit>>()
    private val requestSlots = Semaphore(2)

    fun load(
        context: Context,
        city: CityConfig,
        stationName: String,
        lat: String,
        lng: String,
        force: Boolean = false,
        callback: (Result<List<BusLineUi>>) -> Unit,
    ) {
        val key = "${city.version}|$stationName|$lat|$lng"
        val now = System.currentTimeMillis()
        val cached = synchronized(lock) {
            cache[key]
        } ?: readDisk(context.applicationContext, key)?.also { entry ->
            synchronized(lock) { cache[key] = entry }
        }
        if (!force && cached?.isFresh(now) == true) {
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
            val networkResult = try {
                runCatching { loadResolvedLines(city, stationName, lat, lng) }
            } finally {
                requestSlots.release()
            }
            val result = if (networkResult.isFailure && cached?.canFallback(System.currentTimeMillis()) == true) {
                Result.success(cached.lines)
            } else {
                networkResult
            }
            val callbacks = synchronized(lock) {
                if (networkResult.isSuccess) {
                    val entry = CacheEntry(networkResult.getOrThrow(), System.currentTimeMillis())
                    cache[key] = entry
                    writeDisk(context.applicationContext, key, entry)
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

    private fun diskKey(key: String): String = Base64.encodeToString(
        key.toByteArray(Charsets.UTF_8),
        Base64.NO_WRAP or Base64.URL_SAFE,
    )

    private fun readDisk(context: Context, key: String): CacheEntry? = runCatching {
        val raw = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(diskKey(key), "").orEmpty()
        if (raw.isBlank()) return null
        val root = JSONObject(raw)
        val savedAt = root.optLong("savedAt", 0L)
        if (savedAt <= 0L || System.currentTimeMillis() - savedAt > STALE_FALLBACK_MAX_AGE_MS) return null
        val rows = root.optJSONArray("lines") ?: return null
        CacheEntry(buildList {
            for (index in 0 until rows.length()) {
                val item = rows.optJSONObject(index) ?: continue
                add(
                    BusLineUi(
                        id = item.optString("id"),
                        direction = item.optString("direction"),
                        statusMain = item.optString("statusMain"),
                        statusSub = item.optString("statusSub").takeIf { it.isNotBlank() },
                        directionCode = item.optString("directionCode", "1"),
                        stationOrder = item.optInt("stationOrder", 0),
                        platformName = item.optString("platformName"),
                        platformLat = item.optString("platformLat"),
                        platformLng = item.optString("platformLng"),
                        platformLabel = item.optString("platformLabel"),
                    ),
                )
            }
        }, savedAt)
    }.getOrNull()

    private fun writeDisk(context: Context, key: String, entry: CacheEntry) {
        val lines = JSONArray()
        entry.lines.forEach { line ->
            lines.put(JSONObject().apply {
                put("id", line.id)
                put("direction", line.direction)
                put("statusMain", line.statusMain)
                put("statusSub", line.statusSub ?: "")
                put("directionCode", line.directionCode)
                put("stationOrder", line.stationOrder)
                put("platformName", line.platformName)
                put("platformLat", line.platformLat)
                put("platformLng", line.platformLng)
                put("platformLabel", line.platformLabel)
            })
        }
        val root = JSONObject().apply {
            put("savedAt", entry.savedAt)
            put("lines", lines)
        }
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(diskKey(key), root.toString()).apply()
    }
}
