package uno.keyin.bus

import android.location.Location
import org.json.JSONObject
import uno.keyin.bus.location.Wgs84ToGcj02

object PhoneLocationPayload {

    const val TYPE_PHONE_LOCATION = "phone_location"
    const val SOURCE_PROACTIVE = "proactive"

    /** [source] 如 proactive：手表未点定位时由手机中继主动推送，便于重连后首包坐标 */
    fun toJson(location: Location, requestId: String? = null, source: String? = null): String {
        val (gcjLat, gcjLng) = Wgs84ToGcj02.convert(location.latitude, location.longitude)
        return JSONObject().apply {
            put("type", TYPE_PHONE_LOCATION)
            put("lat", gcjLat)
            put("lng", gcjLng)
            put("coordSys", "GCJ-02")
            if (!requestId.isNullOrBlank()) put("requestId", requestId)
            if (!source.isNullOrBlank()) put("source", source)
            if (location.hasAccuracy()) put("accuracy", location.accuracy.toDouble())
            if (location.hasAltitude()) put("altitude", location.altitude)
            if (location.hasBearing()) put("bearing", location.bearing.toDouble())
            if (location.hasSpeed()) put("speed", location.speed.toDouble())
            put("ts", System.currentTimeMillis())
            put("provider", location.provider ?: "")
        }.toString()
    }
}
