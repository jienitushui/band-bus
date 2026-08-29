import java.security.KeyStore
import java.security.MessageDigest
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// debug 真机调试：把快应用 sign/debug 下的「公钥证书」打进 APK assets（不含 private.pem）
val interconnectCertDebug = rootProject.projectDir.resolve("../uno.keyin.bus/sign/debug/certificate.pem").normalize()
val interconnectDebugAssetsDir = layout.buildDirectory.dir("generated/interconnect-debug-assets")
val interconnectKeystore = rootProject.projectDir.resolve(
    "../interconnect_dev_test_demo/XMS Wearable Demo/xms-wearable-sdk/keystore/keystore.jks"
).normalize()
val interconnectStorePassword = "xmswearable"
val interconnectKeyAlias = "xmswearable"

android {
    namespace = "uno.keyin.bus"
    compileSdk = 34

    defaultConfig {
        applicationId = "uno.keyin.bus"
        minSdk = 24
        targetSdk = 34
        versionCode = 4
        versionName = "1.3"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // 与 interconnect_dev_test_demo 共用同一套 keystore，便于与已从 demo 同步的 sign/*.pem 做 interconnect 调试
    signingConfigs {
        create("interconnectDemo") {
            storeFile = file(interconnectKeystore)
            storePassword = interconnectStorePassword
            keyAlias = interconnectKeyAlias
            keyPassword = interconnectStorePassword
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("interconnectDemo")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("interconnectDemo")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }

    sourceSets.getByName("debug") {
        assets.srcDir(interconnectDebugAssetsDir)
    }
}

val verifyDebugInterconnectSigning = tasks.register("verifyDebugInterconnectSigning") {
    group = "verification"
    description = "Fail unless the debug APK keystore and watch quick-app certificate use the same key"
    inputs.files(interconnectCertDebug, interconnectKeystore)

    doLast {
        if (!interconnectCertDebug.isFile) {
            throw GradleException(
                "Missing ${interconnectCertDebug.path}. Export certificate.pem and private.pem " +
                    "from the Android debug keystore before building the paired APK/RPK."
            )
        }
        if (!interconnectKeystore.isFile) {
            throw GradleException("Missing Android signing keystore: ${interconnectKeystore.path}")
        }

        val certificateFactory = CertificateFactory.getInstance("X.509")
        val watchCertificate = interconnectCertDebug.inputStream().use {
            certificateFactory.generateCertificate(it) as X509Certificate
        }
        val keyStore = KeyStore.getInstance(
            interconnectKeystore,
            interconnectStorePassword.toCharArray(),
        )
        val androidCertificate = keyStore.getCertificate(interconnectKeyAlias)
            ?: throw GradleException(
                "Alias '$interconnectKeyAlias' was not found in ${interconnectKeystore.path}"
            )

        if (!MessageDigest.isEqual(
                androidCertificate.publicKey.encoded,
                watchCertificate.publicKey.encoded,
            )
        ) {
            throw GradleException(
                "Interconnect signing mismatch: Android keystore and " +
                    "uno.keyin.bus/sign/debug/certificate.pem use different public keys."
            )
        }
    }
}

val copyDebugInterconnectCertificate = tasks.register<org.gradle.api.tasks.Copy>("copyDebugInterconnectCertificate") {
    description = "Pack public certificate.pem into debug APK assets/interconnect/ (never private.pem)"
    dependsOn(verifyDebugInterconnectSigning)
    from(interconnectCertDebug)
    into(interconnectDebugAssetsDir.map { it.dir("interconnect").asFile })
}

// mergeDebugAssets 在 Android 插件后注册，需延后挂接
afterEvaluate {
    tasks.named("mergeDebugAssets").configure {
        dependsOn(copyDebugInterconnectCertificate)
    }
    tasks.matching { task -> task.name.contains("lint", ignoreCase = true) }.configureEach {
        dependsOn(copyDebugInterconnectCertificate)
    }
}

dependencies {
    // 与 demo 相同：本地 aar/jar；可放在 app/libs 或 demo 工程 app/libs（官方 zip 常不含 aar，需从开放文档附件获取）
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    val demoWearLibs = rootProject.projectDir.resolve(
        "../interconnect_dev_test_demo/XMS Wearable Demo/xms-wearable-sdk/app/libs"
    ).normalize()
    if (demoWearLibs.isDirectory) {
        implementation(fileTree(mapOf("dir" to demoWearLibs, "include" to listOf("*.jar", "*.aar"))))
    }
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.json:json:20240303")
}
