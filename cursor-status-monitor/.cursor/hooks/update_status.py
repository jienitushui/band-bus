#!/usr/bin/env python3
"""Cursor Hook 脚本：向本地状态服务器推送 status 变更。"""

import json
import os
import sys
import urllib.error
import urllib.request

DEFAULT_URL = os.environ.get(
    "CURSOR_STATUS_UPDATE_URL",
    "http://127.0.0.1:3000/update",
)
DEFAULT_STATUS = "idle"
VALID_STATUSES = {"idle", "start", "pending", "end"}
# 兼容旧版 wait 写法
STATUS_ALIASES = {"wait": "pending"}


def consume_stdin() -> None:
    """消费 Hook 传入的 stdin；终端直接运行时不阻塞。"""
    if sys.stdin is None or sys.stdin.isatty():
        return
    try:
        sys.stdin.read()
    except OSError:
        pass


def main() -> int:
    consume_stdin()
    status = sys.argv[1] if len(sys.argv) > 1 else DEFAULT_STATUS
    status = STATUS_ALIASES.get(status, status)
    if status not in VALID_STATUSES:
        status = DEFAULT_STATUS

    headers = {"Content-Type": "application/json"}
    token = os.environ.get("CURSOR_STATUS_TOKEN", "").strip()
    if token:
        headers["X-Api-Token"] = token

    payload = json.dumps({"status": status}).encode("utf-8")
    request = urllib.request.Request(
        DEFAULT_URL,
        data=payload,
        headers=headers,
        method="POST",
    )

    try:
        with urllib.request.urlopen(request, timeout=2) as response:
            response.read()
    except (urllib.error.URLError, TimeoutError, OSError):
        # 服务器未启动时不应阻塞 Agent
        pass

    return 0


if __name__ == "__main__":
    raise SystemExit(main())
