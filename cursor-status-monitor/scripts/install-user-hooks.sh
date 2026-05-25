#!/usr/bin/env bash
set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$PROJECT_ROOT"

echo "通过 Python 模块安装全局 Hooks..."
python3 -c "from gui.hooks_installer import install_user_hooks; ok, msg = install_user_hooks(); print(msg); import sys; sys.exit(0 if ok else 1)"
