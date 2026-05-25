#Requires -Version 5.1
$ErrorActionPreference = "Stop"

$ProjectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $ProjectRoot

Write-Host "通过 Python 模块安装全局 Hooks..."
python -c "from gui.hooks_installer import install_user_hooks; ok, msg = install_user_hooks(); print(msg); import sys; sys.exit(0 if ok else 1)"
