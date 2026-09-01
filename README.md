# 腕上公交

面向 **小米 VelaOS 手表 / 手环** 与 **Android 手机** 的双端实时公交应用。

手表端提供附近站点、线路详情、实时到站与换乘方案；手机端负责定位中继、全国城市切换、搜索与收藏，并通过小米 Wear 互联把位置与配置同步到手表。

> 包名两端均为 `uno.keyin.bus`。手机 APK 与手表 RPK **必须使用同一套签名证书**，否则会出现 `fingerprint verify failed` 等互联失败。

## 下载

正式 release 安装包见 [`releases/`](releases/) 目录（当前最新：[`v1.3.0`](releases/v1.3.0/)），内含 APK、RPK、安装说明与校验文件。

## 功能概览

### 手表端（Vela 快应用）

- 附近公交站点与线路预览
- 线路详情、换向、实时到站
- 公交换乘方案查看
- 城市配置、定位请求与手机互联

### 手机端（Android）

- 全国已开通城市切换（默认泉州）
- 附近站点、线路/站点搜索、换乘查询
- 实时公交关注与手表同步
- 「定位中继」前台服务：锁屏后仍可响应手表索要 GPS
- 互联自检、穿戴权限申请、心跳与手表调试开关

## 仓库结构

```text
band-bus/
├── uno.keyin.bus/          # 手表 Vela 快应用（RPK）
├── android/                # 手机 Android 配套应用（APK）
├── interconnect_dev_test_demo/  # 互联签名对齐用 demo / keystore 参考
├── 腕上公交互联排障.md      # 真机互联常见错误与处理
└── 请求性能优化方案.md      # 定位缓存与请求优化说明
```

| 路径 | 说明 |
| --- | --- |
| `uno.keyin.bus/` | 快应用工程；`package` 见 `src/manifest.json` |
| `android/app/` | `applicationId` = `uno.keyin.bus` |
| `uno.keyin.bus/sign/` | 快应用 PEM（勿提交私钥到公开仓库时请自行保管） |
| `腕上公交互联排障.md` | `app not installed` / `fingerprint` / `permission denied` 等 |

## 环境要求

- **手表**：支持 Vela 快应用的小米手环 / REDMI Watch 等设备；需安装 [AIoT-IDE](https://iot.mi.com/vela/quickapp/zh/guide/start/use-ide.html) 进行调试或出包
- **手机**：Android 7.0+（`minSdk 24`），已安装「小米穿戴」并与手表绑定
- **开发机**：Node.js 16+（推荐）、JDK 17、Android SDK

## 快速开始

### 1. 克隆仓库

```bash
git clone https://github.com/way2-del/band-bus.git
cd band-bus
```

### 2. 编译手表 RPK

```bash
cd uno.keyin.bus
npm install
npm run build
```

产物：`uno.keyin.bus/dist/uno.keyin.bus.debug.*.BAND.rpk`  
使用 `sign/debug` 下证书签名（需与手机 debug APK 同源）。

也可用 AIoT-IDE 打开 `uno.keyin.bus` 目录，按 IDE 引导安装依赖并打包。

### 3. 编译手机 APK

```bash
cd android
./gradlew assembleDebug          # Linux / macOS
.\gradlew.bat assembleDebug      # Windows
```

产物：`android/app/build/outputs/apk/debug/app-debug.apk`

本仓库 debug 构建默认使用 `interconnect_dev_test_demo` 中的 keystore，并与 `uno.keyin.bus/sign/debug/certificate.pem` 做编译期一致性校验。签名细节见 [`uno.keyin.bus/sign/README.txt`](uno.keyin.bus/sign/README.txt)。

### 4. 安装与首次授权（推荐顺序）

1. 安装最新手机 APK  
2. 安装最新手表 RPK（覆盖失败时先卸载旧快应用）  
3. 确认「小米穿戴」中手表已连接  
4. 打开手机「腕上公交」→ **设置** → 确认 **互联自检** 通过  
5. **刷新已连接设备** → **申请穿戴权限**  
6. 授予定位权限（建议「始终允许」，便于锁屏中继）  
7. 保持定位中继通知常驻，再打开手表端应用  

两端请尽量使用**同一次构建**的 APK + RPK。

## 开发说明

### 签名与互联

手机与手表互联依赖：

1. **包名一致**：`uno.keyin.bus`  
2. **签名证书一致**：同一 keystore / PEM  
3. **穿戴权限**：`DEVICE_MANAGER`、`NOTIFY` 等（在设置中申请）

若通知栏出现 `fingerprint verify failed`、`app not installed`、`permission denied`，请按 [腕上公交互联排障.md](腕上公交互联排障.md) 处理。

### 常用命令

| 目的 | 命令 |
| --- | --- |
| 手表开发模式构建 | `cd uno.keyin.bus && npm run build` |
| 手表 release（含 JSC） | `cd uno.keyin.bus && npm run release` |
| 手表本地预览服务 | `cd uno.keyin.bus && npm start` |
| 手机 debug 安装到真机 | `cd android && ./gradlew installDebug` |

### 文档与参考

- [小米 Vela 快应用开发文档](https://iot.mi.com/vela/quickapp/zh/guide/)
- [Interconnect 互联特性说明](https://iot.mi.com/vela/quickapp/zh/features/network/interconnect.html)
- 本仓库：`腕上公交互联排障.md`、`请求性能优化方案.md`

## 参与贡献

欢迎 Issue 与 Pull Request。提交前建议：

1. 说明复现设备（手表型号、手机系统、小米穿戴版本）  
2. 若涉及互联，附上设置页「互联自检」结果与通知栏原文  
3. 尽量保持手机 / 手表改动可同批构建验证  

## 许可证

本项目采用 **GNU Affero General Public License v3.0（AGPL-3.0）**，完整文本见 [`LICENSE`](LICENSE)。

若你修改并对外提供基于本项目的网络服务或分发衍生作品，请遵守 AGPL 义务（包括提供对应源代码）。

## 免责声明

- 公交线路、到站与换乘数据来自第三方公开接口，准确性与可用性不由本项目保证。  
- 定位与后台中继会消耗电量；请按需授权「始终允许」位置权限，并留意系统省电策略。  
- 本项目与小米、公交数据提供方无官方隶属关系，仅供学习与个人使用参考。
