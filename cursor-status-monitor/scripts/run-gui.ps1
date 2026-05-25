#Requires -Version 5.1
$ProjectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $ProjectRoot
python main.py
