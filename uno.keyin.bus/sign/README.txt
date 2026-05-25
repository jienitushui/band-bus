interconnect 与 Android 配套应用签名对齐说明（与官方文档一致）
================================================================

官方文档：https://iot.mi.com/vela/quickapp/zh/features/network/interconnect.html

前提（文档「开发注意事项」）：
1. 快应用 src/manifest.json 的 package 与 Android applicationId 一致（当前均为 uno.keyin.bus）。
2. 快应用打包所用证书与 Android APK 签名证书为同一套（从同一 .jks 或同一 debug.keystore 导出）。

本目录约定：
- sign/debug/private.pem   + sign/debug/certificate.pem   → 对应 debug 快应用与 debug APK 同一密钥。
- sign/release/private.pem + sign/release/certificate.pem → 对应 release 快应用与 release APK 同一密钥。

当前工程已与仓库内 interconnect_dev_test_demo 对齐：
- PEM 来自 interconnect-demo/sign 下同名文件（与小米 demo 快应用一致）。
- Android 模块 android/app 使用 interconnect_dev_test_demo/XMS Wearable Demo/xms-wearable-sdk/keystore/keystore.jks
  （别名与口令与 demo 的 keystore.properties 一致：xmswearable）。
  因此快应用与配套 APK 证书一致，包名仍为 manifest 中的 uno.keyin.bus（与证书无绑定关系）。
若移动或删除 interconnect_dev_test_demo 目录，需改 android/app/build.gradle.kts 中路径或改为自己密钥并重新导出 PEM。

Android 侧小米穿戴 SDK（aar）：
- 请将官方文档提供的 aar 放入 android/app/libs/（见该目录 README.txt）。
- 手机 APK 与手表快应用 interconnect 时，仍需满足上文「包名一致 + 签名一致」。

Android Studio 绿色 Run（debug）：
- 工程会把本目录 debug 下的 certificate.pem 打进 APK assets，并在「腕上公交配套」启动时自动校验：
  包名是否为 uno.keyin.bus、APK 签名公钥是否与该 certificate.pem 一致。
  顶部绿色条为通过；红色条请对照日志修正签名或包名后再调试。

从 Android 使用的 .jks 生成 PEM（命令行，在 PC 上执行）：
1) jks → p12
   keytool -importkeystore -srckeystore 你的.jks -destkeystore keystore.p12 -srcstoretype jks -deststoretype pkcs12

2) p12 → pem（含私钥与证书链）
   openssl pkcs12 -nodes -in keystore.p12 -out keystore.pem

3) 编辑 keystore.pem：
   - 从 -----BEGIN PRIVATE KEY----- 到 -----END PRIVATE KEY----- 整段复制到 private.pem
     （若为 -----BEGIN RSA PRIVATE KEY-----，按文档要求通常需与工具链一致的 PKCS#8；可用 openssl pkcs8 转换）
   - 从 -----BEGIN CERTIFICATE----- 到 -----END CERTIFICATE----- 复制到 certificate.pem
     （有多段证书时，一般第一段为签名证书）

Debug 互联测试：Android Studio 默认 debug 安装使用用户目录下 .android/debug.keystore。
可用同一套 keytool/openssl 流程从 debug.keystore 导出 PEM，放入 sign/debug/，再与 debug APK 一起测试。

在线工具（不上传私钥到服务器）：文档中的「在线签名生成工具」可从 p12 生成 pem。

真机替换包名时建议先卸载旧包再装新包，避免图标/缓存残留（文档说明）。
