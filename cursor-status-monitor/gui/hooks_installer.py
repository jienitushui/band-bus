"""检测 Cursor 路径并安装全局 Hooks。"""

from __future__ import annotations

import json
import os
import shutil
import sys
from dataclasses import dataclass
from datetime import datetime
from pathlib import Path

HOOK_EVENTS = (
    "beforeSubmitPrompt",
    "preToolUse",
    "beforeShellExecution",
    "beforeMCPExecution",
    "stop",
    "sessionEnd",
)


@dataclass
class CursorPaths:
    cursor_exe: Path | None
    user_config_dir: Path
    hooks_dir: Path
    hooks_json: Path

    @property
    def cursor_exe_display(self) -> str:
        if self.cursor_exe and self.cursor_exe.is_file():
            return str(self.cursor_exe)
        return "未检测到（不影响 Hooks 安装）"


def get_app_root() -> Path:
    """开发目录或 PyInstaller 解压目录。"""
    if getattr(sys, "frozen", False):
        return Path(sys._MEIPASS)  # type: ignore[attr-defined]
    return Path(__file__).resolve().parent.parent


def find_cursor_paths() -> CursorPaths:
    local_app = Path(os.environ.get("LOCALAPPDATA", ""))
    cursor_candidates = [
        local_app / "Programs" / "cursor" / "Cursor.exe",
        local_app / "Programs" / "Cursor" / "Cursor.exe",
        local_app / "cursor" / "Cursor.exe",
    ]

    cursor_exe: Path | None = None
    for candidate in cursor_candidates:
        if candidate.is_file():
            cursor_exe = candidate
            break

    if cursor_exe is None:
        cursor_from_path = shutil.which("cursor")
        if cursor_from_path:
            cursor_exe = Path(cursor_from_path)

    user_config = Path.home() / ".cursor"
    hooks_dir = user_config / "hooks"
    return CursorPaths(
        cursor_exe=cursor_exe,
        user_config_dir=user_config,
        hooks_dir=hooks_dir,
        hooks_json=user_config / "hooks.json",
    )


def find_python_executable() -> str:
    """Hooks 需调用系统 Python 解释器（exe 本体不能替代）。"""
    for name in ("python", "python3", "py"):
        found = shutil.which(name)
        if found:
            return found

    for candidate in (
        Path(os.environ.get("LOCALAPPDATA", "")) / "Programs" / "Python",
        Path(r"C:\Python313"),
        Path(r"C:\Python312"),
    ):
        if candidate.is_dir():
            for exe in candidate.rglob("python.exe"):
                return str(exe)

    return "python"


def resolve_hook_script_source() -> Path:
    root = get_app_root()
    candidates = [
        root / ".cursor" / "hooks" / "update_status.py",
        Path(__file__).resolve().parent.parent / ".cursor" / "hooks" / "update_status.py",
    ]
    for path in candidates:
        if path.is_file():
            return path
    raise FileNotFoundError("找不到 update_status.py，请重新打包或检查项目文件")


def resolve_hooks_template() -> dict:
    root = get_app_root()
    template_path = root / "templates" / "user-hooks.json"
    if not template_path.is_file():
        template_path = Path(__file__).resolve().parent.parent / "templates" / "user-hooks.json"
    if not template_path.is_file():
        raise FileNotFoundError("找不到 templates/user-hooks.json")

    with template_path.open(encoding="utf-8") as file:
        return json.load(file)


def build_hooks_config(hooks_dir: Path, python_exe: str) -> dict:
    template = resolve_hooks_template()
    hooks_dir_json = str(hooks_dir).replace("\\", "\\\\")
    python_json = str(python_exe).replace("\\", "\\\\")

    raw = json.dumps(template, ensure_ascii=False)
    raw = raw.replace("{{HOOKS_DIR}}", hooks_dir_json)
    raw = raw.replace("{{PYTHON_EXE}}", python_json)
    return json.loads(raw)


def is_hooks_installed(paths: CursorPaths) -> bool:
    script = paths.hooks_dir / "update_status.py"
    if not script.is_file() or not paths.hooks_json.is_file():
        return False
    try:
        with paths.hooks_json.open(encoding="utf-8") as file:
            config = json.load(file)
        hooks = config.get("hooks", {})
        for event in HOOK_EVENTS:
            entries = hooks.get(event, [])
            if not entries:
                return False
            command = entries[0].get("command", "")
            if "update_status.py" not in command:
                return False
        return True
    except (json.JSONDecodeError, OSError):
        return False


def merge_hooks_config(existing: dict, new_config: dict) -> dict:
    merged = dict(existing)
    merged["version"] = new_config.get("version", merged.get("version", 1))
    hooks = dict(merged.get("hooks", {}))
    for event in HOOK_EVENTS:
        if event in new_config.get("hooks", {}):
            hooks[event] = new_config["hooks"][event]
    merged["hooks"] = hooks
    return merged


def install_user_hooks() -> tuple[bool, str]:
    paths = find_cursor_paths()
    python_exe = find_python_executable()
    source_script = resolve_hook_script_source()
    new_config = build_hooks_config(paths.hooks_dir, python_exe)

    paths.hooks_dir.mkdir(parents=True, exist_ok=True)
    shutil.copy2(source_script, paths.hooks_dir / "update_status.py")

    if paths.hooks_json.is_file():
        backup = paths.hooks_json.with_suffix(
            f".bak.{datetime.now().strftime('%Y%m%d-%H%M%S')}"
        )
        shutil.copy2(paths.hooks_json, backup)
        try:
            with paths.hooks_json.open(encoding="utf-8") as file:
                existing = json.load(file)
            final_config = merge_hooks_config(existing, new_config)
        except json.JSONDecodeError:
            final_config = new_config
    else:
        paths.user_config_dir.mkdir(parents=True, exist_ok=True)
        final_config = new_config

    with paths.hooks_json.open("w", encoding="utf-8") as file:
        json.dump(final_config, file, ensure_ascii=False, indent=2)
        file.write("\n")

    lines = [
        "Hooks 安装成功",
        f"  Cursor 程序: {paths.cursor_exe_display}",
        f"  配置目录: {paths.user_config_dir}",
        f"  hooks.json: {paths.hooks_json}",
        f"  脚本: {paths.hooks_dir / 'update_status.py'}",
        f"  Python: {python_exe}",
        "",
        "请完全退出并重新打开 Cursor 后生效。",
    ]
    return True, "\n".join(lines)


def get_install_preview() -> str:
    paths = find_cursor_paths()
    installed = is_hooks_installed(paths)
    status = "已安装" if installed else "未安装"
    return (
        f"Cursor: {paths.cursor_exe_display}\n"
        f"配置: {paths.user_config_dir}\n"
        f"Hooks: {status}"
    )
