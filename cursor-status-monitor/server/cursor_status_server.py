"""Cursor 状态中转服务 — 接收 Hooks 推送，对外提供轮询接口。"""

from http.server import BaseHTTPRequestHandler, HTTPServer
import json
import os
import socket
import sys
import threading
from typing import Callable

DEFAULT_PORT = int(os.environ.get("CURSOR_STATUS_PORT", "3000"))
DEFAULT_STATUS = "idle"
VALID_STATUSES = frozenset({"idle", "start", "pending", "end"})
API_TOKEN = os.environ.get("CURSOR_STATUS_TOKEN", "").strip()
VERBOSE = os.environ.get("CURSOR_STATUS_VERBOSE", "") == "1"
QUIET = os.environ.get("CURSOR_STATUS_QUIET", "1") == "1"

_status_listeners: list[Callable[[str], None]] = []
_server: HTTPServer | None = None
_server_thread: threading.Thread | None = None


def get_current_status() -> str:
    return StatusHandler.current_status


def add_status_listener(callback: Callable[[str], None]) -> None:
    _status_listeners.append(callback)


def _notify_status(status: str) -> None:
    for callback in _status_listeners:
        try:
            callback(status)
        except Exception:
            pass


class StatusHandler(BaseHTTPRequestHandler):
    current_status = DEFAULT_STATUS
    server_version = "CursorStatus/1.0"

    def log_message(self, format: str, *args) -> None:
        if QUIET:
            return
        print(f"[{self.log_date_time_string()}] {format % args}", flush=True)

    def log_error(self, format: str, *args) -> None:
        msg = format % args if args else format
        if not VERBOSE and (
            "Bad request version" in msg
            or "Bad HTTP/0.9" in msg
            or "Bad request syntax" in msg
        ):
            return
        print(f"[{self.log_date_time_string()}] {msg}", file=sys.stderr, flush=True)

    def _authorized(self) -> bool:
        if not API_TOKEN:
            return True
        token = self.headers.get("X-Api-Token", "")
        return token == API_TOKEN

    def _send_json(self, status_code: int, payload: dict, *, cors: bool = False) -> None:
        body = json.dumps(payload, ensure_ascii=False).encode("utf-8")
        self.send_response(status_code)
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.send_header("Content-Length", str(len(body)))
        if cors:
            self.send_header("Access-Control-Allow-Origin", "*")
        self.end_headers()
        self.wfile.write(body)

    def _set_status(self, status: str) -> None:
        if status in VALID_STATUSES:
            StatusHandler.current_status = status
            print(f"[状态更新] {status}", flush=True)
            _notify_status(status)

    def do_POST(self) -> None:
        if not self._authorized():
            self._send_json(401, {"success": False, "error": "unauthorized"})
            return
        if self.path != "/update":
            self.send_error(404)
            return

        content_length = int(self.headers.get("Content-Length", 0))
        post_data = self.rfile.read(content_length)

        try:
            data = json.loads(post_data.decode("utf-8"))
            self._set_status(data.get("status", StatusHandler.current_status))
            self._send_json(200, {"success": True})
        except (json.JSONDecodeError, UnicodeDecodeError):
            self._send_json(400, {"success": False, "error": "invalid json"})

    def do_GET(self) -> None:
        if not self._authorized():
            self._send_json(401, {"success": False, "error": "unauthorized"})
            return
        if self.path == "/status":
            self._send_json(200, {"status": StatusHandler.current_status}, cors=True)
            return
        if self.path == "/health":
            self._send_json(200, {"ok": True, "status": StatusHandler.current_status}, cors=True)
            return
        self.send_error(404)


def get_local_ip() -> str:
    try:
        with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as sock:
            sock.connect(("8.8.8.8", 80))
            return sock.getsockname()[0]
    except OSError:
        return "127.0.0.1"


def is_server_running() -> bool:
    return _server is not None and _server_thread is not None and _server_thread.is_alive()


def start_server(port: int | None = None) -> tuple[bool, str]:
    """在后台线程启动 HTTP 服务。返回 (成功与否, 消息)。"""
    global _server, _server_thread

    if is_server_running():
        return False, "服务已在运行"

    port = port or DEFAULT_PORT
    try:
        _server = HTTPServer(("0.0.0.0", port), StatusHandler)
    except OSError as exc:
        return False, f"无法监听端口 {port}: {exc}"

    _server_thread = threading.Thread(target=_server.serve_forever, daemon=True)
    _server_thread.start()

    local_ip = get_local_ip()
    return True, (
        f"已启动 · 本机 http://127.0.0.1:{port}/status · "
        f"局域网 http://{local_ip}:{port}/status"
    )


def stop_server() -> None:
    global _server, _server_thread

    if _server is not None:
        _server.shutdown()
        _server.server_close()
        _server = None
    _server_thread = None


def main() -> None:
    ok, message = start_server()
    if not ok:
        print(message, flush=True)
        raise SystemExit(1)

    print("Cursor 状态 API 已启动", flush=True)
    print(message, flush=True)
    print("  请使用 http:// 不要用 https://", flush=True)
    if API_TOKEN:
        print("  已启用 Token 校验（请求头 X-Api-Token）", flush=True)
    print("按 Ctrl+C 停止服务", flush=True)

    try:
        while is_server_running():
            _server_thread.join(timeout=1)  # type: ignore[union-attr]
    except KeyboardInterrupt:
        stop_server()
        print("已停止", flush=True)


if __name__ == "__main__":
    main()
