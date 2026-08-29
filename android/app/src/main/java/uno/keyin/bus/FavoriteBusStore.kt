package uno.keyin.bus

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object FavoriteBusStore {
    private const val PREFS = "bus_favorites"
    private const val KEY = "items"

    fun get(context: Context, city: CityConfig): List<RealtimeWatchTarget> = decode(
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, "[]").orEmpty(),
    ).filter { it.cityName == city.cityName }

    fun isFavorite(context: Context, target: RealtimeWatchTarget): Boolean = decode(
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, "[]").orEmpty(),
    ).any { it.key == target.key }

    fun toggle(context: Context, target: RealtimeWatchTarget): Boolean {
        val all = decode(context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, "[]").orEmpty())
        val exists = all.any { it.key == target.key }
        val next = if (exists) all.filterNot { it.key == target.key } else all + target
        save(context, next)
        return !exists
    }

    fun remove(context: Context, target: RealtimeWatchTarget) {
        val all = decode(context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, "[]").orEmpty())
        save(context, all.filterNot { it.key == target.key })
    }

    private fun save(context: Context, targets: List<RealtimeWatchTarget>) {
        val array = JSONArray()
        targets.forEach { item -> array.put(JSONObject().apply {
            put("cityName", item.cityName); put("cityKey", item.cityKey)
            put("stationName", item.stationName); put("stationLat", item.stationLat); put("stationLng", item.stationLng)
            put("lineName", item.lineName); put("direction", item.direction); put("directionCode", item.directionCode)
            put("stationOrder", item.stationOrder); put("platformLabel", item.platformLabel)
            put("reminderEnabled", item.reminderEnabled); put("addedAt", item.addedAt)
        }) }
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY, array.toString()).apply()
    }

    private fun decode(raw: String): List<RealtimeWatchTarget> = runCatching {
        val array = JSONArray(raw)
        buildList {
            for (i in 0 until array.length()) {
                val item = array.optJSONObject(i) ?: continue
                if (item.optString("cityName").isBlank() || item.optString("lineName").isBlank()) continue
                add(RealtimeWatchTarget(
                    cityName = item.optString("cityName"), cityKey = item.optString("cityKey"),
                    stationName = item.optString("stationName"), stationLat = item.optString("stationLat"), stationLng = item.optString("stationLng"),
                    lineName = item.optString("lineName"), direction = item.optString("direction"), directionCode = item.optString("directionCode", "1"),
                    stationOrder = item.optInt("stationOrder"), platformLabel = item.optString("platformLabel"),
                    reminderEnabled = item.optBoolean("reminderEnabled", false), addedAt = item.optLong("addedAt"),
                ))
            }
        }
    }.getOrDefault(emptyList())
}
