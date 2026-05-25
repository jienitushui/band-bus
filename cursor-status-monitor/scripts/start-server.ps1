#Requires -Version 5.1
$ErrorActionPreference = "Stop"

$ProjectRoot = Split-Path -Parent $PSScriptRoot
$ServerScript = Join-Path $ProjectRoot "server\cursor_status_server.py"

if (-not (Test-Path $ServerScript)) {
    throw "找不到服务器脚本: $ServerScript"
}

Write-Host "启动 Cursor 状态服务器..."
python $ServerScript
