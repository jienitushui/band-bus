"""Cursor 状态监控 — Windows 图形界面（可打包为 exe）。"""

from __future__ import annotations

import os
import queue
import sys
import tkinter as tk
from pathlib import Path
from tkinter import messagebox, scrolledtext, ttk

# 兼容直接运行与 PyInstaller 打包
ROOT = Path(getattr(sys, "_MEIPASS", Path(__file__).resolve().parent.parent))
if str(ROOT / "server") not in sys.path:
    sys.path.insert(0, str(ROOT / "server"))
if str(ROOT / "gui") not in sys.path:
    sys.path.insert(0, str(ROOT))

import cursor_status_server as srv  # noqa: E402
from gui import hooks_installer  # noqa: E402

STATUS_META = {
    "idle": ("空闲", "#9ca3af", "#f3f4f6"),
    "start": ("工作中", "#16a34a", "#dcfce7"),
    "pending": ("等待确认", "#ea580c", "#ffedd5"),
    "end": ("本轮完成", "#2563eb", "#dbeafe"),
}


class CursorStatusApp:
    def __init__(self) -> None:
        self.root = tk.Tk()
        self.root.title("Cursor 状态监控")
        self.root.minsize(520, 560)
        self.root.geometry("560x620")

        self.log_queue: queue.Queue[str] = queue.Queue()
        self.port_var = tk.StringVar(value=str(srv.DEFAULT_PORT))
        self.cursor_info_var = tk.StringVar()

        self._build_ui()
        self._bind_events()
        self._refresh_cursor_info()
        self.root.after(200, self._poll_log_queue)
        self.root.after(500, self._auto_start)

    def _build_ui(self) -> None:
        pad = {"padx": 12, "pady": 6}

        header = ttk.Frame(self.root)
        header.pack(fill="x", **pad)

        ttk.Label(header, text="当前状态", font=("Microsoft YaHei UI", 10)).pack(anchor="w")

        self.status_frame = tk.Frame(self.root, bg="#f3f4f6", relief="groove", bd=1)
        self.status_frame.pack(fill="x", padx=12, pady=(0, 8))

        self.status_label = tk.Label(
            self.status_frame,
            text="空闲",
            font=("Microsoft YaHei UI", 28, "bold"),
            bg="#f3f4f6",
            fg="#9ca3af",
            pady=16,
        )
        self.status_label.pack(fill="x")

        hooks_frame = ttk.LabelFrame(self.root, text="Cursor Hooks")
        hooks_frame.pack(fill="x", **pad)

        info_label = ttk.Label(
            hooks_frame,
            textvariable=self.cursor_info_var,
            justify="left",
            font=("Microsoft YaHei UI", 9),
        )
        info_label.pack(fill="x", padx=8, pady=(8, 4))

        hooks_btns = ttk.Frame(hooks_frame)
        hooks_btns.pack(fill="x", padx=8, pady=(0, 8))

        ttk.Button(hooks_btns, text="一键安装 Hooks", command=self._install_hooks).pack(
            side="left", padx=(0, 8)
        )
        ttk.Button(hooks_btns, text="打开配置目录", command=self._open_cursor_config).pack(
            side="left", padx=(0, 8)
        )
        ttk.Button(hooks_btns, text="刷新检测", command=self._refresh_cursor_info).pack(side="left")

        url_frame = ttk.LabelFrame(self.root, text="轮询地址（请用 http://）")
        url_frame.pack(fill="x", **pad)

        self.url_local_var = tk.StringVar()
        self.url_lan_var = tk.StringVar()

        for label, var in (("本机", self.url_local_var), ("局域网", self.url_lan_var)):
            row = ttk.Frame(url_frame)
            row.pack(fill="x", padx=8, pady=4)
            ttk.Label(row, text=label, width=6).pack(side="left")
            entry = ttk.Entry(row, textvariable=var, state="readonly")
            entry.pack(side="left", fill="x", expand=True, padx=(4, 4))
            ttk.Button(row, text="复制", width=6, command=lambda v=var: self._copy(v.get())).pack(
                side="left"
            )

        ctrl = ttk.Frame(self.root)
        ctrl.pack(fill="x", **pad)

        ttk.Label(ctrl, text="端口").pack(side="left")
        ttk.Spinbox(ctrl, from_=1024, to=65535, textvariable=self.port_var, width=8).pack(
            side="left", padx=(4, 12)
        )

        self.btn_toggle = ttk.Button(ctrl, text="停止服务", command=self._toggle_server)
        self.btn_toggle.pack(side="left")

        log_frame = ttk.LabelFrame(self.root, text="日志")
        log_frame.pack(fill="both", expand=True, **pad)

        self.log_text = scrolledtext.ScrolledText(
            log_frame, height=8, font=("Consolas", 9), state="disabled"
        )
        self.log_text.pack(fill="both", expand=True, padx=8, pady=8)

        self.status_bar = ttk.Label(self.root, text="就绪", anchor="w")
        self.status_bar.pack(fill="x", padx=12, pady=(0, 8))

    def _bind_events(self) -> None:
        srv.add_status_listener(lambda status: self.log_queue.put(f"__STATUS__:{status}"))
        self.root.protocol("WM_DELETE_WINDOW", self._on_close)
        self._refresh_status_ui(srv.get_current_status())

    def _refresh_cursor_info(self) -> None:
        self.cursor_info_var.set(hooks_installer.get_install_preview())

    def _install_hooks(self) -> None:
        if not messagebox.askyesno(
            "安装 Hooks",
            "将写入全局配置 %USERPROFILE%\\.cursor\\hooks.json\n"
            "若已有配置会先备份为 hooks.json.bak.*\n\n"
            "安装后需重启 Cursor。是否继续？",
        ):
            return

        try:
            ok, message = hooks_installer.install_user_hooks()
        except FileNotFoundError as exc:
            messagebox.showerror("安装失败", str(exc))
            return
        except OSError as exc:
            messagebox.showerror("安装失败", f"写入失败: {exc}")
            return

        self._append_log(message)
        self._refresh_cursor_info()
        self.status_bar.configure(text="Hooks 已安装，请重启 Cursor")

        if ok:
            messagebox.showinfo("安装完成", message)

    def _open_cursor_config(self) -> None:
        paths = hooks_installer.find_cursor_paths()
        paths.user_config_dir.mkdir(parents=True, exist_ok=True)
        os.startfile(paths.user_config_dir)  # type: ignore[attr-defined]

    def _auto_start(self) -> None:
        if not srv.is_server_running():
            self._start_server()

    def _append_log(self, message: str) -> None:
        self.log_text.configure(state="normal")
        self.log_text.insert("end", message + "\n")
        self.log_text.see("end")
        self.log_text.configure(state="disabled")

    def _poll_log_queue(self) -> None:
        while True:
            try:
                item = self.log_queue.get_nowait()
            except queue.Empty:
                break
            if item.startswith("__STATUS__:"):
                self._refresh_status_ui(item.split(":", 1)[1])
            else:
                self._append_log(item)
        self.root.after(200, self._poll_log_queue)

    def _refresh_status_ui(self, status: str) -> None:
        label, fg, bg = STATUS_META.get(status, ("未知", "#6b7280", "#f3f4f6"))
        self.status_label.configure(text=label, fg=fg, bg=bg)
        self.status_frame.configure(bg=bg)

    def _update_urls(self, port: int) -> None:
        ip = srv.get_local_ip()
        self.url_local_var.set(f"http://127.0.0.1:{port}/status")
        self.url_lan_var.set(f"http://{ip}:{port}/status")

    def _copy(self, text: str) -> None:
        if not text:
            return
        self.root.clipboard_clear()
        self.root.clipboard_append(text)
        self.status_bar.configure(text="已复制到剪贴板")

    def _parse_port(self) -> int | None:
        try:
            port = int(self.port_var.get().strip())
        except ValueError:
            messagebox.showerror("端口错误", "请输入有效端口号")
            return None
        if port < 1024 or port > 65535:
            messagebox.showerror("端口错误", "端口范围应为 1024–65535")
            return None
        return port

    def _start_server(self) -> None:
        port = self._parse_port()
        if port is None:
            return

        ok, message = srv.start_server(port)
        self._append_log(message)
        self.status_bar.configure(text=message)

        if ok:
            self._update_urls(port)
            self.btn_toggle.configure(text="停止服务")
            self._refresh_status_ui(srv.get_current_status())
        else:
            messagebox.showerror("启动失败", message)

    def _stop_server(self) -> None:
        srv.stop_server()
        self.btn_toggle.configure(text="启动服务")
        self._append_log("服务已停止")
        self.status_bar.configure(text="服务已停止")

    def _toggle_server(self) -> None:
        if srv.is_server_running():
            self._stop_server()
        else:
            self._start_server()

    def _on_close(self) -> None:
        srv.stop_server()
        self.root.destroy()

    def run(self) -> None:
        self.root.mainloop()


def run_app() -> None:
    CursorStatusApp().run()


if __name__ == "__main__":
    run_app()
