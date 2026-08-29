package uno.keyin.bus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uno.keyin.bus.databinding.ItemStationCardBinding

data class BusLineUi(
    val id: String,
    val direction: String,
    val statusMain: String,
    val statusSub: String? = null,
    val directionCode: String = "1",
    val stationOrder: Int = 0,
    val platformName: String = "",
    val platformLat: String = "",
    val platformLng: String = "",
    val platformLabel: String = "",
)

data class StationPlatform(
    val name: String,
    val lat: String,
    val lng: String,
    val distance: Double = 0.0,
    val sameCount: Int = 0,
)

enum class StationLinesState { NOT_LOADED, LOADING, LOADED, ERROR }

data class StationUi(
    val name: String,
    val desc: String,
    val lat: String = "",
    val lng: String = "",
    val buses: List<BusLineUi> = emptyList(),
    val linesState: StationLinesState = StationLinesState.NOT_LOADED,
) {
    val key: String get() = "$name|$lat|$lng"
}

class BusHomeNearbyAdapter(
    private val onStationClick: (StationUi) -> Unit,
) : RecyclerView.Adapter<BusHomeNearbyAdapter.StationVH>() {

    companion object {
        private const val STATION_PAGE_SIZE = 10
        private const val PREVIEW_LINE_COUNT = 3
    }

    private var allStations: List<StationUi> = emptyList()
    private var shown: List<StationUi> = emptyList()
    private var searchKeyword = ""
    private var visibleStationCount = STATION_PAGE_SIZE

    fun submitList(stations: List<StationUi>) {
        allStations = stations
        if (stations.isEmpty()) visibleStationCount = STATION_PAGE_SIZE
        applyFilter()
    }

    fun updateStation(station: StationUi) {
        allStations = allStations.map { if (it.key == station.key) station else it }
        applyFilter()
    }

    fun stationAt(position: Int): StationUi? = shown.getOrNull(position)

    fun loadMoreStations(): Boolean {
        if (searchKeyword.isNotEmpty() || visibleStationCount >= allStations.size) return false
        visibleStationCount = (visibleStationCount + STATION_PAGE_SIZE).coerceAtMost(allStations.size)
        applyFilter()
        return true
    }

    fun setSearchFilter(keyword: String) {
        searchKeyword = keyword.trim()
        applyFilter()
    }

    private fun applyFilter() {
        val keyword = searchKeyword
        val filtered = if (keyword.isEmpty()) {
            allStations
        } else {
            allStations.filter { station ->
                station.name.contains(keyword, ignoreCase = true) ||
                    station.desc.contains(keyword, ignoreCase = true) ||
                    station.buses.any { line ->
                        line.id.contains(keyword, ignoreCase = true) ||
                            line.direction.contains(keyword, ignoreCase = true)
                    }
            }
        }
        shown = if (keyword.isEmpty()) filtered.take(visibleStationCount) else filtered
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationVH = StationVH(
        ItemStationCardBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun getItemCount(): Int = shown.size

    override fun onBindViewHolder(holder: StationVH, position: Int) {
        holder.bind(shown[position])
    }

    inner class StationVH(private val binding: ItemStationCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(station: StationUi) {
            binding.stationName.text = station.name
            binding.stationDesc.text = station.desc
            binding.linePreview.text = previewText(station)
            binding.root.setOnClickListener { onStationClick(station) }
        }

        private fun previewText(station: StationUi): String = when (station.linesState) {
            StationLinesState.NOT_LOADED -> binding.root.context.getString(R.string.nearby_lines_tap_to_view)
            StationLinesState.LOADING -> binding.root.context.getString(R.string.nearby_lines_loading)
            StationLinesState.ERROR -> binding.root.context.getString(R.string.nearby_lines_tap_to_view)
            StationLinesState.LOADED -> {
                val names = station.buses.map { it.id }.filter { it.isNotBlank() }.distinct()
                if (names.isEmpty()) {
                    binding.root.context.getString(R.string.nearby_lines_empty)
                } else {
                    val preview = names.take(PREVIEW_LINE_COUNT).joinToString(" · ")
                    val remaining = names.size - PREVIEW_LINE_COUNT
                    if (remaining > 0) {
                        binding.root.context.getString(R.string.nearby_lines_preview_more, preview, remaining)
                    } else {
                        preview
                    }
                }
            }
        }
    }
}
