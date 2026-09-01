# 发布包目录

预编译安装包按版本放在子目录中，例如 [`v1.3.0/`](v1.3.0/)。

每个版本目录通常包含：

- `*-phone-release.apk` — 手机端
- `*-watch-release.rpk` — 手表端
- `RELEASE_NOTES.md` — 版本说明
- `INSTALL.md` — 安装与授权步骤
- `SHA256SUMS.txt` — 校验和

安装前请阅读对应版本的 `INSTALL.md`，并保证 APK 与 RPK 来自同一版本目录。
