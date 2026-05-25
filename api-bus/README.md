# H5 API Crawler

This tool opens the target H5 page in Chromium, records XHR/fetch/JSON responses, lightly explores visible click targets, replays `ApiData.do` through the page's own `$.ajax`, scans linked JavaScript for jQuery AJAX calls, and writes API documentation.

## Install

```powershell
python -m pip install -r requirements.txt
python -m playwright install chromium
```

## Run

```powershell
python crawl_h5_apis.py --url "https://h5.mygolbs.com/?areacode=qz595803" --headed
```

Outputs are written to `output/api-doc.md`, `output/api-capture.json`, and `output/static-ajax.json` by default.

## Run the demo page

`index.html` must be opened through the local proxy server, otherwise `/ApiData.do` will return `405 Method Not Allowed` from a plain static server.

```powershell
python dev_server.py
```

Then open:

```text
http://127.0.0.1:8080/index.html
```

On Windows you can also double-click `start_demo.bat`.

## Notes

- The crawler is intentionally gentle: it does not perform load testing and it clicks slowly.
- `ApiData.do` replay intentionally runs inside the loaded page via `$.ajax`; out-of-page POST replay is not used because it can return unusable encrypted bodies.
- Increase coverage with `--max-clicks 80 --keywords "1,2,101"`.
- If text looks garbled in PowerShell, run `chcp 65001` before viewing output, or open the generated Markdown in an UTF-8 aware editor.
