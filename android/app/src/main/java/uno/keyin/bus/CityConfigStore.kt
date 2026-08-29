package uno.keyin.bus

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class CityConfig(
    val cityName: String,
    val displayName: String,
    val cityKey: String,
    val version: Long,
)

object CityConfigStore {
    private const val KEY_CITY_NAME = "bus_city_name"
    private const val KEY_CITY_DISPLAY_NAME = "bus_city_display_name"
    private const val KEY_CITY_KEY = "bus_city_key"
    private const val KEY_CITY_VERSION = "bus_city_version"
    private const val KEY_CITY_LIST = "bus_city_list"
    private const val KEY_RECENT_CITIES = "bus_recent_cities"

    private val defaultCity = CityConfig("泉州市", "泉州", "qz595803", 0L)

    fun get(context: Context): CityConfig {
        val prefs = context.getSharedPreferences(RelayUiPrefs.PREFS_NAME, Context.MODE_PRIVATE)
        val cityName = prefs.getString(KEY_CITY_NAME, defaultCity.cityName).orEmpty()
            .ifBlank { defaultCity.cityName }
        return CityConfig(
            cityName = cityName,
            displayName = prefs.getString(KEY_CITY_DISPLAY_NAME, "").orEmpty()
                .ifBlank { displayName(cityName) },
            cityKey = prefs.getString(KEY_CITY_KEY, defaultCity.cityKey).orEmpty(),
            version = prefs.getLong(KEY_CITY_VERSION, defaultCity.version),
        )
    }

    fun save(context: Context, cityName: String): CityConfig {
        val normalized = cityName.trim()
        val previous = get(context)
        if (normalized == previous.cityName) return previous
        val next = CityConfig(
            cityName = normalized,
            displayName = displayName(normalized),
            cityKey = if (normalized == defaultCity.cityName) defaultCity.cityKey else "",
            version = maxOf(System.currentTimeMillis(), previous.version + 1),
        )
        context.getSharedPreferences(RelayUiPrefs.PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_CITY_NAME, next.cityName)
            .putString(KEY_CITY_DISPLAY_NAME, next.displayName)
            .putString(KEY_CITY_KEY, next.cityKey)
            .putLong(KEY_CITY_VERSION, next.version)
            .apply()
        addRecent(context, normalized)
        return next
    }

    fun toPayload(city: CityConfig): JSONObject = JSONObject().apply {
        put("type", "city_config")
        put("cityName", city.cityName)
        put("displayName", city.displayName)
        put("cityKey", city.cityKey)
        put("version", city.version)
    }

    fun cacheCityList(context: Context, cities: List<String>) {
        val array = JSONArray()
        cities.distinct().forEach(array::put)
        context.getSharedPreferences(RelayUiPrefs.PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_CITY_LIST, array.toString()).apply()
    }

    fun getCachedCityList(context: Context): List<String> = parseList(
        context.getSharedPreferences(RelayUiPrefs.PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_CITY_LIST, "").orEmpty(),
    )

    fun getRecent(context: Context): List<String> = parseList(
        context.getSharedPreferences(RelayUiPrefs.PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_RECENT_CITIES, "").orEmpty(),
    )

    private fun addRecent(context: Context, cityName: String) {
        val next = (listOf(cityName) + getRecent(context).filter { it != cityName }).take(5)
        val array = JSONArray()
        next.forEach(array::put)
        context.getSharedPreferences(RelayUiPrefs.PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_RECENT_CITIES, array.toString()).apply()
    }

    private fun parseList(raw: String): List<String> = runCatching {
        val array = JSONArray(raw)
        buildList {
            for (i in 0 until array.length()) {
                array.optString(i).trim().takeIf { it.isNotEmpty() }?.let(::add)
            }
        }
    }.getOrDefault(emptyList())

    fun displayName(cityName: String): String = cityName
        .removeSuffix("特别行政区")
        .removeSuffix("自治州")
        .removeSuffix("地区")
        .removeSuffix("市")
        .ifBlank { cityName }
}
