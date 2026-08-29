package uno.keyin.bus

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class RealtimeWatchTarget(
    val cityName: String,
    val cityKey: String,
    val stationName: String,
    val stationLat: String,
    val stationLng: String,
    val lineName: String,
    val direction: String,
    val directionCode: String,
    val stationOrder: Int,
    val platformLabel: String = "",
    val reminderEnabled: Boolean = false,
    val addedAt: Long = System.currentTimeMillis(),
) {
    val stationKey: String get() = "$cityName|$stationName|$stationLat|$stationLng"
    val key: String get() = "$stationKey|$lineName|$directionCode"
}

object RealtimeWatchStore {
    const val MAX_TARGETS = 6
    private const val PREFS_NAME = "realtime_bus_watch"
    private const val KEY_TARGETS = "targets"

    fun getForCity(context: Context, city: CityConfig): List<RealtimeWatchTarget> =
        getAll(context).filter { it.cityName == city.cityName }.sortedBy { it.addedAt }

    fun isFollowing(context: Context, target: RealtimeWatchTarget): Boolean =
        getAll(context).any { it.key == target.key }

    fun add(context: Context, target: RealtimeWatchTarget): Boolean {
        val current = getAll(context)
        val merged = mergeTarget(current, target) ?: return false
        save(context, merged)
        return true
    }

    internal fun mergeTarget(
        current: List<RealtimeWatchTarget>,
        target: RealtimeWatchTarget,
    ): List<RealtimeWatchTarget>? {
        if (current.any { it.key == target.key }) return current
        val legacy = current.firstOrNull {
            it.platformLabel.isBlank() && it.cityName == target.cityName &&
                it.stationName == target.stationName && it.lineName == target.lineName &&
                it.directionCode == target.directionCode
        }
        if (legacy != null) return current.map { if (it.key == legacy.key) target else it }
        if (current.count { it.cityName == target.cityName } >= MAX_TARGETS) return null
        return current + target
    }

    fun remove(context: Context, target: RealtimeWatchTarget) {
        save(context, getAll(context).filterNot { it.key == target.key })
    }

    fun setReminder(context: Context, target: RealtimeWatchTarget, enabled: Boolean) {
        save(context, getAll(context).map { if (it.key == target.key) it.copy(reminderEnabled = enabled) else it })
    }

    fun toSyncPayload(context: Context): String {
        val targets = getAll(context)
        return JSONObject().apply {
            put("type", LocationRelayService.TYPE_REALTIME_WATCH_TARGETS)
            put("version", targets.maxOfOrNull { it.addedAt } ?: System.currentTimeMillis())
            put("targets", JSONArray(encodeTargets(targets)))
        }.toString()
    }

    private fun getAll(context: Context): List<RealtimeWatchTarget> = decodeTargets(
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TARGETS, "[]").orEmpty(),
    )

    internal fun decodeTargets(raw: String): List<RealtimeWatchTarget> = runCatching {
        val array = JSONArray(raw)
        buildList {
            for (index in 0 until array.length()) {
                val item = array.optJSONObject(index) ?: continue
                val stationName = item.optString("stationName").trim()
                val lineName = item.optString("lineName").trim()
                if (stationName.isBlank() || lineName.isBlank()) continue
                add(
                    RealtimeWatchTarget(
                        cityName = item.optString("cityName"),
                        cityKey = item.optString("cityKey"),
                        stationName = stationName,
                        stationLat = item.optString("stationLat"),
                        stationLng = item.optString("stationLng"),
                        lineName = lineName,
                        direction = item.optString("direction"),
                        directionCode = item.optString("directionCode", "1"),
                        stationOrder = item.optInt("stationOrder", 0),
                        platformLabel = item.optString("platformLabel"),
                        reminderEnabled = item.optBoolean("reminderEnabled", false),
                        addedAt = item.optLong("addedAt", 0L),
                    ),
                )
            }
        }
    }.getOrDefault(emptyList())

    private fun save(context: Context, targets: List<RealtimeWatchTarget>) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_TARGETS, encodeTargets(targets)).apply()
    }

    internal fun encodeTargets(targets: List<RealtimeWatchTarget>): String {
        val array = JSONArray()
        targets.forEach { target ->
            array.put(JSONObject().apply {
                put("cityName", target.cityName)
                put("cityKey", target.cityKey)
                put("stationName", target.stationName)
                put("stationLat", target.stationLat)
                put("stationLng", target.stationLng)
                put("lineName", target.lineName)
                put("direction", target.direction)
                put("directionCode", target.directionCode)
                put("stationOrder", target.stationOrder)
                put("platformLabel", target.platformLabel)
                put("reminderEnabled", target.reminderEnabled)
                put("addedAt", target.addedAt)
            })
        }
        return array.toString()
    }
}
