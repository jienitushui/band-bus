package uno.keyin.bus

import android.content.Context
import org.json.JSONArray

object SearchHistoryStore {
    private const val PREFS_NAME = "bus_search_history"
    private const val MAX_ITEMS = 8

    fun get(context: Context, city: CityConfig): List<String> = runCatching {
        val raw = prefs(context).getString(key(city), "").orEmpty()
        if (raw.isBlank()) return emptyList()
        val array = JSONArray(raw)
        buildList {
            for (i in 0 until array.length()) {
                array.optString(i).trim().takeIf { it.isNotEmpty() }?.let(::add)
            }
        }
    }.getOrDefault(emptyList())

    fun add(context: Context, city: CityConfig, keyword: String) {
        val value = keyword.trim()
        if (value.isEmpty()) return
        val next = (listOf(value) + get(context, city).filterNot { it.equals(value, true) }).take(MAX_ITEMS)
        save(context, city, next)
    }

    fun remove(context: Context, city: CityConfig, keyword: String) {
        save(context, city, get(context, city).filterNot { it == keyword })
    }

    fun clear(context: Context, city: CityConfig) {
        prefs(context).edit().remove(key(city)).apply()
    }

    private fun save(context: Context, city: CityConfig, values: List<String>) {
        prefs(context).edit().putString(key(city), JSONArray(values).toString()).apply()
    }

    private fun prefs(context: Context) = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private fun key(city: CityConfig) = "${city.cityName}|${city.cityKey}"
}
