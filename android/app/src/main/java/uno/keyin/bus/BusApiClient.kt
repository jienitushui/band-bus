package uno.keyin.bus

import android.util.Log
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs

object BusApiClient {
    private const val API_URL = "https://h5.mygolbs.com/ApiData.do"
    private const val SEARCH_CACHE_TTL_MS = 60_000L
    private const val RETRY_START_BUDGET_MS = 2_500L
    val executor: ExecutorService = Executors.newFixedThreadPool(4)
    private val searchCache = ConcurrentHashMap<String, SearchCacheEntry>()

    private data class SearchCacheEntry(val savedAt: Long, val results: List<SearchResult>)

    fun fetchCities(): List<String> {
        val json = post(mapOf("CMD" to "101"))
        check(json.optInt("status") == 1) { json.optString("msg", "城市列表加载失败") }
        val data = json.optJSONArray("data") ?: return emptyList()
        return buildList {
            for (i in 0 until data.length()) {
                data.optJSONObject(i)?.optString("cityname")?.trim()
                    ?.takeIf { it.isNotEmpty() }?.let(::add)
            }
        }.distinct()
    }

    fun validateCity(cityName: String): Boolean = post(
        mapOf("CMD" to "204", "CITYNAME" to cityName),
    ).optInt("status") == 1

    fun search(city: CityConfig, keyword: String): List<SearchResult> {
        val normalized = keyword.trim()
        val cacheKey = "${city.cityName}|${city.cityKey}|${normalized.lowercase()}"
        val now = System.currentTimeMillis()
        searchCache[cacheKey]?.takeIf { now - it.savedAt <= SEARCH_CACHE_TTL_MS }?.let {
            return it.results
        }
        val params = mapOf(
            "CITYNAME" to city.cityName,
            "CITYKEY" to city.cityKey,
            "KEYWORD" to normalized,
        )
        val results = runCatching {
            val json = postWithRetry(params + ("CMD" to "102"))
            check(json.optInt("status") == 1) { json.optString("msg", "搜索失败") }
            parseSearchResults(json, includeLines = true, includeStations = true)
        }.getOrElse {
            val lineJson = postWithRetry(params + ("CMD" to "114"))
            val stationJson = postWithRetry(params + ("CMD" to "110"))
            parseSearchResults(lineJson, includeLines = true, includeStations = false) +
                parseSearchResults(stationJson, includeLines = false, includeStations = true)
        }.let(::mergeSearchResults)
        searchCache[cacheKey] = SearchCacheEntry(now, results)
        return results
    }

    internal fun parseSearchResults(
        json: JSONObject,
        includeLines: Boolean,
        includeStations: Boolean,
    ): List<SearchResult> {
        check(json.optInt("status") == 1) { json.optString("msg", "搜索失败") }
        val results = ArrayList<SearchResult>()
        if (includeLines) {
            val lines = json.optJSONArray("buslines")
            for (i in 0 until minOf(lines?.length() ?: 0, 8)) {
                val item = lines?.optJSONObject(i) ?: continue
                val name = item.optString("lineName").trim()
                if (name.isNotEmpty()) results += SearchResult(
                    SearchResultType.LINE, name,
                    "${item.optString("from").ifBlank { "起点" }} → ${item.optString("to").ifBlank { "终点" }}",
                    name, item.optString("upperOrDown", "1"),
                )
            }
        }
        if (includeStations) {
            val stations = json.optJSONArray("busstations")
            for (i in 0 until minOf(stations?.length() ?: 0, 8)) {
                val item = stations?.optJSONObject(i) ?: continue
                val name = item.optString("stationName").trim()
                if (name.isNotEmpty()) results += SearchResult(SearchResultType.STATION, name, "查看经过该站线路")
            }
        }
        return results
    }

    internal fun mergeSearchResults(results: List<SearchResult>): List<SearchResult> =
        results.distinctBy { "${it.type}:${it.name}" }

    fun loadTransfer(
        city: CityConfig,
        start: String,
        end: String,
        startLat: String = "",
        startLng: String = "",
    ): List<TransferScheme> {
        val params = mapOf(
            "CITYNAME" to city.cityName, "CITYKEY" to city.cityKey,
            "STARTPOINTNAME" to start, "STARTPOINTLNG" to startLng, "STARTPOINTLAT" to startLat,
            "ENDPOINTNAME" to end, "ENDPOINTLNG" to "", "ENDPOINTLAT" to "",
        )
        val modern = runCatching { postWithRetry(params + ("CMD" to "118")) }.getOrNull()
        val modernResults = modern?.let { parseModernTransfer(it, start, end) }.orEmpty()
        if (modernResults.isNotEmpty()) return loadTransferRealtime(city, modernResults)
        val json = postWithRetry(params + ("CMD" to "111"))
        check(json.optInt("status") == 1) { json.optString("msg", "换乘查询失败") }
        val data = json.optJSONArray("data") ?: return emptyList()
        return buildList {
            for (i in 0 until data.length()) {
                val item = data.optJSONObject(i) ?: continue
                add(TransferScheme(
                    startStation = start,
                    endStation = end,
                    startLine = item.optString("startLineName"),
                    changeStation = item.optString("endChangeStation"),
                    endLine = item.optString("endLineName"),
                    boardingStation = item.optString("startStation"),
                ))
            }
        }
    }

    internal fun parseModernTransfer(json: JSONObject, start: String, end: String): List<TransferScheme> {
        if (json.optInt("status") != 1) return emptyList()
        val data = json.optJSONArray("info") ?: return emptyList()
        return buildList {
            for (i in 0 until data.length()) {
                val item = data.optJSONObject(i) ?: continue
                val lines = item.optJSONArray("lines")
                val names = buildList {
                    for (j in 0 until (lines?.length() ?: 0)) {
                        lines?.optJSONObject(j)?.optString("lineNames")?.trim()?.takeIf { it.isNotEmpty() }?.let { add(it.replace("#", " / ")) }
                    }
                }
                val firstLine = lines?.optJSONObject(0)
                val detailedLegs = parseTransferLegs(item)
                val fallbackLegs = if (detailedLegs.isEmpty()) {
                    names.mapIndexed { index, group ->
                        TransferLeg(group.split(" / ").filter { it.isNotBlank() }.map { lineName ->
                            TransferLineOption(
                                lineName = lineName,
                                boardStation = if (index == 0) item.optString("upStation") else "",
                                alightStation = if (index == names.lastIndex) item.optString("downStation") else "",
                            )
                        })
                    }
                } else {
                    detailedLegs
                }
                val segmentNames = fallbackLegs.map { it.lineNames }.filter { it.isNotBlank() }
                val boardingStation = fallbackLegs.firstOrNull()?.boardStation.orEmpty()
                    .ifBlank { item.optString("upStation") }
                val alightingStation = fallbackLegs.lastOrNull()?.alightStation.orEmpty()
                    .ifBlank { item.optString("downStation") }
                val changeStation = fallbackLegs.getOrNull(1)?.boardStation.orEmpty()
                add(TransferScheme(
                    startStation = item.optString("startName").ifBlank { start },
                    endStation = item.optString("endName").ifBlank { end },
                    startLine = segmentNames.firstOrNull().orEmpty(),
                    changeStation = changeStation,
                    endLine = segmentNames.drop(1).joinToString(" → "),
                    totalTime = item.optString("totalTime"),
                    walkDistance = item.optString("totalWalkDistance"),
                    boardingStation = boardingStation,
                    alightingStation = alightingStation,
                    stationCount = item.optString("stationNum"),
                    lineSegments = segmentNames,
                    realtimeLine = firstLine?.optString("lineNames").orEmpty(),
                    realtimeDirection = firstLine?.optString("dirs").orEmpty(),
                    realtimeOrder = firstLine?.optString("orders").orEmpty(),
                    realtimeStation = firstLine?.optString("stations").orEmpty(),
                    startWalkDistance = item.optString("startWalkDistance"),
                    endWalkDistance = item.optString("endWalkDistance"),
                    totalDistance = item.optString("totalDistance").ifBlank { item.optString("busDistance") },
                    legs = fallbackLegs,
                ))
            }
        }
    }

    private fun parseTransferLegs(item: JSONObject): List<TransferLeg> = buildList {
        val keys = listOf(
            "routeUpDownSimple1" to "firstWalkDistance",
            "routeUpDownSimple2" to "secondWalkDistance",
            "routeUpDownSimple3" to "",
        )
        keys.forEach { (arrayKey, walkKey) ->
            val rows = item.optJSONArray(arrayKey) ?: return@forEach
            val options = buildList {
                for (index in 0 until rows.length()) {
                    val row = rows.optJSONObject(index) ?: continue
                    val lineName = row.optString("routeName").trim()
                    if (lineName.isBlank()) continue
                    val stationCount = if (row.has("upStationIndex") && row.has("downStationIndex")) {
                        abs(row.optInt("downStationIndex") - row.optInt("upStationIndex"))
                    } else {
                        null
                    }
                    add(TransferLineOption(
                        lineName = lineName,
                        direction = row.optString("endStationName").trim(),
                        boardStation = row.optString("upStationName").trim(),
                        alightStation = row.optString("downStationName").trim(),
                        stationCount = stationCount,
                        distance = row.optString("busDistance").trim(),
                        duration = row.optString("costTM").trim(),
                        entryName = row.optString("entryName").trim(),
                        exitName = row.optString("outName").trim(),
                    ))
                }
            }
            if (options.isNotEmpty()) {
                add(TransferLeg(
                    options = options,
                    walkAfterDistance = walkKey.takeIf { it.isNotBlank() }
                        ?.let { item.optString(it).trim() }.orEmpty(),
                ))
            }
        }
    }

    private fun loadTransferRealtime(city: CityConfig, schemes: List<TransferScheme>): List<TransferScheme> {
        if (schemes.any {
                it.realtimeLine.isBlank() || it.realtimeDirection.isBlank() ||
                    it.realtimeOrder.isBlank() || it.realtimeStation.isBlank()
            }
        ) return schemes
        return runCatching {
            val json = postWithRetry(mapOf(
                "CMD" to "112",
                "CITYNAME" to city.cityName,
                "CITYKEY" to city.cityKey,
                "REALLINE" to schemes.joinToString(",") { it.realtimeLine },
                "REALDIR" to schemes.joinToString(",") { it.realtimeDirection },
                "STATIONORDER" to schemes.joinToString(",") { it.realtimeOrder },
                "STATIONNAME" to schemes.joinToString(",") { it.realtimeStation },
            ))
            if (json.optInt("status") != 1) return@runCatching schemes
            val data = json.optJSONArray("data") ?: return@runCatching schemes
            schemes.mapIndexed { index, scheme ->
                val info = data.optJSONObject(index) ?: return@mapIndexed scheme
                val status = info.optString("staNum").trim()
                val planTime = info.optString("plantime").trim()
                val costTime = info.optString("costTm").trim()
                val text = when {
                    status == "等待发车" && planTime.isNotEmpty() -> "起点预计发车：$planTime"
                    costTime.isNotEmpty() -> "最近车辆：$status · $costTime"
                    else -> status
                }
                scheme.copy(
                    realtimeDisplayLine = info.optString("name").trim(),
                    realtimeText = text,
                )
            }
        }.getOrDefault(schemes)
    }

    fun searchStations(city: CityConfig, keyword: String): List<String> {
        val json = postWithRetry(mapOf(
            "CMD" to "110", "CITYNAME" to city.cityName, "CITYKEY" to city.cityKey,
            "KEYWORD" to keyword.trim(),
        ))
        check(json.optInt("status") == 1) { json.optString("msg", "站点搜索失败") }
        val data = json.optJSONArray("busstations") ?: return emptyList()
        return buildList {
            for (i in 0 until minOf(data.length(), 8)) {
                data.optJSONObject(i)?.optString("stationName")?.trim()
                    ?.takeIf { it.isNotEmpty() }?.let(::add)
            }
        }.distinct()
    }

    fun loadNearby(city: CityConfig, lat: Double, lng: Double): List<StationUi> {
        val nearby = post(
            mapOf(
                "CMD" to "106",
                "CITYNAME" to city.cityName,
                "CITYKEY" to city.cityKey,
                "LAT" to lat.toString(),
                "LNG" to lng.toString(),
            ),
        )
        check(nearby.optInt("status") == 1) { nearby.optString("msg", "附近站点加载失败") }
        val rows = nearby.optJSONArray("data") ?: return emptyList()
        val stations = ArrayList<StationUi>()
        for (i in 0 until rows.length()) {
            val item = rows.optJSONObject(i) ?: continue
            val name = item.optString("name").trim()
            if (name.isEmpty()) continue
            val distance = item.optDouble("dis", 0.0).toInt().coerceAtLeast(0)
            stations += StationUi(
                name = name,
                desc = "约 ${distance}m",
                lat = item.optString("lat"),
                lng = item.optString("lon"),
            )
        }
        return stations
    }

    fun loadStationLines(
        city: CityConfig,
        stationName: String,
        lat: String,
        lng: String,
    ): List<BusLineUi> {
        val json = post(
            mapOf(
                "CMD" to "115",
                "CITYNAME" to city.cityName,
                "CITYKEY" to city.cityKey,
                "STATIONNAME" to stationName,
                "MYLAT" to lat,
                "MYLNG" to lng,
                "ALL" to "1",
            ),
        )
        if (json.optInt("status") != 1) return emptyList()
        val data = json.optJSONArray("data") ?: return emptyList()
        return buildList {
            for (i in 0 until data.length()) {
                val item = data.optJSONObject(i) ?: continue
                val lineName = item.optString("lineName").ifBlank { "--" }
                val destination = item.optString("to").ifBlank {
                    item.optString("routeNameUp").ifBlank { "未知方向" }
                }
                val nearText = item.optString("neartext").trim()
                val nearTime = item.optString("neartime").trim()
                val nearDistance = item.optString("neardis").trim()
                val status = when {
                    nearText.isNotEmpty() -> nearText
                    nearTime.isNotEmpty() -> "$nearTime 分钟"
                    nearDistance.isNotEmpty() -> nearDistance
                    else -> "暂无预报"
                }
                add(
                    BusLineUi(
                        id = lineName,
                        direction = destination,
                        statusMain = status,
                        statusSub = nearDistance.takeIf { it.isNotEmpty() && it != status },
                        directionCode = item.optString("upperOrDown", "1"),
                        stationOrder = item.optInt("stationOrder", 0),
                    ),
                )
            }
        }
    }

    fun loadStationPlatforms(
        city: CityConfig,
        stationName: String,
        lat: String,
        lng: String,
    ): List<StationPlatform> {
        if (stationName.isBlank() || lat.isBlank() || lng.isBlank()) return emptyList()
        val json = postWithRetry(
            mapOf(
                "CMD" to "209",
                "CITYNAME" to city.cityName,
                "CITYKEY" to city.cityKey,
                "STATIONNAME" to stationName,
                "MYLAT" to lat,
                "MYLNG" to lng,
                "LAT" to lat,
                "LNG" to lng,
            ),
        )
        return parseStationPlatforms(json)
    }

    internal fun parseStationPlatforms(json: JSONObject): List<StationPlatform> {
        if (json.optInt("status") != 1) return emptyList()
        val rows = json.optJSONArray("info")
            ?: json.optJSONObject("data")?.optJSONArray("info")
            ?: return emptyList()
        return buildList {
            for (index in 0 until rows.length()) {
                val item = rows.optJSONObject(index) ?: continue
                val name = item.optString("name").trim()
                val lat = item.optString("lat").ifBlank { item.optString("latitude") }.trim()
                val lng = item.optString("lng").ifBlank { item.optString("lon") }.trim()
                if (name.isBlank() || lat.toDoubleOrNull() == null || lng.toDoubleOrNull() == null) continue
                add(
                    StationPlatform(
                        name = name,
                        lat = lat,
                        lng = lng,
                        distance = item.optDouble("dis", 0.0),
                        sameCount = item.optInt("sameNum", 0),
                    ),
                )
            }
        }.distinctBy { "${it.name}|${it.lat}|${it.lng}" }.sortedBy { it.distance }
    }

    fun loadLineDetail(city: CityConfig, lineName: String, direction: String): LineDetail {
        val json = postWithRetry(mapOf(
            "CMD" to "103", "CITYNAME" to city.cityName, "CITYKEY" to city.cityKey,
            "LINENAME" to lineName, "DIRECTION" to direction,
        ))
        check(json.optInt("status") == 1) { json.optString("msg", "线路详情加载失败") }
        return parseLineDetail(json, lineName, direction)
    }

    fun loadLineRealtime(
        city: CityConfig,
        lineName: String,
        direction: String,
        stationOrder: Int,
    ): LineRealtime {
        val json = post(
            mapOf(
                "CMD" to "104",
                "CITYNAME" to city.cityName,
                "CITYKEY" to city.cityKey,
                "LINENAME" to lineName,
                "DIRECTION" to direction,
                "STATIONORDER" to stationOrder.coerceAtLeast(0).toString(),
            ),
        )
        return parseLineRealtime(json)
    }

    internal fun parseLineRealtime(json: JSONObject): LineRealtime {
        if (json.optInt("status") != 1) return LineRealtime()
        val vehicles = buildList {
            val rows = json.optJSONArray("list") ?: return@buildList
            for (i in 0 until rows.length()) {
                val row = rows.optJSONObject(i) ?: continue
                val targetOrder = row.optInt("index", -1) + 1
                if (targetOrder <= 0) continue
                val arrived = row.optString("statusType") == "0"
                val markerOrder = if (arrived) targetOrder else targetOrder - 1
                if (markerOrder > 0) {
                    add(
                        LineVehicle(
                            stationOrder = markerOrder,
                            busNumber = row.optString("busNumber").trim(),
                            arrived = arrived,
                        ),
                    )
                }
            }
        }
        val eta = json.optJSONArray("routeOnStationRTimeInfoList")?.optJSONObject(0)
        val etaText = eta?.let {
            listOf(
                it.optString("busToStationTips").trim(),
                it.optString("busToStationTimeTips").trim().ifBlank {
                    it.optInt("busToStationTime", -1).takeIf { value -> value >= 0 }?.let { value -> "$value 分钟" }.orEmpty()
                },
            ).filter { value -> value.isNotBlank() }.joinToString(" · ")
        }.orEmpty()
        return LineRealtime(
            vehicles = vehicles,
            etaText = etaText,
            planTime = json.optString("planTime").trim(),
        )
    }

    internal fun parseLineDetail(json: JSONObject, lineName: String, direction: String): LineDetail {
        check(json.optInt("status") == 1) { json.optString("msg", "线路详情加载失败") }
        val stations = buildList {
            val data = json.optJSONArray("data") ?: return@buildList
            for (i in 0 until data.length()) {
                val item = data.optJSONObject(i) ?: continue
                val name = item.optString("showName").ifBlank { item.optString("stationName") }.trim()
                if (name.isNotEmpty()) add(LineStation(
                    order = item.optInt("stationOrder", i + 1),
                    name = name,
                    lat = item.optString("station_lat").ifBlank { item.optString("lat").ifBlank { item.optString("stationLat") } },
                    lng = item.optString("station_lon").ifBlank { item.optString("lon").ifBlank { item.optString("lng").ifBlank { item.optString("stationLng") } } },
                ))
            }
        }
        val firstLast = json.optJSONArray("firstLast")?.optJSONObject(0)
        return LineDetail(
            lineName = json.optString("routeName").ifBlank { lineName },
            direction = direction,
            from = stations.firstOrNull()?.name.orEmpty(),
            to = stations.lastOrNull()?.name.orEmpty(),
            firstTime = firstLast?.optString("first").orEmpty().ifBlank { json.optString("beginTime", "--") },
            lastTime = firstLast?.optString("last").orEmpty().ifBlank { json.optString("endTime", "--") },
            comment = json.optString("commonts").trim(),
            stations = stations,
        )
    }

    private fun post(params: Map<String, String>): JSONObject {
        val startedAt = System.nanoTime()
        val command = params["CMD"].orEmpty().ifBlank { "?" }
        var outcome = "error"
        val body = params.entries.joinToString("&") { (key, value) ->
            "${encode(key)}=${encode(value)}"
        }
        val connection = (URL(API_URL).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 6_000
            readTimeout = 8_000
            doOutput = true
            setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01")
            setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
            setRequestProperty("X-Requested-With", "XMLHttpRequest")
            setRequestProperty("Origin", "https://h5.mygolbs.com")
            setRequestProperty("Referer", "https://h5.mygolbs.com/")
        }
        return try {
            connection.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
            val code = connection.responseCode
            val stream = if (code in 200..299) connection.inputStream else connection.errorStream
            val text = stream.bufferedReader(Charsets.UTF_8).use { it.readText() }
            if (code !in 200..299) throw IOException("HTTP $code")
            JSONObject(text).also { outcome = "ok" }
        } finally {
            connection.disconnect()
            val durationMs = (System.nanoTime() - startedAt) / 1_000_000
            Log.i("BusPerf", "stage=api cmd=$command durationMs=$durationMs result=$outcome")
        }
    }

    private fun postWithRetry(params: Map<String, String>): JSONObject {
        val startedAt = System.nanoTime()
        return try {
            post(params)
        } catch (first: IOException) {
            val elapsedMs = (System.nanoTime() - startedAt) / 1_000_000
            if (elapsedMs >= RETRY_START_BUDGET_MS) throw first
            post(params)
        }
    }

    private fun encode(value: String): String = URLEncoder.encode(value, Charsets.UTF_8.name())
}
