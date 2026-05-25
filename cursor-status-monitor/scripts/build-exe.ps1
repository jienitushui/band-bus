#Requires -Version 5.1
$ErrorActionPreference = "Stop"

$ProjectRoot = Split-Path -Parent $PSScriptRoot
$DistDir = Join-Path $ProjectRoot "dist"

Set-Location $ProjectRoot

Write-Host "安装打包依赖..."
python -m pip install -q -r requirements-build.txt

$HookScript = Join-Path $ProjectRoot ".cursor\hooks\update_status.py"
$Template = Join-Path $ProjectRoot "templates\user-hooks.json"
if (-not (Test-Path $HookScript)) { throw "缺少 $HookScript" }
if (-not (Test-Path $Template)) { throw "缺少 $Template" }

Write-Host "开始打包 exe（无控制台窗口）..."
python -m PyInstaller `
    --noconfirm `
    --clean `
    --onefile `
    --windowed `
    --name "CursorStatusMonitor" `
    --paths "$ProjectRoot" `
    --paths "$ProjectRoot\server" `
    --hidden-import "cursor_status_server" `
    --hidden-import "gui.hooks_installer" `
    --add-data "$HookScript;.cursor\hooks" `
    --add-data "$Template;templates" `
    --collect-submodules "http" `
    "$ProjectRoot\main.py"

$ExePath = Join-Path $DistDir "CursorStatusMonitor.exe"
if (Test-Path $ExePath) {
    Write-Host ""
    Write-Host "打包成功:" -ForegroundColor Green
    Write-Host "  $ExePath"
    Write-Host ""
    Write-Host "双击运行后，点击「一键安装 Hooks」即可，无需单独执行 ps1。"
} else {
    throw "未找到输出文件: $ExePath"
}
