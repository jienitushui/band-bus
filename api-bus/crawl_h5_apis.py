#!/usr/bin/env python3
"""
Capture and document H5 API calls.

The script combines two signals:
1. Static scan of local/downloaded JavaScript for $.ajax({ data: { ... } }).
2. Browser automation with Playwright that records real requests and response
   JSON schemas while lightly exploring the page.
3. ApiData.do replay executed through the loaded page's own $.ajax.
"""

from __future__ import annotations

import argparse
import asyncio
import json
import re
import sys
import time
from collections import defaultdict
from dataclasses import dataclass, field
from pathlib import Path
from typing import Any
from urllib.parse import parse_qs, urljoin, urlparse

from bs4 import BeautifulSoup
from playwright.async_api import Page, Response, async_playwright


DEFAULT_URL = "https://h5.mygolbs.com/?areacode=qz595803"
DEFAULT_OUTPUT_DIR = Path("output")
JSON_TYPES = {"application/json", "text/json", "text/javascript"}
COMMON_API_CMDS = ("103", "104", "110", "114", "115", "119", "120", "203", "205", "209")
MOJIBAKE_MARKERS = set(
    "ÃÂÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿ"
    "€šœžŸ‰…™“”‘’ˆ˜"
)


def now_ms() -> int:
    return int(time.time() * 1000)


def ensure_ascii_safe(text: str) -> str:
    return text.encode("utf-8", errors="replace").decode("utf-8")


def cp1252_mojibake_bytes(text: str) -> bytes:
    raw = bytearray()
    for ch in text:
        codepoint = ord(ch)
        if codepoint <= 0xFF:
            raw.append(codepoint)
            continue
        raw.extend(ch.encode("cp1252"))
    return bytes(raw)


def looks_mojibaked(text: str) -> bool:
    if any(ch in MOJIBAKE_MARKERS for ch in text):
        return True
    return any(0x80 <= ord(ch) <= 0x9F for ch in text)


def cjk_count(text: str) -> int:
    return sum(1 for ch in text if "\u4e00" <= ch <= "\u9fff")


def repair_mojibake_text(text: str) -> str:
    if not text or not looks_mojibaked(text):
        return text
    try:
        repaired = cp1252_mojibake_bytes(text).decode("utf-8")
    except (UnicodeEncodeError, UnicodeDecodeError):
        return text
    if cjk_count(repaired) > cjk_count(text) or len(repaired) < len(text):
        return repaired
    return text


def repair_mojibake(value: Any) -> Any:
    if isinstance(value, dict):
        return {key: repair_mojibake(val) for key, val in value.items()}
    if isinstance(value, list):
        return [repair_mojibake(item) for item in value]
    if isinstance(value, str):
        return repair_mojibake_text(value)
    return value


def compact_text(text: str, limit: int = 4000) -> str:
    text = ensure_ascii_safe(repair_mojibake_text(text))
    if len(text) <= limit:
        return text
    return text[:limit] + f"... <truncated {len(text) - limit} chars>"


def parse_urlencoded(body: str | None) -> dict[str, Any]:
    if not body:
        return {}
    parsed = parse_qs(body, keep_blank_values=True)
    return {key: values[0] if len(values) == 1 else values for key, values in parsed.items()}


def parse_json_maybe(text: str) -> Any:
    text = text.strip()
    if not text:
        return None
    try:
        return repair_mojibake(json.loads(text))
    except Exception:
        pass
    jsonp_match = re.match(r"^[\w$.]+\((.*)\)\s*;?\s*$", text, re.S)
    if jsonp_match:
        try:
            return repair_mojibake(json.loads(jsonp_match.group(1)))
        except Exception:
            return None
    return None


def looks_unusable_encrypted_text(text: str) -> bool:
    value = text.strip()
    if not value:
        return False
    return value.startswith("0vx0") or bool(re.fullmatch(r"[A-Za-z0-9+/=]{24,}", value))


def decode_response_bytes(body: bytes, content_type: str = "") -> str:
    charset_match = re.search(r"charset=([\w.-]+)", content_type, re.I)
    encodings = ["utf-8"]
    if charset_match:
        encodings.insert(0, charset_match.group(1))
    encodings.extend(["gb18030", "cp1252"])
    seen: set[str] = set()
    for encoding in encodings:
        key = encoding.lower()
        if key in seen:
            continue
        seen.add(key)
        try:
            return repair_mojibake_text(body.decode(encoding))
        except (LookupError, UnicodeDecodeError):
            continue
    return repair_mojibake_text(body.decode("utf-8", errors="replace"))


def parse_payload(post_data: str | None, content_type: str = "") -> Any:
    if not post_data:
        return {}
    content_type = content_type.lower()
    if "json" in content_type:
        parsed = parse_json_maybe(post_data)
        return parsed if parsed is not None else post_data
    parsed_json = parse_json_maybe(post_data)
    if parsed_json is not None:
        return parsed_json
    if "=" in post_data or "&" in post_data:
        return parse_urlencoded(post_data)
    return post_data


def normalize_url(url: str) -> str:
    parsed = urlparse(url)
    path = parsed.path or "/"
    if parsed.query:
        return f"{parsed.scheme}://{parsed.netloc}{path}?{parsed.query}"
    return f"{parsed.scheme}://{parsed.netloc}{path}"


def payload_cmd(payload: Any) -> str:
    if isinstance(payload, dict):
        value = payload.get("CMD") or payload.get("cmd")
        if value is not None:
            return str(value)
    return ""


def endpoint_key(method: str, url: str, payload: Any) -> str:
    cmd = payload_cmd(payload)
    base = normalize_url(url)
    return f"{method.upper()} {base} CMD={cmd}" if cmd else f"{method.upper()} {base}"


def scalar_sample(value: Any) -> Any:
    if isinstance(value, str):
        return compact_text(value, 120)
    if isinstance(value, (int, float, bool)) or value is None:
        return value
    return None


def infer_schema(value: Any, depth: int = 0, max_depth: int = 8) -> dict[str, Any]:
    if depth >= max_depth:
        return {"type": type(value).__name__}
    if isinstance(value, dict):
        return {
            "type": "object",
            "fields": {
                str(key): infer_schema(val, depth + 1, max_depth)
                for key, val in sorted(value.items(), key=lambda item: str(item[0]))
            },
        }
    if isinstance(value, list):
        if not value:
            return {"type": "array", "items": {"type": "unknown"}, "length_sample": 0}
        merged = infer_schema(value[0], depth + 1, max_depth)
        for item in value[1:20]:
            merged = merge_schema(merged, infer_schema(item, depth + 1, max_depth))
        return {"type": "array", "items": merged, "length_sample": len(value)}
    if isinstance(value, bool):
        return {"type": "boolean", "sample": value}
    if isinstance(value, int) and not isinstance(value, bool):
        return {"type": "integer", "sample": value}
    if isinstance(value, float):
        return {"type": "number", "sample": value}
    if value is None:
        return {"type": "null"}
    return {"type": "string", "sample": scalar_sample(value)}


def merge_schema(left: dict[str, Any], right: dict[str, Any]) -> dict[str, Any]:
    if not left:
        return right
    if not right:
        return left
    if left.get("type") != right.get("type"):
        types = []
        for schema in (left, right):
            typ = schema.get("type")
            if isinstance(typ, list):
                types.extend(typ)
            elif typ:
                types.append(typ)
        return {"type": sorted(set(types))}
    typ = left.get("type")
    merged = dict(left)
    if typ == "object":
        fields: dict[str, Any] = {}
        left_fields = left.get("fields", {})
        right_fields = right.get("fields", {})
        for key in sorted(set(left_fields) | set(right_fields)):
            fields[key] = merge_schema(left_fields.get(key, {}), right_fields.get(key, {}))
        merged["fields"] = fields
    elif typ == "array":
        merged["items"] = merge_schema(left.get("items", {}), right.get("items", {}))
        merged["length_sample"] = max(left.get("length_sample", 0), right.get("length_sample", 0))
    elif "sample" not in merged and "sample" in right:
        merged["sample"] = right["sample"]
    return merged


def schema_type_label(typ: Any) -> str:
    if isinstance(typ, list):
        return " | ".join(str(item) for item in typ)
    return str(typ)


def markdown_schema(schema: dict[str, Any], indent: int = 0, max_lines: int = 80) -> list[str]:
    lines: list[str] = []
    prefix = "  " * indent
    typ = schema.get("type", "unknown")
    if typ == "object":
        fields = schema.get("fields", {})
        if not fields:
            return [prefix + "- object"]
        for key, sub_schema in fields.items():
            sub_type = sub_schema.get("type", "unknown")
            sub_type_label = schema_type_label(sub_type)
            sample = sub_schema.get("sample")
            nested = sub_type in {"object", "array"} if isinstance(sub_type, str) else False
            suffix = f" = `{sample}`" if sample is not None and not nested else ""
            lines.append(f"{prefix}- `{key}`: {sub_type_label}{suffix}")
            if nested:
                lines.extend(markdown_schema(sub_schema, indent + 1, max_lines))
            if len(lines) >= max_lines:
                lines.append(prefix + "- ...")
                return lines
        return lines
    if typ == "array":
        lines.append(prefix + f"- array, sample length `{schema.get('length_sample', 0)}`")
        lines.extend(markdown_schema(schema.get("items", {}), indent + 1, max_lines))
        return lines[:max_lines]
    sample = schema.get("sample")
    suffix = f", sample `{sample}`" if sample is not None else ""
    return [prefix + f"- {schema_type_label(typ)}{suffix}"]


def find_matching(text: str, start: int, open_char: str = "{", close_char: str = "}") -> int:
    depth = 0
    quote: str | None = None
    escape = False
    line_comment = False
    block_comment = False
    for index in range(start, len(text)):
        ch = text[index]
        nxt = text[index + 1] if index + 1 < len(text) else ""
        if line_comment:
            if ch in "\r\n":
                line_comment = False
            continue
        if block_comment:
            if ch == "*" and nxt == "/":
                block_comment = False
            continue
        if quote:
            if escape:
                escape = False
            elif ch == "\\":
                escape = True
            elif ch == quote:
                quote = None
            continue
        if ch in ("'", '"', "`"):
            quote = ch
            continue
        if ch == "/" and nxt == "/":
            line_comment = True
            continue
        if ch == "/" and nxt == "*":
            block_comment = True
            continue
        if ch == open_char:
            depth += 1
        elif ch == close_char:
            depth -= 1
            if depth == 0:
                return index
    return -1


def nearest_function_name(text: str, pos: int) -> str:
    prefix = text[:pos]
    matches = list(re.finditer(r"(?:function\s+([A-Za-z_$][\w$]*)|([A-Za-z_$][\w$]*)\s*=\s*function)\s*\(", prefix))
    if not matches:
        return ""
    match = matches[-1]
    return match.group(1) or match.group(2) or ""


def find_property_object(text: str, prop: str) -> tuple[int, int] | None:
    pattern = re.compile(rf"\b{re.escape(prop)}\s*:\s*\{{")
    match = pattern.search(text)
    if not match:
        return None
    start = text.find("{", match.start())
    end = find_matching(text, start)
    if end < 0:
        return None
    return start, end


def find_property_scalar(text: str, prop: str) -> str:
    match = re.search(rf"\b{re.escape(prop)}\s*:\s*([^,\n\r}}]+)", text)
    if not match:
        return ""
    return match.group(1).strip().strip("'\"")


def split_object_pairs(obj_body: str) -> dict[str, str]:
    pairs: dict[str, str] = {}
    i = 0
    while i < len(obj_body):
        key_match = re.search(r"['\"]?([A-Za-z0-9_$-]+)['\"]?\s*:", obj_body[i:])
        if not key_match:
            break
        key = key_match.group(1)
        value_start = i + key_match.end()
        cursor = value_start
        depth = 0
        quote: str | None = None
        escape = False
        while cursor < len(obj_body):
            ch = obj_body[cursor]
            if quote:
                if escape:
                    escape = False
                elif ch == "\\":
                    escape = True
                elif ch == quote:
                    quote = None
            else:
                if ch in ("'", '"', "`"):
                    quote = ch
                elif ch in "([{":
                    depth += 1
                elif ch in ")]}":
                    if depth > 0:
                        depth -= 1
                elif ch == "," and depth == 0:
                    break
            cursor += 1
        pairs[key] = obj_body[value_start:cursor].strip().rstrip(",")
        i = cursor + 1
    return pairs


def scan_static_ajax(paths: list[Path]) -> list[dict[str, Any]]:
    endpoints: list[dict[str, Any]] = []
    for path in paths:
        if not path.exists():
            continue
        text = path.read_text(encoding="utf-8", errors="ignore")
        cursor = 0
        while True:
            pos = text.find("$.ajax", cursor)
            if pos < 0:
                break
            open_paren = text.find("(", pos)
            open_brace = text.find("{", open_paren)
            if open_paren < 0 or open_brace < 0:
                cursor = pos + 6
                continue
            end = find_matching(text, open_brace)
            if end < 0:
                cursor = pos + 6
                continue
            ajax_body = text[open_brace + 1 : end]
            data_range = find_property_object(ajax_body, "data")
            data_fields: dict[str, str] = {}
            cmd = ""
            if data_range:
                data_body = ajax_body[data_range[0] + 1 : data_range[1]]
                data_fields = split_object_pairs(data_body)
                cmd = data_fields.get("CMD", "").strip("'\"")
            endpoints.append(
                {
                    "source": str(path),
                    "line": text.count("\n", 0, pos) + 1,
                    "function": nearest_function_name(text, pos),
                    "method": find_property_scalar(ajax_body, "type") or "GET",
                    "dataType": find_property_scalar(ajax_body, "dataType"),
                    "url": find_property_scalar(ajax_body, "url") or "",
                    "cmd": cmd,
                    "params": data_fields,
                }
            )
            cursor = end + 1
    return endpoints


@dataclass
class CaptureRecord:
    method: str
    url: str
    status: int | None
    request_headers: dict[str, str]
    response_headers: dict[str, str]
    payload: Any
    response_json: Any = None
    response_text: str = ""
    error: str = ""
    page_url: str = ""
    capture_source: str = "browser"
    timestamp_ms: int = field(default_factory=now_ms)

    def as_dict(self) -> dict[str, Any]:
        return {
            "method": self.method,
            "url": self.url,
            "status": self.status,
            "request_headers": self.request_headers,
            "response_headers": self.response_headers,
            "payload": self.payload,
            "response_json": self.response_json,
            "response_text": self.response_text,
            "error": self.error,
            "page_url": self.page_url,
            "capture_source": self.capture_source,
            "timestamp_ms": self.timestamp_ms,
        }


async def read_response(response: Response, page_url: str) -> CaptureRecord | None:
    request = response.request
    resource_type = request.resource_type
    headers = {k.lower(): v for k, v in request.headers.items()}
    response_headers = {k.lower(): v for k, v in response.headers.items()}
    content_type = response_headers.get("content-type", "")
    post_data = request.post_data
    payload = parse_payload(post_data, headers.get("content-type", ""))

    looks_like_api = (
        resource_type in {"xhr", "fetch"}
        or "ApiData.do" in response.url
        or any(kind in content_type.lower() for kind in JSON_TYPES)
    )
    if not looks_like_api:
        return None

    record = CaptureRecord(
        method=request.method,
        url=response.url,
        status=response.status,
        request_headers=dict(headers),
        response_headers=dict(response_headers),
        payload=payload,
        page_url=page_url,
    )
    try:
        body = await response.body()
        text = decode_response_bytes(body, content_type)
        parsed = parse_json_maybe(text)
        if parsed is not None:
            record.response_json = parsed
        else:
            record.response_text = compact_text(text)
    except Exception as exc:
        record.error = str(exc)
        try:
            record.response_text = compact_text(await response.text())
        except Exception:
            pass
    return record


async def collect_script_urls(page: Page) -> list[str]:
    try:
        html = await page.content()
    except Exception:
        return []
    soup = BeautifulSoup(html, "html.parser")
    urls = []
    for script in soup.find_all("script"):
        src = script.get("src")
        if src:
            urls.append(urljoin(page.url, src))
    return urls


async def download_scripts(page: Page, output_dir: Path) -> list[Path]:
    output_dir.mkdir(parents=True, exist_ok=True)
    script_paths: list[Path] = []
    for script_url in await collect_script_urls(page):
        parsed = urlparse(script_url)
        name = Path(parsed.path).name or "script.js"
        if not name.endswith(".js"):
            continue
        target = output_dir / f"remote-{name}"
        try:
            response = await page.request.get(script_url, timeout=15000)
            if response.ok:
                target.write_text(await response.text(), encoding="utf-8")
                script_paths.append(target)
        except Exception:
            continue
    return script_paths


async def click_visible_targets(page: Page, max_clicks: int, delay_ms: int) -> int:
    clicked = 0
    selector = "button, a, [role=button], [onclick], li, .tab, .home-main-item, .near-list-view-con"
    seen: set[str] = set()
    for _ in range(max_clicks):
        handles = await page.locator(selector).element_handles()
        made_progress = False
        for handle in handles:
            if clicked >= max_clicks:
                return clicked
            try:
                box = await handle.bounding_box()
                if not box or box["width"] < 4 or box["height"] < 4:
                    continue
                label = await handle.evaluate(
                    """el => [
                        el.tagName,
                        el.id || '',
                        el.className || '',
                        (el.innerText || el.textContent || '').trim().slice(0, 80),
                        el.getAttribute('onclick') || '',
                        location.href
                    ].join('|')"""
                )
                if label in seen:
                    continue
                seen.add(label)
                await handle.scroll_into_view_if_needed(timeout=1500)
                await handle.click(timeout=2500, force=True)
                clicked += 1
                made_progress = True
                await page.wait_for_timeout(delay_ms)
            except Exception:
                continue
        if not made_progress:
            break
    return clicked


async def fill_inputs(page: Page, keywords: list[str], delay_ms: int) -> None:
    for keyword in keywords:
        inputs = await page.locator("input:not([type=hidden]), textarea").element_handles()
        for handle in inputs:
            try:
                box = await handle.bounding_box()
                if not box or box["width"] < 8 or box["height"] < 8:
                    continue
                await handle.scroll_into_view_if_needed(timeout=1500)
                await handle.fill(keyword, timeout=2500)
                await handle.press("Enter", timeout=1500)
                await page.wait_for_timeout(delay_ms)
                await handle.evaluate(
                    """el => {
                        el.dispatchEvent(new Event('input', { bubbles: true }));
                        el.dispatchEvent(new Event('change', { bubbles: true }));
                        el.dispatchEvent(new KeyboardEvent('keyup', { bubbles: true, key: 'Enter' }));
                    }"""
                )
                await page.wait_for_timeout(delay_ms)
            except Exception:
                continue


async def seed_page_state(page: Page, url: str) -> None:
    parsed = urlparse(url)
    areacode = parse_qs(parsed.query).get("areacode", [""])[0]
    if not areacode:
        return
    encoded = json.dumps(areacode, ensure_ascii=False)
    await page.add_init_script(
        f"""(() => {{
            try {{
                localStorage.setItem('citykey', {encoded});
            }} catch (_) {{}}
        }})()"""
    )


async def run_browser_capture(args: argparse.Namespace) -> tuple[list[dict[str, Any]], list[Path]]:
    records: list[CaptureRecord] = []
    downloaded_scripts: list[Path] = []
    ignored_response_payloads: set[str] = set()
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=not args.headed)
        context = await browser.new_context(
            viewport={"width": args.width, "height": args.height},
            user_agent=(
                "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) "
                "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.0 "
                "Mobile/15E148 Safari/604.1"
            ),
            locale="zh-CN",
            timezone_id=args.timezone,
            geolocation={"latitude": args.latitude, "longitude": args.longitude},
            permissions=["geolocation"],
        )
        page = await context.new_page()
        await seed_page_state(page, args.url)

        async def on_response(response: Response) -> None:
            if "ApiData.do" in response.url:
                request = response.request
                headers = {k.lower(): v for k, v in request.headers.items()}
                payload = parse_payload(request.post_data, headers.get("content-type", ""))
                signature = json.dumps(payload, ensure_ascii=False, sort_keys=True, default=str)
                if signature in ignored_response_payloads:
                    return
            record = await read_response(response, page.url)
            if record:
                records.append(record)

        page.on("response", lambda response: asyncio.create_task(on_response(response)))

        await page.goto(args.url, wait_until="domcontentloaded", timeout=args.timeout)
        await page.wait_for_timeout(args.initial_wait)
        try:
            await page.wait_for_load_state("networkidle", timeout=10000)
        except Exception:
            pass

        downloaded_scripts = await download_scripts(page, Path(args.output_dir) / "scripts")
        await fill_inputs(page, args.keywords, args.delay)
        await click_visible_targets(page, args.max_clicks, args.delay)
        if not args.no_replay_api:
            await run_browser_api_replay(page, args, records, ignored_response_payloads)
        await page.wait_for_timeout(args.final_wait)
        await browser.close()

    return [record.as_dict() for record in records], downloaded_scripts


def api_data_url(page_url: str) -> str:
    parsed = urlparse(page_url)
    if parsed.scheme and parsed.netloc:
        return f"{parsed.scheme}://{parsed.netloc}/ApiData.do"
    return urljoin(page_url, "ApiData.do")


def resolve_endpoint_url(url: str, page_url: str) -> str:
    value = (url or "ApiData.do").strip().strip("'\"")
    if value in {"apiroot", "ApiData.do", "/ApiData.do"}:
        return api_data_url(page_url)
    if value.startswith(("http://", "https://")):
        return normalize_url(value)
    return normalize_url(urljoin(page_url, value))


def parse_raw_response_headers(raw_headers: str | None) -> dict[str, str]:
    headers: dict[str, str] = {}
    if not raw_headers:
        return headers
    for line in raw_headers.splitlines():
        if ":" not in line:
            continue
        key, value = line.split(":", 1)
        key = key.strip().lower()
        value = value.strip()
        if not key:
            continue
        if key in headers:
            headers[key] = f"{headers[key]}, {value}"
        else:
            headers[key] = value
    return headers


def first_value(*values: Any) -> str:
    for value in values:
        if value is None:
            continue
        text = str(value).strip()
        if text:
            return text
    return ""


def add_unique(items: list[dict[str, str]], item: dict[str, str], keys: tuple[str, ...]) -> None:
    if not any(item.get(key) for key in keys):
        return
    signature = tuple(item.get(key, "") for key in keys)
    if not any(tuple(existing.get(key, "") for key in keys) == signature for existing in items):
        items.append(item)


def iter_dicts(value: Any):
    if isinstance(value, dict):
        yield value
        for sub_value in value.values():
            yield from iter_dicts(sub_value)
    elif isinstance(value, list):
        for item in value:
            yield from iter_dicts(item)


def collect_replay_seeds(captures: list[dict[str, Any]], args: argparse.Namespace) -> dict[str, Any]:
    seeds: dict[str, Any] = {
        "citykeys": [],
        "citynames": [],
        "stations": [],
        "lines": [],
        "keywords": list(args.keywords),
        "latitude": str(args.latitude),
        "longitude": str(args.longitude),
    }
    parsed = urlparse(args.url)
    areacode = parse_qs(parsed.query).get("areacode", [""])[0]
    if areacode:
        seeds["citykeys"].append(areacode)

    for capture in captures:
        payload = repair_mojibake(capture.get("payload"))
        response_json = repair_mojibake(capture.get("response_json"))
        if isinstance(payload, dict):
            citykey = first_value(payload.get("CITYKEY"), payload.get("citykey"))
            cityname = first_value(payload.get("CITYNAME"), payload.get("cityname"))
            if citykey and citykey not in seeds["citykeys"]:
                seeds["citykeys"].append(citykey)
            if cityname and cityname not in seeds["citynames"]:
                seeds["citynames"].append(cityname)
            station = first_value(payload.get("STATIONNAME"), payload.get("stationname"))
            if station:
                add_unique(
                    seeds["stations"],
                    {
                        "name": station,
                        "lat": first_value(payload.get("MYLAT"), payload.get("LAT")),
                        "lng": first_value(payload.get("MYLNG"), payload.get("LNG")),
                    },
                    ("name", "lat", "lng"),
                )
            line = first_value(payload.get("LINENAME"), payload.get("lineName"))
            if line:
                add_unique(
                    seeds["lines"],
                    {
                        "name": line,
                        "direction": first_value(payload.get("DIRECTION"), payload.get("direction")),
                        "station_order": first_value(payload.get("STATIONORDER"), payload.get("stationOrder")),
                    },
                    ("name", "direction", "station_order"),
                )

        for item in iter_dicts(response_json):
            city = item.get("city")
            if isinstance(city, dict):
                cityname = first_value(city.get("cityname"), city.get("showName"))
                if cityname and cityname not in seeds["citynames"]:
                    seeds["citynames"].append(cityname)
            cityname = first_value(item.get("cityName"), item.get("cityname"), item.get("city"))
            if cityname and cityname not in seeds["citynames"] and cjk_count(cityname) > 0:
                seeds["citynames"].append(cityname)

            line_name = first_value(item.get("lineName"), item.get("routeName"), item.get("route"))
            if line_name:
                add_unique(
                    seeds["lines"],
                    {
                        "name": line_name,
                        "direction": first_value(item.get("upperOrDown"), item.get("direction")),
                        "station_order": first_value(item.get("stationOrder"), item.get("stationorder")),
                    },
                    ("name", "direction", "station_order"),
                )
            station_name = first_value(item.get("stationName"), item.get("stationname"), item.get("name"))
            if station_name:
                add_unique(
                    seeds["stations"],
                    {
                        "name": station_name,
                        "lat": first_value(item.get("lat"), item.get("LAT")),
                        "lng": first_value(item.get("lng"), item.get("lon"), item.get("LNG"), item.get("LON")),
                    },
                    ("name", "lat", "lng"),
                )

    for collection in (seeds["citynames"],):
        for value in collection:
            if value and value not in seeds["keywords"]:
                seeds["keywords"].append(value)
    for station in seeds["stations"][:5]:
        if station["name"] not in seeds["keywords"]:
            seeds["keywords"].append(station["name"])
    for line in seeds["lines"][:8]:
        if line["name"] not in seeds["keywords"]:
            seeds["keywords"].append(line["name"])
    return seeds


def replay_payload_candidates(seeds: dict[str, Any], cmds: list[str], per_cmd: int) -> list[dict[str, str]]:
    citykey = first_value(*seeds["citykeys"])
    cityname = first_value(*seeds["citynames"])
    stations = seeds["stations"] or [{"name": "", "lat": seeds["latitude"], "lng": seeds["longitude"]}]
    lines = seeds["lines"] or [{"name": "", "direction": "1", "station_order": "0"}]
    keywords = [item for item in seeds["keywords"] if item]
    candidates: list[dict[str, str]] = []

    for cmd in cmds:
        generated: list[dict[str, str]] = []
        if cmd == "205" and citykey:
            generated.append({"CMD": "205", "CITYKEY": citykey})
        elif cmd == "203" and cityname and citykey:
            generated.append({"CMD": "203", "CITYNAME": cityname, "CITYKEY": citykey})
        elif cmd in {"110", "114"} and cityname and citykey:
            for keyword in keywords:
                generated.append({"CMD": cmd, "CITYNAME": cityname, "KEYWORD": keyword, "CITYKEY": citykey})
        elif cmd == "119" and cityname and citykey:
            for keyword in keywords:
                generated.append(
                    {"CMD": "119", "CITYNAME": cityname, "KEYWORD": keyword, "KEY": keyword, "CITYKEY": citykey}
                )
        elif cmd == "115" and cityname and citykey:
            for station in stations:
                if station.get("name"):
                    generated.append(
                        {
                            "CMD": "115",
                            "CITYNAME": cityname,
                            "STATIONNAME": station["name"],
                            "MYLAT": station.get("lat") or seeds["latitude"],
                            "MYLNG": station.get("lng") or seeds["longitude"],
                            "ALL": "1",
                            "CITYKEY": citykey,
                        }
                    )
        elif cmd == "209" and cityname and citykey:
            for station in stations:
                if station.get("name"):
                    lat = station.get("lat") or seeds["latitude"]
                    lng = station.get("lng") or seeds["longitude"]
                    generated.append(
                        {
                            "CMD": "209",
                            "CITYNAME": cityname,
                            "STATIONNAME": station["name"],
                            "MYLAT": lat,
                            "MYLNG": lng,
                            "LAT": lat,
                            "LNG": lng,
                            "CITYKEY": citykey,
                        }
                    )
        elif cmd in {"103", "104"} and cityname and citykey:
            for line in lines:
                if not line.get("name"):
                    continue
                payload = {
                    "CMD": cmd,
                    "CITYNAME": cityname,
                    "LINENAME": line["name"],
                    "DIRECTION": line.get("direction") or "1",
                    "CITYKEY": citykey,
                }
                if cmd == "104":
                    payload["STATIONORDER"] = line.get("station_order") or "0"
                generated.append(payload)
        elif cmd == "120" and cityname:
            for line in lines:
                if not line.get("name"):
                    continue
                generated.append(
                    {
                        "CMD": "120",
                        "CITYNAME": cityname,
                        "LINELIST": json.dumps(
                            [{"lineName": line["name"], "direction": line.get("direction") or "1"}],
                            ensure_ascii=False,
                        ),
                    }
                )

        seen: set[str] = set()
        for payload in generated:
            signature = json.dumps(payload, ensure_ascii=False, sort_keys=True)
            if signature in seen:
                continue
            seen.add(signature)
            candidates.append(payload)
            if len(seen) >= per_cmd:
                break
    return candidates


async def page_ajax_post_api_data(page: Page, payload: dict[str, str], timeout_ms: int) -> CaptureRecord:
    api_url = api_data_url(page.url)
    try:
        result = await page.evaluate(
            """({ payload, timeout }) => new Promise((resolve) => {
                const $ = window.jQuery || window.$;
                if (!$ || typeof $.ajax !== 'function') {
                    resolve({
                        ok: false,
                        status: null,
                        error: 'window.$.ajax is not available',
                        responseText: '',
                        responseJSON: null,
                        responseHeaders: ''
                    });
                    return;
                }

                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: 'ApiData.do',
                    data: payload,
                    timeout,
                    success: function(data, textStatus, jqXHR) {
                        resolve({
                            ok: true,
                            status: jqXHR.status || null,
                            statusText: jqXHR.statusText || textStatus || '',
                            responseText: jqXHR.responseText || '',
                            responseJSON: data === undefined ? null : data,
                            responseHeaders: jqXHR.getAllResponseHeaders()
                        });
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        let parsed = null;
                        const text = jqXHR.responseText || '';
                        try {
                            parsed = text ? JSON.parse(text) : null;
                        } catch (_) {}
                        resolve({
                            ok: false,
                            status: jqXHR.status || null,
                            statusText: jqXHR.statusText || textStatus || '',
                            error: errorThrown || textStatus || 'ajax error',
                            responseText: text,
                            responseJSON: parsed,
                            responseHeaders: jqXHR.getAllResponseHeaders()
                        });
                    }
                });
            })""",
            {"payload": payload, "timeout": timeout_ms},
        )
    except Exception as exc:
        result = {
            "ok": False,
            "status": None,
            "error": str(exc),
            "responseText": "",
            "responseJSON": None,
            "responseHeaders": "",
        }

    response_headers = parse_raw_response_headers(result.get("responseHeaders"))
    response_text = result.get("responseText") or ""
    response_json = repair_mojibake(result.get("responseJSON"))
    if response_json is None and response_text:
        response_json = parse_json_maybe(response_text)

    return CaptureRecord(
        method="POST",
        url=api_url,
        status=result.get("status"),
        request_headers={
            "content-type": "application/x-www-form-urlencoded; charset=UTF-8",
            "x-requested-with": "XMLHttpRequest",
        },
        response_headers=response_headers,
        payload=payload,
        response_json=response_json,
        response_text="" if response_json is not None else compact_text(response_text),
        error="" if result.get("ok") else result.get("error", ""),
        page_url=page.url,
        capture_source="page_ajax_replay",
    )


async def run_browser_api_replay(
    page: Page,
    args: argparse.Namespace,
    records: list[CaptureRecord],
    ignored_response_payloads: set[str],
) -> None:
    cmds = [cmd.strip() for cmd in args.replay_cmds.split(",") if cmd.strip()]
    api_url = api_data_url(page.url)
    try:
        await page.wait_for_function(
            "() => !!((window.jQuery || window.$) && (window.jQuery || window.$).ajax)",
            timeout=5000,
        )
    except Exception:
        records.append(
            CaptureRecord(
                method="POST",
                url=api_url,
                status=None,
                request_headers={},
                response_headers={},
                payload={},
                error="window.$.ajax is not available; skipped page ajax replay",
                page_url=page.url,
                capture_source="page_ajax_replay",
            )
        )
        return

    seen_payloads = {
        json.dumps(record.payload, ensure_ascii=False, sort_keys=True, default=str)
        for record in records
        if normalize_url(record.url) == normalize_url(api_url)
    }
    for _ in range(args.replay_rounds):
        seeds = collect_replay_seeds([record.as_dict() for record in records], args)
        candidates = replay_payload_candidates(seeds, cmds, args.replay_samples_per_cmd)
        progress = False
        for payload in candidates:
            signature = json.dumps(payload, ensure_ascii=False, sort_keys=True, default=str)
            if signature in seen_payloads:
                continue
            seen_payloads.add(signature)
            ignored_response_payloads.add(signature)
            record = await page_ajax_post_api_data(page, payload, args.replay_timeout * 1000)
            if record.response_json is None and looks_unusable_encrypted_text(record.response_text):
                continue
            records.append(record)
            progress = True
            await asyncio.sleep(args.replay_delay / 1000)
        if not progress:
            break


def summarize_endpoints(
    static_endpoints: list[dict[str, Any]], captures: list[dict[str, Any]], page_url: str
) -> list[dict[str, Any]]:
    grouped: dict[str, dict[str, Any]] = {}

    for item in static_endpoints:
        url = resolve_endpoint_url(item.get("url") or "ApiData.do", page_url)
        method = (item.get("method") or "POST").upper()
        cmd = item.get("cmd") or ""
        key = f"{method} {url} CMD={cmd}" if cmd else f"{method} {url}"
        endpoint = grouped.setdefault(
            key,
            {
                "key": key,
                "method": method,
                "url": url,
                "cmd": cmd,
                "static_sources": [],
                "static_params": {},
                "captures": [],
                "response_schema": {},
                "statuses": set(),
            },
        )
        endpoint["static_sources"].append(
            {"source": item.get("source"), "line": item.get("line"), "function": item.get("function")}
        )
        endpoint["static_params"].update(item.get("params") or {})

    for capture in captures:
        method = capture.get("method", "GET").upper()
        url = normalize_url(capture.get("url", ""))
        payload = capture.get("payload")
        cmd = payload_cmd(payload)
        key = f"{method} {url} CMD={cmd}" if cmd else f"{method} {url}"
        endpoint = grouped.setdefault(
            key,
            {
                "key": key,
                "method": method,
                "url": url,
                "cmd": cmd,
                "static_sources": [],
                "static_params": {},
                "captures": [],
                "response_schema": {},
                "statuses": set(),
            },
        )
        endpoint["captures"].append(capture)
        if isinstance(payload, dict):
            for field_name in payload:
                endpoint["static_params"].setdefault(field_name, "<captured>")
        if capture.get("status") is not None:
            endpoint["statuses"].add(capture["status"])
        if capture.get("response_json") is not None:
            schema = infer_schema(capture["response_json"])
            endpoint["response_schema"] = merge_schema(endpoint.get("response_schema", {}), schema)

    result = []
    for endpoint in grouped.values():
        endpoint["statuses"] = sorted(endpoint["statuses"])
        endpoint["capture_count"] = len(endpoint["captures"])
        endpoint["sample_payloads"] = []
        seen_payloads = set()
        for capture in endpoint["captures"]:
            payload = capture.get("payload")
            payload_text = json.dumps(payload, ensure_ascii=False, sort_keys=True, default=str)
            if payload_text not in seen_payloads:
                seen_payloads.add(payload_text)
                endpoint["sample_payloads"].append(payload)
            if len(endpoint["sample_payloads"]) >= 3:
                break
        result.append(endpoint)
    return sorted(result, key=lambda item: (str(item.get("cmd") or "9999"), item["key"]))


def write_markdown(
    output_path: Path,
    url: str,
    endpoints: list[dict[str, Any]],
    static_count: int,
    capture_count: int,
) -> None:
    lines: list[str] = []
    lines.append("# H5 接口文档")
    lines.append("")
    lines.append(f"- 目标首页: `{url}`")
    lines.append(f"- 生成时间: `{time.strftime('%Y-%m-%d %H:%M:%S')}`")
    lines.append(f"- 静态接口声明: `{static_count}` 条")
    lines.append(f"- 动态请求捕获: `{capture_count}` 条")
    lines.append(f"- 合并后接口: `{len(endpoints)}` 个")
    lines.append("")
    lines.append("## 说明")
    lines.append("")
    lines.append("- `CMD` 是该站点 `ApiData.do` 的主要业务接口分发参数。")
    lines.append("- “静态参数”来自前端 JS 的 `$.ajax` 调用；变量值会保留为源码表达式。")
    lines.append(
        "- “响应结构”来自浏览器真实访问、自动点击和页面内 `$.ajax` replay 捕获到的 JSON。未触发的接口会只显示请求参数。"
    )
    lines.append("")
    lines.append("## 接口列表")
    lines.append("")
    for index, endpoint in enumerate(endpoints, start=1):
        title_cmd = f" CMD {endpoint['cmd']}" if endpoint.get("cmd") else ""
        lines.append(f"### {index}. {endpoint['method']} {endpoint['url']}{title_cmd}")
        lines.append("")
        statuses = ", ".join(str(status) for status in endpoint.get("statuses", [])) or "未动态捕获"
        lines.append(f"- 捕获次数: `{endpoint.get('capture_count', 0)}`")
        lines.append(f"- HTTP 状态: `{statuses}`")
        sources = endpoint.get("static_sources", [])
        if sources:
            source_text = "; ".join(
                f"{Path(src.get('source') or '').name}:{src.get('line')} `{src.get('function') or '-'}()`"
                for src in sources[:8]
            )
            if len(sources) > 8:
                source_text += f"; ... 共 {len(sources)} 处"
            lines.append(f"- 源码位置: {source_text}")
        params = endpoint.get("static_params", {})
        if params:
            lines.append("")
            lines.append("请求参数:")
            lines.append("")
            lines.append("| 参数 | 示例/来源表达式 |")
            lines.append("| --- | --- |")
            for key, value in sorted(params.items()):
                lines.append(f"| `{key}` | `{compact_text(str(value), 160)}` |")
        samples = endpoint.get("sample_payloads") or []
        if samples:
            lines.append("")
            lines.append("实际请求样例:")
            lines.append("")
            lines.append("```json")
            lines.append(json.dumps(samples[0], ensure_ascii=False, indent=2, default=str))
            lines.append("```")
        schema = endpoint.get("response_schema") or {}
        if schema:
            lines.append("")
            lines.append("响应结构:")
            lines.append("")
            lines.extend(markdown_schema(schema))
        else:
            lines.append("")
            lines.append("响应结构: 未在本次动态访问中捕获。")
        lines.append("")
    output_path.write_text("\n".join(lines), encoding="utf-8")


def parse_args(argv: list[str]) -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Crawl H5 API calls and generate Markdown docs.")
    parser.add_argument("--url", default=DEFAULT_URL, help="Target H5 homepage URL.")
    parser.add_argument("--output-dir", default=str(DEFAULT_OUTPUT_DIR), help="Output directory.")
    parser.add_argument("--headed", action="store_true", help="Show Chromium while crawling.")
    parser.add_argument("--max-clicks", type=int, default=80, help="Maximum visible elements to click.")
    parser.add_argument("--delay", type=int, default=700, help="Delay after actions in milliseconds.")
    parser.add_argument("--initial-wait", type=int, default=2500, help="Initial page wait in milliseconds.")
    parser.add_argument("--final-wait", type=int, default=2500, help="Final wait before closing browser.")
    parser.add_argument("--timeout", type=int, default=45000, help="Navigation timeout in milliseconds.")
    parser.add_argument("--keywords", default="1,2,101,公交,站,市中心", help="Comma-separated input keywords.")
    parser.add_argument("--width", type=int, default=390, help="Browser viewport width.")
    parser.add_argument("--height", type=int, default=844, help="Browser viewport height.")
    parser.add_argument("--latitude", type=float, default=24.8741, help="Mock geolocation latitude.")
    parser.add_argument("--longitude", type=float, default=118.6759, help="Mock geolocation longitude.")
    parser.add_argument("--timezone", default="Asia/Hong_Kong", help="Browser timezone.")
    parser.add_argument("--no-replay-api", action="store_true", help="Skip in-page $.ajax ApiData.do replay.")
    parser.add_argument(
        "--replay-cmds",
        default=",".join(COMMON_API_CMDS),
        help="Comma-separated ApiData.do CMD values to replay from captured samples.",
    )
    parser.add_argument("--replay-samples-per-cmd", type=int, default=2, help="Page $.ajax replay samples per CMD.")
    parser.add_argument("--replay-rounds", type=int, default=3, help="Seed expansion rounds for page $.ajax replay.")
    parser.add_argument("--replay-timeout", type=int, default=15, help="Page $.ajax replay request timeout in seconds.")
    parser.add_argument("--replay-delay", type=int, default=250, help="Delay between page $.ajax replay requests in ms.")
    parser.add_argument(
        "--static-js",
        nargs="*",
        default=["mybus.remote.js", "encrypt-decrypt.remote.js"],
        help="Local JS files to scan before/after browser capture.",
    )
    args = parser.parse_args(argv)
    args.keywords = [item.strip() for item in args.keywords.split(",") if item.strip()]
    return args


async def amain(argv: list[str]) -> int:
    args = parse_args(argv)
    output_dir = Path(args.output_dir)
    output_dir.mkdir(parents=True, exist_ok=True)

    local_static_paths = [Path(path) for path in args.static_js]
    captures, downloaded_scripts = await run_browser_capture(args)
    static_paths = local_static_paths + downloaded_scripts
    static_endpoints = scan_static_ajax(static_paths)
    endpoints = summarize_endpoints(static_endpoints, captures, args.url)
    replay_count = sum(1 for capture in captures if capture.get("capture_source") == "page_ajax_replay")

    (output_dir / "api-capture.json").write_text(
        json.dumps(captures, ensure_ascii=False, indent=2, default=str),
        encoding="utf-8",
    )
    (output_dir / "static-ajax.json").write_text(
        json.dumps(static_endpoints, ensure_ascii=False, indent=2, default=str),
        encoding="utf-8",
    )
    (output_dir / "api-endpoints.json").write_text(
        json.dumps(endpoints, ensure_ascii=False, indent=2, default=str),
        encoding="utf-8",
    )
    write_markdown(output_dir / "api-doc.md", args.url, endpoints, len(static_endpoints), len(captures))

    print(f"Static ajax declarations: {len(static_endpoints)}")
    print(f"Captured requests: {len(captures)}")
    print(f"Page $.ajax ApiData.do replay requests: {replay_count}")
    print(f"Merged endpoints: {len(endpoints)}")
    print(f"Wrote: {output_dir / 'api-doc.md'}")
    print(f"Wrote: {output_dir / 'api-capture.json'}")
    print(f"Wrote: {output_dir / 'static-ajax.json'}")
    print(f"Wrote: {output_dir / 'api-endpoints.json'}")
    return 0


def main() -> int:
    try:
        return asyncio.run(amain(sys.argv[1:]))
    except KeyboardInterrupt:
        return 130


if __name__ == "__main__":
    raise SystemExit(main())
