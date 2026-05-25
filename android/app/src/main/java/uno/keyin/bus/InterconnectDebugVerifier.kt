package uno.keyin.bus

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import java.io.ByteArrayInputStream
import java.security.MessageDigest
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.Locale

/**
 * 绿色 Run/Debug 安装后一键自检：包名必须为 uno.keyin.bus，且 APK 签名公钥须与
 * assets/interconnect/certificate.pem（与手表快应用 sign/debug 同源）一致。
 */
object InterconnectDebugVerifier {

    const val EXPECTED_PACKAGE = "uno.keyin.bus"
    private const val ASSET_CERT = "interconnect/certificate.pem"

    data class Result(
        val packageOk: Boolean,
        val certAssetPresent: Boolean,
        val signingMatchesAssetCert: Boolean,
        val detailLine: String,
        val sha256ApkCert: String?,
        val sha256AssetCert: String?,
    ) {
        val allOkForInterconnect: Boolean
            get() = packageOk && certAssetPresent && signingMatchesAssetCert
    }

    fun verify(context: Context): Result {
        val pkg = context.packageName
        val packageOk = pkg == EXPECTED_PACKAGE

        val apkCert = loadApkSigningCertificate(context) ?: return Result(
            packageOk = packageOk,
            certAssetPresent = false,
            signingMatchesAssetCert = false,
            detailLine = context.getString(R.string.verify_no_apk_cert),
            sha256ApkCert = null,
            sha256AssetCert = null,
        )

        val shaApk = sha256Hex(apkCert.encoded)

        val assetCert = try {
            context.assets.open(ASSET_CERT).use { loadX509FromPemOrDer(it.readBytes()) }
        } catch (_: Throwable) {
            null
        }

        if (assetCert == null) {
            return Result(
                packageOk = packageOk,
                certAssetPresent = false,
                signingMatchesAssetCert = false,
                detailLine = context.getString(R.string.verify_no_asset_cert),
                sha256ApkCert = shaApk,
                sha256AssetCert = null,
            )
        }

        val shaAsset = sha256Hex(assetCert.encoded)
        val match = MessageDigest.isEqual(apkCert.publicKey.encoded, assetCert.publicKey.encoded) ||
            MessageDigest.isEqual(apkCert.encoded, assetCert.encoded)

        val detail = when {
            !packageOk -> context.getString(R.string.verify_pkg_wrong, pkg)
            !match -> context.getString(R.string.verify_cert_mismatch)
            else -> context.getString(R.string.verify_all_ok)
        }

        return Result(
            packageOk = packageOk,
            certAssetPresent = true,
            signingMatchesAssetCert = match,
            detailLine = detail,
            sha256ApkCert = shaApk,
            sha256AssetCert = shaAsset,
        )
    }

    private fun loadApkSigningCertificate(context: Context): X509Certificate? {
        return try {
            val pm = context.packageManager
            val pkg = context.packageName
            val cf = CertificateFactory.getInstance("X.509")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val pi = pm.getPackageInfo(pkg, PackageManager.GET_SIGNING_CERTIFICATES)
                val si = pi.signingInfo ?: return null
                val sigs = si.apkContentsSigners
                if (sigs.isEmpty()) return null
                cf.generateCertificate(ByteArrayInputStream(sigs[0].toByteArray())) as X509Certificate
            } else {
                @Suppress("DEPRECATION")
                val pi = pm.getPackageInfo(pkg, PackageManager.GET_SIGNATURES)
                val sigs = pi.signatures ?: return null
                if (sigs.isEmpty()) return null
                cf.generateCertificate(ByteArrayInputStream(sigs[0].toByteArray())) as X509Certificate
            }
        } catch (_: Throwable) {
            null
        }
    }

    private fun loadX509FromPemOrDer(bytes: ByteArray): X509Certificate {
        val text = bytes.toString(Charsets.UTF_8).trim()
        val der = if (text.contains("BEGIN CERTIFICATE")) {
            val b64 = text
                .replace("-----BEGIN CERTIFICATE-----", "")
                .replace("-----END CERTIFICATE-----", "")
                .replace(Regex("\\s"), "")
            Base64.decode(b64, Base64.DEFAULT)
        } else {
            bytes
        }
        return CertificateFactory.getInstance("X.509")
            .generateCertificate(ByteArrayInputStream(der)) as X509Certificate
    }

    private fun sha256Hex(encoded: ByteArray): String {
        val md = MessageDigest.getInstance("SHA-256")
        val dig = md.digest(encoded)
        return dig.joinToString(":") { b -> "%02X".format(Locale.US, b) }
    }
}
