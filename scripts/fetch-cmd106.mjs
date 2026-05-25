/**
 * 调用与手表 App 相同的「附近站点」接口（CMD=106），便于对比返回字段与手表侧展示。
 *
 * 用法:
 *   node scripts/fetch-cmd106.mjs
 *   node scripts/fetch-cmd106.mjs 24.884134 118.622254
 *
 * 需要 Node.js 18+（内置 fetch）。
 */

const API_URL = 'https://h5.mygolbs.com/ApiData.do';
const CITY_NAME = '泉州市';
const CITY_KEY = 'qz595803';

/** 默认使用你提供的 phone_location 示例坐标 */
const DEFAULT_LAT = '24.884134';
const DEFAULT_LNG = '118.622254';

function buildBody(lat, lng) {
  const params = {
    CMD: '106',
    CITYNAME: CITY_NAME,
    CITYKEY: CITY_KEY,
    LAT: String(lat),
    LNG: String(lng)
  };
  return Object.keys(params)
    .map((k) => `${encodeURIComponent(k)}=${encodeURIComponent(params[k])}`)
    .join('&');
}

async function main() {
  const lat = process.argv[2] ?? DEFAULT_LAT;
  const lng = process.argv[3] ?? DEFAULT_LNG;
  const body = buildBody(lat, lng);

  console.log('POST', API_URL);
  console.log('Body (decoded):', Object.fromEntries(new URLSearchParams(body)));
  console.log('---\n');

  const res = await fetch(API_URL, {
    method: 'POST',
    headers: {
      'User-Agent': 'Mozilla/5.0',
      Accept: 'application/json, text/javascript, */*; q=0.01',
      'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
      'X-Requested-With': 'XMLHttpRequest',
      Referer: 'https://h5.mygolbs.com/?areacode=qz595803',
      Origin: 'https://h5.mygolbs.com'
    },
    body
  });

  const text = await res.text();
  let parsed;
  try {
    parsed = JSON.parse(text);
  } catch {
    parsed = null;
  }

  console.log('HTTP', res.status, res.statusText);
  if (parsed) {
    console.log(JSON.stringify(parsed, null, 2));

    // 直连接口时多为 { data: Station[], status, msg }；若经网关再包一层则可能是 data.data
    const raw = parsed.data;
    const list = Array.isArray(raw)
      ? raw
      : raw && Array.isArray(raw.data)
        ? raw.data
        : null;
    if (list && list.length) {
      console.log(`\n--- 站点列表共 ${list.length} 条；首条原始字段 ---`);
      console.log(Object.keys(list[0]).sort().join(', '));
      console.log('\n首条站点:');
      console.log(JSON.stringify(list[0], null, 2));
    } else if (raw && typeof raw === 'object' && !Array.isArray(raw)) {
      console.log('\n--- parsed.data 顶层键（非数组） ---');
      console.log(Object.keys(raw).join(', '));
    }
  } else {
    console.log('(非 JSON 响应，前 2000 字符)');
    console.log(text.slice(0, 2000));
  }
}

main().catch((e) => {
  console.error(e);
  process.exit(1);
});
