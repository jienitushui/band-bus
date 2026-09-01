package uno.keyin.bus.wear

import android.content.Context
import uno.keyin.bus.R
import java.util.Locale

/** 将小米 Wear SDK 英文错误码转成可操作的中文提示。 */
object WearSdkErrorMessages {

    fun friendly(context: Context, raw: String?): String {
        val msg = raw?.trim().orEmpty()
        if (msg.isEmpty()) return context.getString(R.string.wear_err_unknown)
        val lower = msg.lowercase(Locale.US)
        return when {
            lower.contains("fingerprint") ->
                context.getString(R.string.wear_err_fingerprint)
            lower.contains("app not installed") || lower.contains("app_not_installed") ->
                context.getString(R.string.wear_err_app_not_installed)
            lower.contains("permission denied") || lower.contains("permission_denied") ->
                context.getString(R.string.wear_err_permission_denied)
            lower.contains("not connected") || lower.contains("disconnect") ->
                context.getString(R.string.wear_err_not_connected)
            else -> msg
        }
    }

    fun isFingerprintMismatch(raw: String?): Boolean {
        val lower = raw?.lowercase(Locale.US).orEmpty()
        return lower.contains("fingerprint")
    }
}
