debug 专用 assets（随 debug APK 一并打包）

- certificate.pem：由 Gradle 在编译 debug 时从仓库内快应用目录复制（若存在），便于核对与手表签名是否同源。
- 私钥 private.pem 绝不会被打进 APK；请勿手动放入 assets。

安装包内签名证书亦见 META-INF/*.RSA / *.MF（与 interconnect 使用的签名一致）。
