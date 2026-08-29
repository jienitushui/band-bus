package uno.keyin.bus

object RealtimeLineMatcher {
    fun find(lines: List<BusLineUi>, target: RealtimeWatchTarget): BusLineUi? {
        val expected = normalizeLineId(target.lineName)
        return lines.firstOrNull {
            normalizeLineId(it.id) == expected && it.directionCode == target.directionCode
        } ?: lines.firstOrNull {
            normalizeLineId(it.id) == expected && it.direction == target.direction
        } ?: lines.firstOrNull { normalizeLineId(it.id) == expected }
    }

    internal fun normalizeLineId(value: String): String = value
        .replace(Regex("\\s+"), "")
        .removeSuffix("路")
        .uppercase()
}
