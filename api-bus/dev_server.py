from http.server import SimpleHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
from urllib.request import Request, urlopen


API_URL = "https://h5.mygolbs.com/ApiData.do"
ROOT = Path(__file__).resolve().parent


class Handler(SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=str(ROOT), **kwargs)

    def do_OPTIONS(self):
        self.send_response(204)
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Access-Control-Allow-Methods", "POST, GET, OPTIONS")
        self.send_header("Access-Control-Allow-Headers", "Content-Type, X-Requested-With")
        self.end_headers()

    def do_POST(self):
        if self.path.split("?", 1)[0] != "/ApiData.do":
            self.send_error(404)
            return

        length = int(self.headers.get("Content-Length", "0"))
        body = self.rfile.read(length)
        request = Request(
            API_URL,
            data=body,
            method="POST",
            headers={
                "Content-Type": self.headers.get("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"),
                "Origin": "https://h5.mygolbs.com",
                "Referer": "https://h5.mygolbs.com/?areacode=qz595803",
                "X-Requested-With": "XMLHttpRequest",
                "User-Agent": self.headers.get("User-Agent", "Mozilla/5.0"),
            },
        )

        try:
            with urlopen(request, timeout=20) as response:
                payload = response.read()
                self.send_response(response.status)
                self.send_header("Content-Type", response.headers.get("Content-Type", "application/json; charset=UTF-8"))
                self.send_header("Access-Control-Allow-Origin", "*")
                self.end_headers()
                self.wfile.write(payload)
        except Exception as exc:
            message = str(exc).encode("utf-8", errors="replace")
            self.send_response(502)
            self.send_header("Content-Type", "text/plain; charset=UTF-8")
            self.end_headers()
            self.wfile.write(message)


if __name__ == "__main__":
    server = ThreadingHTTPServer(("127.0.0.1", 8080), Handler)
    print("Serving on http://127.0.0.1:8080/index.html")
    server.serve_forever()
