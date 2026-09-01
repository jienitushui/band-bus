package uno.keyin.bus.location

import android.annotation.SuppressLint
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.os.Handler
import android.os.Looper
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

/**
 * 单次定位：中继手表侧建议 [highAccuracy]=false（平衡功耗与速度）；手动发坐标可用高精度。
 * 任意非空结果会写入 [PhoneLocationCache]。
 */
object PhoneLocationHelper {

    /** 高精度：Fused last 在 3 分钟内可直接返回 */
    private const val MAX_LAST_KNOWN_AGE_HIGH_MS = 3 * 60 * 1000L

    /** 平衡模式：稍旧的 last 也先用，减少 getCurrentLocation 等待 */
    private const val MAX_LAST_KNOWN_AGE_BALANCED_MS = 5 * 60 * 1000L

    /**
     * 单次 [getCurrentLocation] 在弱信号/室内可能长时间不返回；超时后取消并回退 last/fallback，
     * 避免界面长时间停在「正在获取」。
     */
    private const val FUSED_HIGH_ACCURACY_TIMEOUT_MS = 8_000L
    private const val FUSED_BALANCED_TIMEOUT_MS = 4_000L

    fun hasLocationPermission(context: Context): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    /** Android 10+ 后台定位；HyperOS/Android 14 下中继在锁屏后仍建议为「始终允许」 */
    fun hasBackgroundLocationPermission(context: Context): Boolean {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) return true
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * @param allowLastKnown 为 false 时跳过 Fused lastLocation 快路径，强制走
     * [getCurrentLocation]（超时后再回退 last）。用于「已有缓存秒开、后台校正」场景，
     * 避免把缓存里同一条 last 再当“新定位”用，导致附近站点看起来刷新了却不变。
     */
    @SuppressLint("MissingPermission")
    fun fetchBestLocation(
        context: Context,
        highAccuracy: Boolean = true,
        allowLastKnown: Boolean = true,
        onResult: (Location?) -> Unit,
    ) {
        val appCtx = context.applicationContext
        val wrapped: (Location?) -> Unit = { loc ->
            if (loc != null) PhoneLocationCache.put(appCtx, loc)
            onResult(loc)
        }
        if (!hasLocationPermission(appCtx)) {
            wrapped(null)
            return
        }

        val fused = runCatching { LocationServices.getFusedLocationProviderClient(context) }.getOrNull()
        if (fused != null) {
            if (!allowLastKnown) {
                requestFusedCurrentThenFallback(fused, appCtx, highAccuracy, wrapped)
                return
            }
            val maxLastAge = if (highAccuracy) MAX_LAST_KNOWN_AGE_HIGH_MS else MAX_LAST_KNOWN_AGE_BALANCED_MS
            fused.lastLocation.addOnSuccessListener { last ->
                if (last != null && System.currentTimeMillis() - last.time <= maxLastAge) {
                    wrapped(last)
                } else {
                    requestFusedCurrentThenFallback(fused, appCtx, highAccuracy, wrapped)
                }
            }.addOnFailureListener {
                requestFusedCurrentThenFallback(fused, appCtx, highAccuracy, wrapped)
            }
        } else {
            wrapped(fallbackLastKnown(appCtx))
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestFusedCurrentThenFallback(
        fused: FusedLocationProviderClient,
        appCtx: Context,
        highAccuracy: Boolean,
        onResult: (Location?) -> Unit,
    ) {
        val cts = CancellationTokenSource()
        val handler = Handler(Looper.getMainLooper())
        val lock = Any()
        var done = false
        val timeoutRunnable = Runnable {
            cts.cancel()
            fused.lastLocation.addOnSuccessListener { last ->
                synchronized(lock) {
                    if (done) return@addOnSuccessListener
                    done = true
                }
                handler.removeCallbacksAndMessages(null)
                onResult(last ?: fallbackLastKnown(appCtx))
            }.addOnFailureListener {
                synchronized(lock) {
                    if (done) return@addOnFailureListener
                    done = true
                }
                handler.removeCallbacksAndMessages(null)
                onResult(fallbackLastKnown(appCtx))
            }
        }
        fun finish(loc: Location?) {
            synchronized(lock) {
                if (done) return
                done = true
            }
            handler.removeCallbacks(timeoutRunnable)
            onResult(loc)
        }
        val timeoutMs = if (highAccuracy) FUSED_HIGH_ACCURACY_TIMEOUT_MS else FUSED_BALANCED_TIMEOUT_MS
        handler.postDelayed(timeoutRunnable, timeoutMs)
        val priority = if (highAccuracy) {
            Priority.PRIORITY_HIGH_ACCURACY
        } else {
            Priority.PRIORITY_BALANCED_POWER_ACCURACY
        }
        fused.getCurrentLocation(priority, cts.token)
            .addOnSuccessListener { loc ->
                if (loc != null) {
                    finish(loc)
                } else {
                    fused.lastLocation.addOnSuccessListener { last ->
                        finish(last ?: fallbackLastKnown(appCtx))
                    }.addOnFailureListener {
                        finish(fallbackLastKnown(appCtx))
                    }
                }
            }
            .addOnFailureListener {
                fused.lastLocation.addOnSuccessListener { last ->
                    finish(last ?: fallbackLastKnown(appCtx))
                }.addOnFailureListener {
                    finish(fallbackLastKnown(appCtx))
                }
            }
    }

    private fun fallbackLastKnown(context: Context): Location? {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            ?: return null
        if (!hasLocationPermission(context)) return null
        return try {
            val gps = runCatching { lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) }.getOrNull()
            val net = runCatching { lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) }.getOrNull()
            listOfNotNull(gps, net).maxByOrNull { it.time }
        } catch (_: SecurityException) {
            null
        }
    }
}
