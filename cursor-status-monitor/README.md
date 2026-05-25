# Cursor Status Monitor

通过 Cursor Hooks 将 Agent 工作状态推送到本地 HTTP 服务，供手机、手环等设备轮询。

## 架构

```
Cursor Hooks (状态变化) → 本地 HTTP 服务 (缓存) → 外部设备 (轮询 GET /status)
```

## 状态说明

| 状态 | 含义 | 触发 Hook |
|------|------|-----------|
| `idle` | **默认**：空闲，服务启动后或未在对话中 | 服务初始值；`sessionEnd` |
| `start` | 用户回车后，AI 开始思考 / 执行工具 | `beforeSubmitPrompt`, `preToolUse` |
| `pending` | 等待用户同意（终端命令、MCP 等） | `beforeShellExecution`, `beforeMCPExecution` |
| `end` | 本轮 Agent 回复与修改已完成 | `stop` |

状态流转示意：

```
idle ──(回车提交)──► start ──(需审批)──► pending ──(允许后继续)──► start ──(完成)──► end
  ▲                                                                              │
  └──────────────────────────── sessionEnd / 新一轮对话前 ───────────────────────┘
```

> 说明：Cursor 官方 **不支持** `PermissionRequest`。`beforeShellExecution` / `beforeMCPExecution` 用于覆盖「等待点击 Allow」的 `pending` 场景。

若已安装过全局 Hooks，请重新执行 `install-user-hooks.ps1` 并重启 Cursor。

## 快速开始

### 1. 启动状态服务器

**方式 A：图形界面（推荐 Windows）**

```powershell
# 开发运行
.\scripts\run-gui.ps1

# 或打包成 exe 后双击
.\scripts\build-exe.ps1
# 输出: dist\CursorStatusMonitor.exe
```

**方式 B：命令行**

```powershell
.\scripts\start-server.ps1
# 或
python server/cursor_status_server.py
```

服务默认监听 `0.0.0.0:3000`，可通过环境变量 `CURSOR_STATUS_PORT` 修改端口。

### 2. 安装 Hooks（全局，适用于所有项目）

**方式 A：在 GUI / exe 里一键安装（推荐）**

点击 **「一键安装 Hooks」**，会自动：

- 检测 Cursor 安装路径（`%LOCALAPPDATA%\Programs\cursor\...`）
- 写入 `%USERPROFILE%\.cursor\hooks.json` 与 `hooks\update_status.py`
- 自动检测本机 `python.exe` 路径写入配置

**方式 B：命令行**

```powershell
.\scripts\install-user-hooks.ps1
```

安装完成后 **完全退出并重启 Cursor**。

### 3. 验证

```powershell
# 健康检查
curl http://127.0.0.1:3000/health

# 手动模拟状态变更
curl -X POST http://127.0.0.1:3000/update -H "Content-Type: application/json" -d "{\"status\":\"start\"}"

# 查询当前状态
curl http://127.0.0.1:3000/status
```

在 Cursor 中提交一条 Agent 请求，观察终端是否输出 `[状态更新]` 日志。

### 4. 外部设备轮询

1. 获取电脑局域网 IP：`ipconfig`（Windows）或 `ifconfig`（macOS/Linux）
2. 设备每 1~2 秒请求：

```
GET http://<局域网IP>:3000/status
```

响应示例：

```json
{ "status": "idle" }
```

UI 映射参考：

- `idle` → 灰色，默认空闲（可放松）
- `start` → 绿色，AI 正在工作
- `pending` → 橙色，等待你点击 Allow / 确认
- `end` → 蓝色或浅灰，本轮已完成

## 目录结构

```
cursor-status-monitor/
├── .cursor/
│   ├── hooks.json              # 项目级 Hooks（仅在本目录打开时生效）
│   └── hooks/
│       └── update_status.py    # Hook 推送脚本
├── server/
│   └── cursor_status_server.py # 本地状态中转服务
├── scripts/
│   ├── start-server.ps1        # 启动服务 (Windows)
│   ├── start-server.sh         # 启动服务 (Unix)
│   ├── install-user-hooks.ps1  # 安装全局 Hooks (Windows)
│   └── install-user-hooks.sh   # 安装全局 Hooks (Unix)
└── templates/
    └── user-hooks.json         # 全局 Hooks 模板
```

## 环境变量

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `CURSOR_STATUS_PORT` | `3000` | 服务器监听端口 |
| `CURSOR_STATUS_UPDATE_URL` | `http://127.0.0.1:3000/update` | Hook 脚本推送地址 |
| `CURSOR_STATUS_TOKEN` | （空） | 可选 API Token，请求头 `X-Api-Token` |
| `CURSOR_STATUS_QUIET` | `1` | `1` 不打印每条 GET 日志，只显示 `[状态更新]` |
| `CURSOR_STATUS_VERBOSE` | `0` | `1` 时显示被过滤的 TLS 探测错误 |

## 日志里出现乱码？

**不是 Python 坏了**，而是有客户端用 **`https://`** 去访问只支持 **`http://`** 的 3000 端口。

浏览器/手环/App 会先发 TLS 握手（`\x13\x01...`、`À+À/` 等），本服务按 HTTP 解析就会报 `Bad request version` 并打印乱码。

**解决办法：**

1. 轮询地址改成 **`http://192.168.x.x:3000/status`**（不要写 `https://`）
2. 若必须用 HTTPS，在服务器上用 **Nginx/Caddy** 做反向代理并终止 TLS，后端仍用 HTTP
3. 重启服务后默认已静默这类错误；正常轮询只会看到 `[状态更新]`

## 部署到远程服务器

可以，但要分清两段链路：

```
本机 Cursor Hooks ──POST──► 状态服务（存当前 status）
手机/手环 ──GET──► 同一台或远程服务器 /status
```

### 方案 A：服务仍在本机（推荐入门）

- 电脑跑 `cursor_status_server.py`
- 手机同一 Wi-Fi 用 `http://局域网IP:3000/status`
- 外网可用 ngrok：`ngrok http 3000`

### 方案 B：状态服务放在 VPS

1. 把 `server/cursor_status_server.py` 拷到 Linux 服务器，安装 Python 3
2. 启动（建议设 Token）：

```bash
export CURSOR_STATUS_PORT=3000
export CURSOR_STATUS_TOKEN=你的随机密钥
python3 cursor_status_server.py
```

3. 用 Nginx 配置 HTTPS（示例域名 `status.example.com`），反代到 `127.0.0.1:3000`
4. **本机 Windows** 设置用户环境变量，让 Hooks 推到远程：

```powershell
[System.Environment]::SetEnvironmentVariable(
  "CURSOR_STATUS_UPDATE_URL", "https://status.example.com/update", "User")
[System.Environment]::SetEnvironmentVariable(
  "CURSOR_STATUS_TOKEN", "你的随机密钥", "User")
```

5. 重新执行 `install-user-hooks.ps1`（会复制最新 `update_status.py`），**重启 Cursor**
6. 手机轮询：`https://status.example.com/status`，请求头加 `X-Api-Token: 你的随机密钥`

> Hooks 必须在本机执行（Cursor 限制），只能把 **POST /update** 指到远程；不能把 Cursor 装到服务器上。

### 方案 C：本机服务 + 内网穿透

本机继续跑 Python，用 Cloudflare Tunnel / frp 暴露 3000，手机走公网域名，Hooks 仍用默认 `http://127.0.0.1:3000/update`。

## 打包为 Windows exe

```powershell
cd cursor-status-monitor
.\scripts\build-exe.ps1
```

生成 `dist\CursorStatusMonitor.exe`（无黑窗口，带状态面板与地址复制）。

首次打包会安装 `pyinstaller`。exe 仍需配合本机已安装的 **全局 Hooks**（`install-user-hooks.ps1`）使用。

可将 exe 快捷方式放到「启动」文件夹实现开机自启。

## 进阶

- **开机自启**：运行 `CursorStatusMonitor.exe` 或 Windows 任务计划程序
- **systemd 示例**：见 `docs/systemd.example.service`

## 故障排查

1. 确认 Python 3 已安装且在 PATH 中
2. 确认状态服务器在运行
3. 轮询必须用 **http://**（除非前面有 Nginx 提供 HTTPS）
4. 在 Cursor **Settings → Hooks** 或 **Hooks 输出通道** 查看 Hook 是否触发
5. 修改 `hooks.json` 后 Cursor 会自动重载；若无反应请重启 Cursor
