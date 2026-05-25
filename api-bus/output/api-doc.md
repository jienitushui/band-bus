# H5 接口文档

- 目标首页: `https://h5.mygolbs.com/?areacode=qz595803`
- 生成时间: `2026-05-01 13:40:03`
- 静态接口声明: `56` 条
- 动态请求捕获: `16` 条
- 合并后接口: `24` 个

## 说明

- `CMD` 是该站点 `ApiData.do` 的主要业务接口分发参数。
- “静态参数”来自前端 JS 的 `$.ajax` 调用；变量值会保留为源码表达式。
- “响应结构”来自浏览器真实访问、自动点击和页面内 `$.ajax` replay 捕获到的 JSON。未触发的接口会只显示请求参数。

## 常用接口链（附近站 → 同站站台 → 过站线路）

以下顺序与「腕上公交」快应用首页逻辑一致，便于对照实现。

| 步骤 | CMD | 作用 |
| --- | --- | --- |
| 1 | **106** | 根据用户经纬度查附近站点：`data[]` 含 `name`、`lat`、`lon`、`dis`、`sameNum` 等 |
| 2 | **209** | 对某一站名 + **106 该条站点的经纬度**，解析同名站点的**多个物理站台**（对向停靠等）；请求里 `MYLAT`/`MYLNG`/`LAT`/`LNG` 均填 **106 返回的该站 `lat`/`lon`** |
| 3 | **115** | 过站线路：`STATIONNAME`/`MYLAT`/`MYLNG` 使用 **209 当前选用站台位点**（与换站台 H5 一致），`ALL` 固定为 **`"1"`**，不传 `DIRECTION`/`LAT`/`LNG` |

## 接口列表

### 1. POST https://h5.mygolbs.com/ApiData.do CMD 101

- 捕获次数: `0`
- HTTP 状态: `未动态捕获`
- 源码位置: mybus.remote.js:4408 `getCityList()`; remote-mybus.js:8815 `getCityList()`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CMD` | `"101"` |

响应结构: 未在本次动态访问中捕获。

### 2. POST https://h5.mygolbs.com/ApiData.do CMD 102

- 捕获次数: `0`
- HTTP 状态: `未动态捕获`
- 源码位置: mybus.remote.js:560 `searchLineAnStation()`; remote-mybus.js:1119 `searchLineAnStation()`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CITYKEY` | `citykey` |
| `CITYNAME` | `cityname` |
| `CMD` | `"102"` |
| `KEYWORD` | `keyword` |

响应结构: 未在本次动态访问中捕获。

### 3. POST https://h5.mygolbs.com/ApiData.do CMD 103

- 捕获次数: `1`
- HTTP 状态: `200`
- 源码位置: mybus.remote.js:1263 `getLineDetailsNew()`; remote-mybus.js:2525 `getLineDetailsNew()`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CITYKEY` | `citykey` |
| `CITYNAME` | `cityname` |
| `CMD` | `"103"` |
| `DIRECTION` | `direction` |
| `LINENAME` | `linename` |

实际请求样例:

```json
{
  "CMD": "103",
  "CITYNAME": "泉州市",
  "LINENAME": "K606路",
  "DIRECTION": "1",
  "CITYKEY": "qz595803"
}
```

响应结构:

- `StationRelatedRouteName`: array
  - array, sample length `0`
    - unknown
- `beginTime`: string = `06:30`
- `commonts`: string = `一票制1元`
- `companys`: array
  - array, sample length `0`
    - unknown
- `data`: array
  - array, sample length `29`
    - `StationNihePointIndex`: integer = `0`
    - `arrive`: integer = `0`
    - `come`: integer = `0`
    - `showName`: string = `东宏路公交首末站`
    - `stationId`: integer = `0`
    - `stationName`: string = `东宏路公交首末站`
    - `stationOrder`: integer = `1`
    - `station_lat`: number = `24.85555999`
    - `station_lon`: number = `118.66222719`
    - `stationsStatus`: integer = `1`
- `endTime`: string = `19:10`
- `firstLast`: array
  - array, sample length `1`
    - `first`: string = `06:30`
    - `last`: string = `19:10`
- `loopType`: string = `1`
- `msg`: string = `获取数据成功！`
- `nihelist`: array
  - array, sample length `505`
    - `lat`: string = `24.855567`
    - `lng`: string = `118.662231`
- `planTime`: string = `13:45`
- `routeColor`: string = ``
- `routeId`: integer = `638`
- `routeName`: string = `K606路`
- `routeName2`: string = ``
- `showDepart`: integer = `1`
- `status`: integer = `1`
- `telephoneTips`: string = `咨询电话`
- `upperOrDown`: string = `1`

### 4. POST https://h5.mygolbs.com/ApiData.do CMD 104

- 捕获次数: `1`
- HTTP 状态: `200`
- 源码位置: mybus.remote.js:1939 `getRealTime()`; remote-mybus.js:3877 `getRealTime()`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CITYKEY` | `citykey` |
| `CITYNAME` | `cityname` |
| `CMD` | `"104"` |
| `DIRECTION` | `direction` |
| `LINENAME` | `linename` |
| `STATIONORDER` | `selectStationId` |

实际请求样例:

```json
{
  "CMD": "104",
  "CITYNAME": "泉州市",
  "LINENAME": "K606路",
  "DIRECTION": "1",
  "CITYKEY": "qz595803",
  "STATIONORDER": "9"
}
```

响应结构:

- `data`: array
  - array, sample length `3`
    - `arrive`: integer = `0`
    - `come`: integer = `1`
    - `index`: integer = `14`
    - `showType`: integer = `1`
    - `stationName`: string = `法坊路口`
- `dislist`: array
  - array, sample length `29`
    - `d`: number = `1513.53`
- `exceptionFlag`: boolean = `False`
- `hasReal`: integer = `1`
- `list`: array
  - array, sample length `3`
    - `_recTime`: integer = `1777613989305`
    - `abnormal`: string = ``
    - `angle`: number = `206.0302186078876`
    - `busNumber`: string = `闽C02767D`
    - `busRaoXingTips`: string = ``
    - `busToStationNiheDistance`: number = `120.95233147326775`
    - `busType`: string = ``
    - `bus_lat`: number = `24.879128597282172`
    - `bus_lng`: number = `118.68336470692832`
    - `crowdStatus`: string = ``
    - `index`: integer = `4`
    - `nihePointIndex`: integer = `105`
    - `personNumOnBus`: string = ``
    - `runType`: string = ``
    - `sectionStation`: string = ``
    - `stationName`: string = `市政务服务中心（海星小区）`
    - `station_lat`: number = `24.8786768`
    - `station_lng`: number = `118.68247888`
    - `statusType`: string = `2`
- `msg`: string = `获取实时数据成功!`
- `planTime`: string = `13:45`
- `plantimeSimpleVectorMain`: array
  - array, sample length `1`
    - `remarks`: string = ``
    - `stime`: string = `13:45`
- `plantimeSimpleVectorSub`: array
  - array, sample length `0`
    - unknown
- `routeOnStationRTimeInfoList`: array
  - array, sample length `1`
    - `busNumber`: string = `闽C02767D`
    - `busToStationCount`: integer = `5`
    - `busToStationDistance`: integer = `1500`
    - `busToStationDistanceTips`: string = `1.5公里`
    - `busToStationName`: string = `市政务服务中心（海星小区）`
    - `busToStationTime`: integer = `4`
    - `busToStationTimeTips`: string = `5分钟`
    - `busToStationTips`: string = `5站`
    - `frmStationName`: string = `东宏路公交首末站`
    - `planTime`: string = `13:45`
    - `routeName`: string = `K606路`
    - `routeNameDown`: string = ``
    - `routeNameUp`: string = ``
    - `stationIndex`: string = `8`
    - `stationLat`: string = `24.86951873`
    - `stationLng`: string = `118.67940281`
    - `stationName`: string = `滨海街中段`
    - `toStationName`: string = `滨江公交站`
    - `upperOrDown`: string = `1`
- `runState`: integer = `0`
- `speedlist`: array
  - array, sample length `28`
    - `co`: string = `green`
    - `speed`: integer = `450`
- `status`: integer = `1`

### 5. POST https://h5.mygolbs.com/ApiData.do CMD 105

- 捕获次数: `0`
- HTTP 状态: `未动态捕获`
- 源码位置: mybus.remote.js:2602 `getLineByStation()`; remote-mybus.js:5203 `getLineByStation()`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CITYKEY` | `citykey` |
| `CITYNAME` | `cityname` |
| `CMD` | `"105"` |
| `MYLAT` | `''` |
| `MYLNG` | `''` |
| `STATIONNAME` | `stationname` |

响应结构: 未在本次动态访问中捕获。

### 6. POST https://h5.mygolbs.com/ApiData.do CMD 106

- 捕获次数: `1`
- HTTP 状态: `200`
- 源码位置: mybus.remote.js:150 `searchNearBy()`; remote-mybus.js:299 `searchNearBy()`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CITYKEY` | `citykey` |
| `CITYNAME` | `cityname` |
| `CMD` | `"106"` |
| `LAT` | `lat` |
| `LNG` | `lng` |

实际请求样例:

```json
{
  "CITYNAME": "泉州市",
  "LAT": "24.871192",
  "LNG": "118.680447",
  "CMD": "106",
  "CITYKEY": "qz595803"
}
```

响应结构:

- `data`: array
  - array, sample length `16`
    - `dis`: integer = `213`
    - `lat`: string = `24.8695187286`
    - `lon`: string = `118.6794028137`
    - `name`: string = `滨海街中段`
    - `sameNum`: integer = `0`
- `msg`: string = `success`
- `status`: integer = `1`

> **关联 CMD 209**：若站名存在对向多个物理站台，可用本接口返回的某条 `name` + `lat` + `lon` 调 **209** 解析 `info` 后再调 **115**，见下文 **CMD 209**。

### 7. POST https://h5.mygolbs.com/ApiData.do CMD 110

- 捕获次数: `1`
- HTTP 状态: `200`
- 源码位置: mybus.remote.js:501 `searchStationByKey()`; mybus.remote.js:2874 `searchTransferPoint()`; remote-mybus.js:1001 `searchStationByKey()`; remote-mybus.js:5747 `searchTransferPoint()`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CITYKEY` | `citykey` |
| `CITYNAME` | `cityname` |
| `CMD` | `"110"` |
| `KEYWORD` | `keyword` |

实际请求样例:

```json
{
  "CMD": "110",
  "CITYNAME": "泉州市",
  "KEYWORD": "1",
  "CITYKEY": "qz595803"
}
```

响应结构:

- `busstations`: array
  - array, sample length `30`
    - `sameNameNum`: integer = `0`
    - `stationId`: integer = `0`
    - `stationName`: string = `一中`
    - `stationOrder`: integer = `0`
    - `station_lat`: integer = `0`
    - `station_lon`: integer = `0`
- `msg`: string = `success`
- `status`: integer = `1`

### 8. POST https://h5.mygolbs.com/ApiData.do CMD 112

- 捕获次数: `0`
- HTTP 状态: `未动态捕获`
- 源码位置: mybus.remote.js:3283 `searchTransferReal()`; mybus.remote.js:3351 `searchTransferReal2()`; remote-mybus.js:6565 `searchTransferReal()`; remote-mybus.js:6701 `searchTransferReal2()`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CITYKEY` | `citykey` |
| `CITYNAME` | `cityname` |
| `CMD` | `"112"` |
| `REALDIR` | `realDir` |
| `REALLINE` | `realLine` |
| `STATIONNAME` | `stationName` |
| `STATIONORDER` | `stationOrder` |

响应结构: 未在本次动态访问中捕获。

### 9. POST https://h5.mygolbs.com/ApiData.do CMD 113

- 捕获次数: `0`
- HTTP 状态: `未动态捕获`
- 源码位置: mybus.remote.js:3957 `searchTransferDetailReal2()`; mybus.remote.js:4057 `searchTransferDetailReal()`; remote-mybus.js:7913 `searchTransferDetailReal2()`; remote-mybus.js:8113 `searchTransferDetailReal()`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CITYKEY` | `citykey` |
| `CITYNAME` | `cityname` |
| `CMD` | `"113"` |
| `REALDIR` | `realDir` |
| `REALLINE` | `realLine` |
| `STATIONNAME` | `stationName` |
| `STATIONORDER` | `stationOrder` |

响应结构: 未在本次动态访问中捕获。

### 10. POST https://h5.mygolbs.com/ApiData.do CMD 114

- 捕获次数: `1`
- HTTP 状态: `200`
- 源码位置: mybus.remote.js:442 `searchLineByKey()`; remote-mybus.js:883 `searchLineByKey()`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CITYKEY` | `citykey` |
| `CITYNAME` | `cityname` |
| `CMD` | `"114"` |
| `KEYWORD` | `keyword` |

实际请求样例:

```json
{
  "CMD": "114",
  "CITYNAME": "泉州市",
  "KEYWORD": "1",
  "CITYKEY": "qz595803"
}
```

响应结构:

- `buslines`: array
  - array, sample length `314`
    - `beginTime`: string = ``
    - `cityName`: string = ``
    - `commentStationName`: string = ``
    - `comments`: string = ``
    - `distance`: integer = `0`
    - `endTime`: string = ``
    - `from`: string = `东宏路公交首末站`
    - `lineName`: string = `1路`
    - `neardis`: string = ``
    - `nearnum`: integer = `0`
    - `neartext`: string = ``
    - `neartime`: string = ``
    - `nexttext`: string = ``
    - `number`: string = ``
    - `routeBean`: object
      - `cityName`: string = `泉州市`
      - `comments`: string = `一票制1元`
      - `companies`: array
        - array, sample length `0`
          - unknown
      - `company`: string = `公交集团东海分公司`
      - `companyMobile`: string = ``
      - `departCar`: integer = `1`
      - `departCarCount`: integer = `1`
      - `downAllStationsLat`: string = ``
      - `downAllStationsLng`: string = ``
      - `downAllStationsName`: string = ``
      - `downAllStationsStatus`: string = ``
      - `downStationStatusList`: array
        - array, sample length `0`
          - unknown
      - `downTunnelEntryOutInfos`: array
        - array, sample length `0`
          - unknown
      - `downTunnelIds`: array
        - array, sample length `0`
          - unknown
      - `edTime`: string = `22:00`
      - `euTime`: string = `22:00`
      - `exceptionCode`: integer = `0`
      - `exceptionMsg`: string = ``
      - `exceptionTip`: string = ``
      - `exceptionUrl`: string = ``
      - `externalIds`: array
        - array, sample length `0`
          - unknown
      - `hasReal`: string = `1`
      - `hideBusNumber`: string = `0`
      - `id`: integer = `436`
      - `interval`: string = ``
      - `intervalDown`: integer = `-1`
      - `intervalUp`: integer = `-1`
      - `loopType`: string = `1`
      - `regBusCount`: string = ``
      - `routeAlias`: string = ``
      - `routeArea`: string = ``
      - `routeColor`: string = ``
      - `routeImage`: string = ``
      - `routeLevel`: string = `1`
      - `routeName`: string = `1路`
      - `routeNameDown`: string = ``
      - `routeNameUp`: string = ``
      - `routeNumber2`: string = ``
      - `routeSpeed`: integer = `0`
      - `routeType`: string = `1`
      - `runState`: integer = `0`
      - `runStateTemp`: integer = `0`
      - `runType`: string = `1`
      - `sdTime`: string = `06:55`
      - `sedtimes`: array
        - array, sample length `1`
          - `first`: string = `06:55`
          - `last`: string = `22:00`
      - `sedtimesOld`: array
        - array, sample length `0`
          - unknown
      - `seutimes`: array
        - array, sample length `1`
          - `first`: string = `06:20`
- ...

### 11. POST https://h5.mygolbs.com/ApiData.do CMD 115

- 捕获次数: `2`
- HTTP 状态: `200`
- 源码位置: mybus.remote.js:312 `searchNearLine()`; mybus.remote.js:2659 `getLineByStationV2()`; remote-mybus.js:623 `searchNearLine()`; remote-mybus.js:5317 `getLineByStationV2()`

#### 腕上公交首页 / 换站台（与 209 联动）

首页在 **106 → 209** 之后请求 **115** 时，与 H5「换站台」一致，仅传以下字段（`ALL` 固定为 `"1"`）：

| 参数 | 说明 |
| --- | --- |
| `CMD` | `"115"` |
| `CITYNAME` / `CITYKEY` | 城市 |
| `STATIONNAME` | **209** 当前选用 `info` 条目的 `name`（与站名一致） |
| `MYLAT` / `MYLNG` | **209** 当前选用条目的 `lat` / `lon`（字符串） |
| `ALL` | 固定 **`"1"`** |

```json
{
  "CMD": "115",
  "CITYNAME": "泉州市",
  "STATIONNAME": "真武庙",
  "MYLAT": "24.8793611925",
  "MYLNG": "118.6233539060",
  "ALL": "1",
  "CITYKEY": "qz595803"
}
```

#### H5 捕获参数（历史样例，含 ALL=0 等）

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `ALL` | `1` |
| `CITYKEY` | `citykey` |
| `CITYNAME` | `cityname` |
| `CMD` | `"115"` |
| `MYLAT` | `lat` |
| `MYLNG` | `lng` |
| `STATIONNAME` | `stationname` |

实际请求样例:

```json
{
  "CMD": "115",
  "CITYNAME": "泉州市",
  "STATIONNAME": "滨海街中段",
  "MYLAT": "24.8695187286",
  "MYLNG": "118.6794028137",
  "ALL": "0",
  "CITYKEY": "qz595803"
}
```

响应结构:

- `data`: array
  - array, sample length `4`
    - object | unknown
- `msg`: string = `获取经过该站点的线路列表成功`
- `status`: integer = `1`
- `type`: integer = `0`

### 12. POST https://h5.mygolbs.com/ApiData.do CMD 116

- 捕获次数: `0`
- HTTP 状态: `未动态捕获`
- 源码位置: mybus.remote.js:4948 `getDepart()`; remote-mybus.js:9895 `getDepart()`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CITYKEY` | `""` |
| `CITYNAME` | `cityname` |
| `CMD` | `"116"` |
| `DIRECTION` | `upperOrDown` |
| `ROUTEID` | `routeId` |

响应结构: 未在本次动态访问中捕获。

### 13. POST https://h5.mygolbs.com/ApiData.do CMD 117

- 捕获次数: `0`
- HTTP 状态: `未动态捕获`
- 源码位置: mybus.remote.js:5194 `searchBusLineByStationQRCodeNew()`; remote-mybus.js:10387 `searchBusLineByStationQRCodeNew()`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CITYCODE` | `citycode` |
| `CMD` | `"117"` |
| `STATIONID` | `stationid` |

响应结构: 未在本次动态访问中捕获。

### 14. POST https://h5.mygolbs.com/ApiData.do CMD 119

- 捕获次数: `1`
- HTTP 状态: `200`
- 源码位置: mybus.remote.js:641 `searchAllLine()`; mybus.remote.js:789 `searchAllLineLeShan()`; mybus.remote.js:870 `searchAllLineXiJiu()`; remote-mybus.js:1281 `searchAllLine()`; remote-mybus.js:1577 `searchAllLineLeShan()`; remote-mybus.js:1739 `searchAllLineXiJiu()`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CITYKEY` | `"xijiu101401"` |
| `CITYNAME` | `"习酒通勤"` |
| `CMD` | `"119"` |
| `KEY` | `""` |
| `KEYWORD` | `keyword` |

实际请求样例:

```json
{
  "CMD": "119",
  "CITYNAME": "泉州市",
  "KEYWORD": "1",
  "KEY": "1",
  "CITYKEY": "qz595803"
}
```

响应结构:

- `buslines`: array
  - array, sample length `314`
    - `beginTime`: string = ``
    - `cityName`: string = ``
    - `commentStationName`: string = ``
    - `comments`: string = ``
    - `company`: string = `公交集团东海分公司`
    - `distance`: integer = `0`
    - `endTime`: string = ``
    - `from`: string = `东宏路公交首末站`
    - `lineName`: string = `1路`
    - `neardis`: string = ``
    - `nearnum`: integer = `0`
    - `neartext`: string = ``
    - `neartime`: string = ``
    - `nexttext`: string = ``
    - `number`: string = ``
    - `routeNameDown`: string = ``
    - `routeNameUp`: string = ``
    - `shortName`: integer = `1`
    - `stationID`: string = ``
    - `stationName`: string = ``
    - `stationOrder`: integer = `0`
    - `to`: string = `泉州外国语学校`
    - `upperOrDown`: string = `1`
- `busstations`: array
  - array, sample length `0`
    - unknown
- `msg`: string = `success`
- `status`: integer = `1`

### 15. POST https://h5.mygolbs.com/ApiData.do CMD 120

- 捕获次数: `1`
- HTTP 状态: `200`
- 源码位置: mybus.remote.js:1749 `getLineSpecialNotice()`; mybus.remote.js:1794 `getLineSpecialNotice()`; mybus.remote.js:1836 `getLineSpecialNoticeByLines()`; remote-mybus.js:3497 `getLineSpecialNotice()`; remote-mybus.js:3587 `getLineSpecialNotice()`; remote-mybus.js:3671 `getLineSpecialNoticeByLines()`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CITYNAME` | `cityname` |
| `CMD` | `"120"` |
| `LINELIST` | `lineliststr` |

实际请求样例:

```json
{
  "CMD": "120",
  "CITYNAME": "泉州市",
  "LINELIST": "[{\"lineName\": \"K606路\", \"direction\": \"1\"}]"
}
```

响应结构:

- `info`: array
  - array, sample length `0`
    - unknown
- `msg`: string = `success`
- `serverTime`: string = `20260501134001`
- `status`: integer = `1`

### 16. POST https://h5.mygolbs.com/ApiData.do CMD 203

- 捕获次数: `1`
- HTTP 状态: `200`
- 源码位置: mybus.remote.js:4562 `getNews()`; mybus.remote.js:4605 `getNewsList()`; remote-mybus.js:9123 `getNews()`; remote-mybus.js:9209 `getNewsList()`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CITYKEY` | `citykey` |
| `CITYNAME` | `cityname` |
| `CMD` | `"203"` |

实际请求样例:

```json
{
  "CMD": "203",
  "CITYNAME": "泉州市",
  "CITYKEY": "qz595803"
}
```

响应结构:

- `data`: array
  - array, sample length `11`
    - `city`: string = `泉州市`
    - `clickurl`: string = `http://quanguo.mygolbs.com:8081/MyBusServer/news.jsp?cityCode=059500&newsId=814987`
    - `content`: string = ``
    - `date`: string = `2026-04-30`
    - `id`: integer = `0`
    - `imgurl`: string = ``
    - `shortTitle`: string = ``
    - `title`: string = `关于五一期间泉州公交部分线路增加班次及延时的公告`
    - `type`: string = `公告`
- `status`: integer = `1`

### 17. POST https://h5.mygolbs.com/ApiData.do CMD 204

- 捕获次数: `0`
- HTTP 状态: `未动态捕获`
- 源码位置: mybus.remote.js:4678 `cityInfo()`; remote-mybus.js:9355 `cityInfo()`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CITYNAME` | `cityname` |
| `CMD` | `"204"` |

响应结构: 未在本次动态访问中捕获。

### 18. POST https://h5.mygolbs.com/ApiData.do CMD 205

- 捕获次数: `1`
- HTTP 状态: `200`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CITYKEY` | `<captured>` |
| `CMD` | `<captured>` |

实际请求样例:

```json
{
  "CMD": "205",
  "CITYKEY": "qz595803"
}
```

响应结构:

- `city`: object
  - `cityname`: string = `泉州市`
  - `company`: string = ``
  - `download`: string = `0`
  - `logo`: string = ``
  - `nearStation`: string = `1`
  - `showName`: string = `泉州`
- `msg`: string = `success`
- `status`: integer = `1`

### 19. POST https://h5.mygolbs.com/ApiData.do CMD 207

- 捕获次数: `0`
- HTTP 状态: `未动态捕获`
- 源码位置: mybus.remote.js:5040 `getDepartV2()`; remote-mybus.js:10079 `getDepartV2()`

请求参数:

| 参数 | 示例/来源表达式 |
| --- | --- |
| `CITYKEY` | `""` |
| `CITYNAME` | `cityName` |
| `CMD` | `"207"` |
| `DIRECTION` | `uod` |
| `ROUTEID` | `routeId` |

响应结构: 未在本次动态访问中捕获。

### 20. POST https://h5.mygolbs.com/ApiData.do CMD 209

- 捕获次数: `1`
- HTTP 状态: `200`
- 源码位置: mybus.remote.js:2736 `searchStation()`; remote-mybus.js:5471 `searchStation()`

#### 业务含义

用于「**同名站点、多个物理站台**」场景（例如对向各有一个同名站牌）。**请求坐标一律使用 CMD 106 返回的该站 `lat` / `lon`**（与 106 列表中选中条一致）；响应 `info` 为多条时，每条为同一 `name` 下的一个站台位点，经纬度不同，可与上下行或站台侧别对应。后续 **CMD 115** 查询过站线路时，建议按当前方向选用 `info` 中对应条目的坐标作为 `MYLAT`/`MYLNG`（及 `LAT`/`LNG`），预报更准确。

#### 请求参数

| 参数 | 必填 | 说明 |
| --- | --- | --- |
| `CMD` | 是 | 固定 `"209"` |
| `CITYNAME` | 是 | 城市名，如 `泉州市` |
| `CITYKEY` | 是 | 城市 key，如 `qz595803` |
| `STATIONNAME` | 是 | 站名，与 **106** 返回的 `name` 一致 |
| `MYLAT` | 是 | 与 **106 该站** 的 `lat` 相同 |
| `MYLNG` | 是 | 与 **106 该站** 的 `lon`（或 `lng`）相同 |
| `LAT` | 是 | 与 `MYLAT` 相同（与 H5 捕获一致） |
| `LNG` | 是 | 与 `MYLNG` 相同 |

#### 实际请求样例（与 106 某条站点对齐）

```json
{
  "CMD": "209",
  "CITYNAME": "泉州市",
  "STATIONNAME": "真武庙",
  "MYLAT": "24.8793611925",
  "MYLNG": "118.6233539060",
  "LAT": "24.8793611925",
  "LNG": "118.6233539060",
  "CITYKEY": "qz595803"
}
```

#### 响应样例（同站两个方向 / 两个站台位点）

```json
{
  "status": 1,
  "msg": "success",
  "info": [
    {
      "name": "真武庙",
      "lat": "24.8793611925",
      "lon": "118.6233539060",
      "dis": 0,
      "sameNum": 2
    },
    {
      "name": "真武庙",
      "lat": "24.8797363370",
      "lon": "118.6224370780",
      "dis": 101,
      "sameNum": 2
    }
  ],
  "serverTime": "20260514150642"
}
```

#### 响应字段说明

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `status` | number | `1` 表示成功 |
| `msg` | string | 提示信息，如 `success` |
| `serverTime` | string | 服务端时间串 |
| `info` | array | **站台位点列表**；多条时同名、不同 `lat`/`lon`；`dis` 常与请求点距离有关；`sameNum` 可表示同名站台数量等（以服务端为准） |

#### 客户端使用建议

1. 先 **106** 得到附近站列表，用户或逻辑选定一条站 `name` + `lat`/`lon`。
2. 用该条坐标调 **209**，将 `info` **按 `dis` 升序**排序后：通常 `dis` 最小的一条更贴近当前 106 选中点；**上行/下行**与 `info[0]`、`info[1]` 的对应关系以实现为准（常见做法：`lineDirection` 为 `1` 用首条、`2` 用第二条，仅当 `info.length >= 2` 时切换）。
3. 再调 **115**（首页/换站台）：`STATIONNAME` 与 `MYLAT`/`MYLNG` 取 **209** 当前方向对应 `info` 条；`ALL` 固定 **`"1"`**，不传 `LAT`/`LNG`/`DIRECTION`（与腕上公交实现一致）。

> 注：早期自动捕获里 `STATIONNAME` 曾出现 `"[]"` 占位，实际业务须传真实站名字符串。

### 21. GET blob://https://h5.mygolbs.com/0d60903c-785e-417d-a544-1c833a0e7a73

- 捕获次数: `1`
- HTTP 状态: `200`

实际请求样例:

```json
{}
```

响应结构: 未在本次动态访问中捕获。

### 22. GET blob://https://h5.mygolbs.com/7cbfc84c-a243-4420-b4a5-a4661daa8d06

- 捕获次数: `1`
- HTTP 状态: `200`

实际请求样例:

```json
{}
```

响应结构: 未在本次动态访问中捕获。

### 23. GET https://restapi.amap.com/v3/assistant/coordinate/convert?coordsys=gps&output=json&s=rsv3&locations=118.67590000000001,24.8741&key=45bf007bbf84b9982e721aa5bd259b1f&callback=jsonp_976087_&platform=JS&logversion=2.0&appname=https%3A%2F%2Fh5.mygolbs.com%2F&csid=2EA9C301-5348-4D1A-821F-48AD0A24E0A9&sdkversion=1.4.30

- 捕获次数: `1`
- HTTP 状态: `200`

实际请求样例:

```json
{}
```

响应结构:

- `info`: string = `ok`
- `infocode`: string = `10000`
- `locations`: string = `118.680446777344,24.871191948785`
- `status`: string = `1`

### 24. GET https://restapi.amap.com/v3/geocode/regeo?key=45bf007bbf84b9982e721aa5bd259b1f&s=rsv3&language=zh_cn&location=118.680447,24.871192&extensions=base&callback=jsonp_894703_&platform=JS&logversion=2.0&appname=https%3A%2F%2Fh5.mygolbs.com%2F&csid=30E0E129-8097-496A-B695-DCEEA8441BA7&sdkversion=1.4.30

- 捕获次数: `1`
- HTTP 状态: `200`

实际请求样例:

```json
{}
```

响应结构:

- `info`: string = `OK`
- `infocode`: string = `10000`
- `regeocode`: object
  - `addressComponent`: object
    - `adcode`: string = `350503`
    - `building`: object
      - `name`: array
        - array, sample length `0`
          - unknown
      - `type`: array
        - array, sample length `0`
          - unknown
    - `businessAreas`: array
      - array, sample length `1`
        - array, sample length `0`
          - unknown
    - `city`: string = `泉州市`
    - `citycode`: string = `0595`
    - `country`: string = `中国`
    - `district`: string = `丰泽区`
    - `neighborhood`: object
      - `name`: array
        - array, sample length `0`
          - unknown
      - `type`: array
        - array, sample length `0`
          - unknown
    - `province`: string = `福建省`
    - `streetNumber`: object
      - `direction`: string = `东南`
      - `distance`: string = `56.6204`
      - `location`: string = `118.680943,24.870954`
      - `number`: string = `222号`
      - `street`: string = `滨海街`
    - `towncode`: string = `350503007000`
    - `township`: string = `东海街道`
  - `formatted_address`: string = `福建省泉州市丰泽区东海街道一峰街香缤中心`
- `status`: string = `1`
