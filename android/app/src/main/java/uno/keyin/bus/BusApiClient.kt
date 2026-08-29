package uno.keyin.bus

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object BusApiClient {
    private const val API_URL = "https://h5.mygolbs.com/ApiData.do"
    val executor: ExecutorService = Executors.newFixedThreadPool(4)

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
        for (i in 0 until minOf(rows.length(), 6)) {
            val item = rows.optJSONObject(i) ?: continue
            val name = item.optString("name").trim()
            if (name.isEmpty()) continue
            val lines = runCatching {
                loadStationLines(city, name, item.optString("lat"), item.optString("lon"))
            }.getOrDefault(emptyList())
            val distance = item.optDouble("dis", 0.0).toInt().coerceAtLeast(0)
            stations += StationUi(name, "约 ${distance}m", lines)
        }
        return stations
    }

    private fun loadStationLines(
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
            for (i in 0 until minOf(data.length(), 8)) {
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
                    ),
                )
            }
        }
    }

    private fun post(params: Map<String, String>): JSONObject {
        val body = params.entries.joinToString("&") { (key, value) ->
            "${encode(key)}=${encode(value)}"
        }
        val connection = (URL(API_URL).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 10_000
            readTimeout = 15_000
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
            check(code in 200..299) { "HTTP $code" }
            JSONObject(text)
        } finally {
            connection.disconnect()
        }
    }

    private fun encode(value: String): String = URLEncoder.encode(value, Charsets.UTF_8.name())
}
