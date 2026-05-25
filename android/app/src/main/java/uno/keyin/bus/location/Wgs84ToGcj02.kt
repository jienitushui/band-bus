package uno.keyin.bus.location

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * WGS-84 → GCJ-02（国测局火星坐标），与国内地图/多数 LBS 接口一致。
 * 中国境外返回原坐标（不做偏移）。
 */
object Wgs84ToGcj02 {

    private const val A = 6378245.0
    private const val EE = 0.00669342162296594323

    private fun outOfChina(lat: Double, lng: Double): Boolean {
        if (lng < 72.004 || lng > 137.8347) return true
        if (lat < 0.8293 || lat > 55.8271) return true
        return false
    }

    private fun transformLat(lng: Double, lat: Double): Double {
        var ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * sqrt(abs(lng))
        ret += (20.0 * sin(6.0 * lng * PI) + 20.0 * sin(2.0 * lng * PI)) * 2.0 / 3.0
        ret += (20.0 * sin(lat * PI) + 40.0 * sin(lat / 3.0 * PI)) * 2.0 / 3.0
        ret += (160.0 * sin(lat / 12.0 * PI) + 320 * sin(lat * PI / 30.0)) * 2.0 / 3.0
        return ret
    }

    private fun transformLng(lng: Double, lat: Double): Double {
        var ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * sqrt(abs(lng))
        ret += (20.0 * sin(6.0 * lng * PI) + 20.0 * sin(2.0 * lng * PI)) * 2.0 / 3.0
        ret += (20.0 * sin(lng * PI) + 40.0 * sin(lng / 3.0 * PI)) * 2.0 / 3.0
        ret += (150.0 * sin(lng / 12.0 * PI) + 300.0 * sin(lng / 30.0 * PI)) * 2.0 / 3.0
        return ret
    }

    /**
     * @return Pair(纬度 GCJ-02, 经度 GCJ-02)
     */
    fun convert(wgsLat: Double, wgsLng: Double): Pair<Double, Double> {
        if (outOfChina(wgsLat, wgsLng)) return wgsLat to wgsLng
        var dLat = transformLat(wgsLng - 105.0, wgsLat - 35.0)
        var dLng = transformLng(wgsLng - 105.0, wgsLat - 35.0)
        val radLat = wgsLat / 180.0 * PI
        var magic = sin(radLat)
        magic = 1 - EE * magic * magic
        val sqrtMagic = sqrt(magic)
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI)
        dLng = (dLng * 180.0) / (A / sqrtMagic * cos(radLat) * PI)
        return (wgsLat + dLat) to (wgsLng + dLng)
    }
}
