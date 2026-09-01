# 腕上公交 v1.3.0 安装说明

请使用**本目录同一批** APK + RPK，不要混用旧 debug 包或其他签名版本。

## 1. 安装手机端

1. 将 `band-bus-1.3-phone-release.apk` 传到手机并安装  
2. 若提示冲突，先卸载旧版「腕上公交」再装  
3. 打开应用，授予定位权限（建议后续到系统设置选「始终允许」）

## 2. 安装手表端

1. 通过 AIoT-IDE / 小米穿戴支持的渠道安装  
   `band-bus-V26.2.7.BAND-watch-release.rpk`  
2. 覆盖安装失败时：先在手表或小米穿戴中卸载旧「腕上公交」快应用，再安装本 RPK  

## 3. 首次互联（推荐顺序）

1. 打开「小米穿戴」，确认手表蓝牙已连接  
2. 打开手机「腕上公交」→ 右上角 **设置**  
3. 确认 **互联自检** 通过（包名与签名一致）  
4. 点击 **刷新已连接设备**，确认出现节点 ID  
5. 点击 **申请穿戴权限**，在小米穿戴弹窗中全部允许  
6. 保持通知栏「定位中继」常驻  
7. 打开手表端「腕上公交」，确认能拉取附近站点 / 收到定位  

## 4. 常见失败

| 现象 | 处理 |
| --- | --- |
| `fingerprint verify failed` | 两端卸载后重装本目录 APK+RPK |
| `app not installed` | 手表未装对应 RPK，或包名/签名不一致 |
| `permission denied` | 在设置中重新申请穿戴权限 |
| 锁屏后不回定位 | 授予「始终允许」位置，并保留中继通知 |

更完整说明见仓库文档：`腕上公交互联排障.md`。

## 5. 校验文件（可选）

```bash
# 在 releases/v1.3.0 目录下
sha256sum -c SHA256SUMS.txt
```

Windows PowerShell：

```powershell
Get-FileHash .\band-bus-1.3-phone-release.apk,.\band-bus-V26.2.7.BAND-watch-release.rpk -Algorithm SHA256
```

对照 `SHA256SUMS.txt` 中的哈希值即可。
