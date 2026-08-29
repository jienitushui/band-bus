package uno.keyin.bus

import org.json.JSONObject
import org.json.JSONArray
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BusApiClientParserTest {
    @Test
    fun parseSearchResults_readsLinesAndStations() {
        val json = JSONObject(
            """{
                "status":1,
                "buslines":[{"lineName":"K1路","from":"泉州站","to":"客运中心站","upperOrDown":"2"}],
                "busstations":[{"stationName":"泉州站"}]
            }""",
        )

        val results = BusApiClient.parseSearchResults(json, includeLines = true, includeStations = true)

        assertEquals(2, results.size)
        assertEquals(SearchResultType.LINE, results[0].type)
        assertEquals("K1路", results[0].lineName)
        assertEquals("泉州站 → 客运中心站", results[0].description)
        assertEquals("2", results[0].direction)
        assertEquals(SearchResultType.STATION, results[1].type)
        assertEquals("泉州站", results[1].name)
    }

    @Test
    fun mergeSearchResults_removesDuplicateTypeAndName() {
        val values = listOf(
            SearchResult(SearchResultType.LINE, "1路", "A", "1路"),
            SearchResult(SearchResultType.LINE, "1路", "B", "1路"),
            SearchResult(SearchResultType.STATION, "1路", "C"),
        )

        val merged = BusApiClient.mergeSearchResults(values)

        assertEquals(2, merged.size)
        assertEquals("A", merged[0].description)
        assertEquals(SearchResultType.STATION, merged[1].type)
    }

    @Test
    fun parseModernTransfer_mapsOriginStopsAndMultipleSegments() {
        val json = JSONObject(
            """{
                "status":1,
                "info":[{
                    "startName":"我的位置","endName":"滨海街中段",
                    "upStation":"错误的全局上车站","downStation":"错误的全局下车站",
                    "stationNum":"3站","totalTime":"16分钟","totalWalkDistance":"240米",
                    "startWalkDistance":"100米","firstWalkDistance":"223米","endWalkDistance":"80米",
                    "lines":[
                        {"lineNames":"61路#K607路#14路","dirs":"2#2#2","orders":"3#37#21","stations":"府东路南段#府东路南段#府东路南段"},
                        {"lineNames":"60路","stations":"市民广场"}
                    ],
                    "routeUpDownSimple1":[
                        {"routeName":"61路","endStationName":"公交总站","upStationName":"府东路南段","downStationName":"蔡塘站（BRT）","upStationIndex":3,"downStationIndex":8},
                        {"routeName":"K607路","endStationName":"客运中心","upStationName":"府东路南段","downStationName":"蔡塘站（BRT）","upStationIndex":2,"downStationIndex":7},
                        {"routeName":"14路","endStationName":"市民广场","upStationName":"府东路南段","downStationName":"蔡塘站（BRT）","upStationIndex":4,"downStationIndex":9}
                    ],
                    "routeUpDownSimple2":[
                        {"routeName":"60路","endStationName":"软件园","upStationName":"蔡塘广场2站","downStationName":"晋光小学东海校区","upStationIndex":10,"downStationIndex":16}
                    ]
                }]
            }""",
        )

        val scheme = BusApiClient.parseModernTransfer(json, "请求起点", "请求终点").single()

        assertEquals("我的位置", scheme.startStation)
        assertEquals("滨海街中段", scheme.endStation)
        assertEquals("府东路南段", scheme.boardingStation)
        assertEquals("晋光小学东海校区", scheme.alightingStation)
        assertEquals("61路 / K607路 / 14路", scheme.startLine)
        assertEquals("蔡塘广场2站", scheme.changeStation)
        assertEquals("60路", scheme.endLine)
        assertEquals("16分钟", scheme.totalTime)
        assertEquals("240米", scheme.walkDistance)
        assertEquals("3站", scheme.stationCount)
        assertEquals(listOf("61路 / K607路 / 14路", "60路"), scheme.lineSegments)
        assertEquals("61路#K607路#14路", scheme.realtimeLine)
        assertEquals("100米", scheme.startWalkDistance)
        assertEquals("80米", scheme.endWalkDistance)
        assertEquals(2, scheme.legs.size)
        assertEquals(3, scheme.legs[0].options.size)
        assertEquals("蔡塘站（BRT）", scheme.legs[0].alightStation)
        assertEquals("223米", scheme.legs[0].walkAfterDistance)
        assertEquals("蔡塘广场2站", scheme.legs[1].boardStation)
        assertEquals(6, scheme.legs[1].options.single().stationCount)
        assertTrue(scheme.routeSummary().startsWith("我的位置 → 府东路南段"))
        assertTrue(scheme.routeSummary().endsWith("晋光小学东海校区 → 滨海街中段"))
    }

    @Test
    fun parseModernTransfer_keepsEveryReturnedScheme() {
        val info = JSONArray()
        repeat(10) { index ->
            info.put(JSONObject().apply {
                put("startName", "起点$index")
                put("endName", "终点$index")
                put("lines", JSONArray().put(JSONObject().put("lineNames", "${index + 1}路")))
            })
        }
        val json = JSONObject().put("status", 1).put("info", info)

        val schemes = BusApiClient.parseModernTransfer(json, "请求起点", "请求终点")

        assertEquals(10, schemes.size)
        assertEquals("起点9", schemes.last().startStation)
        assertEquals("10路", schemes.last().startLine)
    }

    @Test
    fun parseLineDetail_readsScheduleAndOrderedStations() {
        val json = JSONObject(
            """{
                "status":1,"routeName":"K606路","beginTime":"06:30","endTime":"19:10",
                "commonts":"一票制1元","firstLast":[{"first":"06:35","last":"19:05"}],
                "data":[
                    {"stationOrder":1,"showName":"东宏路公交首末站"},
                    {"stationOrder":2,"stationName":"法坊路口"}
                ]
            }""",
        )

        val detail = BusApiClient.parseLineDetail(json, "K606", "1")

        assertEquals("K606路", detail.lineName)
        assertEquals("东宏路公交首末站", detail.from)
        assertEquals("法坊路口", detail.to)
        assertEquals("06:35", detail.firstTime)
        assertEquals("19:05", detail.lastTime)
        assertEquals("一票制1元", detail.comment)
        assertEquals(listOf(1, 2), detail.stations.map { it.order })
    }

    @Test
    fun parseLineDetail_usesFallbackFieldsAndSkipsBlankStations() {
        val json = JSONObject(
            """{
                "status":1,"beginTime":"07:00","endTime":"20:00",
                "data":[{"stationOrder":1,"showName":""},{"stationOrder":2,"stationName":"终点站"}]
            }""",
        )

        val detail = BusApiClient.parseLineDetail(json, "测试线", "2")

        assertEquals("测试线", detail.lineName)
        assertEquals("终点站", detail.from)
        assertEquals("终点站", detail.to)
        assertEquals("07:00", detail.firstTime)
        assertEquals("20:00", detail.lastTime)
        assertTrue(detail.comment.isEmpty())
    }

    @Test
    fun parseLineRealtime_mapsVehicleMarkersAndEta() {
        val json = JSONObject(
            """{
                "status":1,"planTime":"13:45",
                "list":[
                    {"index":4,"statusType":"2","busNumber":"闽C02767D"},
                    {"index":8,"statusType":"0","busNumber":"闽C02790D"}
                ],
                "routeOnStationRTimeInfoList":[{"busToStationTips":"5站","busToStationTimeTips":"5分钟"}]
            }""",
        )

        val realtime = BusApiClient.parseLineRealtime(json)

        assertEquals("5站 · 5分钟", realtime.etaText)
        assertEquals("13:45", realtime.planTime)
        assertEquals(listOf(4, 9), realtime.vehicles.map { it.stationOrder })
        assertTrue(!realtime.vehicles[0].arrived)
        assertTrue(realtime.vehicles[1].arrived)
    }
}
