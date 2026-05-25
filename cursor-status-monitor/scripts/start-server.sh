#!/usr/bin/env bash
set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
SERVER_SCRIPT="$PROJECT_ROOT/server/cursor_status_server.py"

if [[ ! -f "$SERVER_SCRIPT" ]]; then
  echo "找不到服务器脚本: $SERVER_SCRIPT" >&2
  exit 1
fi

echo "启动 Cursor 状态服务器..."
exec python "$SERVER_SCRIPT"
