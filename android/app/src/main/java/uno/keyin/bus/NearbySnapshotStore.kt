package uno.keyin.bus

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object NearbySnapshotStore {
    private const val PREFS_NAME = "bus_nearby_snapshot"
    private const val KEY_SNAPSHOT = "snapshot"
    private const val MAX_AGE_MS = 30 * 60 * 1000L

    fun load(context: Context, city: CityConfig): List<StationUi>? = runCatching {
        val raw = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_SNAPSHOT, "").orEmpty()
        if (raw.isBlank()) return null
        val root = JSONObject(raw)
        val savedAt = root.optLong("savedAt", 0L)
        if (savedAt <= 0L || System.currentTimeMillis() - savedAt > MAX_AGE_MS) return null
        if (root.optString("cityName") != city.cityName || root.optString("cityKey") != city.cityKey) return null
        val rows = root.optJSONArray("stations") ?: return emptyList()
        buildList {
            for (index in 0 until rows.length()) {
                val item = rows.optJSONObject(index) ?: continue
                val buses = parseLines(item.optJSONArray("buses"))
                add(
                    StationUi(
                        name = item.optString("name"),
                        desc = item.optString("desc"),
                        lat = item.optString("lat"),
                        lng = item.optString("lng"),
                        buses = buses,
                        linesState = if (buses.isEmpty()) StationLinesState.NOT_LOADED else StationLinesState.LOADED,
                    ),
                )
            }
        }
    }.getOrNull()

    fun save(context: Context, city: CityConfig, stations: List<StationUi>) {
        val rows = JSONArray()
        stations.forEach { station ->
            rows.put(JSONObject().apply {
                put("name", station.name)
                put("desc", station.desc)
                put("lat", station.lat)
                put("lng", station.lng)
                put("buses", JSONArray().apply {
                    station.buses.forEach { line ->
                        put(JSONObject().apply {
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
                })
            })
        }
        val root = JSONObject().apply {
            put("savedAt", System.currentTimeMillis())
            put("cityName", city.cityName)
            put("cityKey", city.cityKey)
            put("stations", rows)
        }
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_SNAPSHOT, root.toString()).apply()
    }

    private fun parseLines(rows: JSONArray?): List<BusLineUi> = buildList {
        if (rows == null) return@buildList
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
    }
}
