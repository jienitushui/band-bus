package uno.keyin.bus

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import uno.keyin.bus.wear.XmsWearSdkBridge

/**
 * 开机后自动拉起定位中继前台服务，避免必须手动打开一次 App 才能接手表消息。
 */
class BootRelayReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) return
        if (!XmsWearSdkBridge.isSdkOnClasspath()) return
        val app = context.applicationContext
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val ok = ContextCompat.checkSelfPermission(app, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            if (!ok) return
        }
        runCatching { LocationRelayService.start(app) }
    }
}
