package uno.keyin.bus

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RealtimeBusTest {
    private val target = RealtimeWatchTarget(
        cityName = "泉州市",
        cityKey = "qz595803",
        stationName = "真武庙",
        stationLat = "24.1",
        stationLng = "118.1",
        lineName = "K606路",
        direction = "泉州站",
        directionCode = "2",
        stationOrder = 9,
        platformLabel = "2号站台",
        reminderEnabled = true,
        addedAt = 123L,
    )

    @Test
    fun lineMatcher_prefersDirectionCodeThenDirection() {
        val lines = listOf(
            BusLineUi(" K606 ", "错误方向", "3分钟", directionCode = "1"),
            BusLineUi("K606路", "泉州站", "5分钟", directionCode = "2"),
        )

        assertEquals("5分钟", RealtimeLineMatcher.find(lines, target)?.statusMain)
        assertNull(RealtimeLineMatcher.find(listOf(BusLineUi("8路", "泉州站", "1分钟")), target))
    }

    @Test
    fun watchTargetCodec_preservesPhysicalPlatform() {
        val encoded = RealtimeWatchStore.encodeTargets(listOf(target))
        val decoded = RealtimeWatchStore.decodeTargets(encoded).single()

        assertEquals(target, decoded)
        assertEquals("2号站台", decoded.platformLabel)
        assertEquals("泉州市|真武庙|24.1|118.1|K606路|2", decoded.key)
    }

    @Test
    fun watchTargetCodec_skipsIncompleteRows() {
        val values = RealtimeWatchStore.decodeTargets(
            """[{"stationName":"","lineName":"K1路"},{"stationName":"站点","lineName":""}]""",
        )

        assertEquals(emptyList<RealtimeWatchTarget>(), values)
    }

    @Test
    fun mergeTarget_upgradesLegacyCoordinatesEvenAtLimit() {
        val legacy = target.copy(platformLabel = "", stationLat = "24.0", stationLng = "118.0")
        val otherTargets = (1 until RealtimeWatchStore.MAX_TARGETS).map { index ->
            target.copy(lineName = "${index}路", directionCode = "1", addedAt = index.toLong())
        }

        val merged = RealtimeWatchStore.mergeTarget(listOf(legacy) + otherTargets, target)

        assertEquals(RealtimeWatchStore.MAX_TARGETS, merged?.size)
        assertEquals(1, merged?.count { it.key == target.key })
        assertEquals(0, merged?.count { it.key == legacy.key })
    }
}
