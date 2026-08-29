package uno.keyin.bus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uno.keyin.bus.databinding.ItemBusLineRowBinding
import uno.keyin.bus.databinding.ItemStationCardBinding

data class BusLineUi(
    val id: String,
    val direction: String,
    val statusMain: String,
    val statusSub: String? = null,
)

data class StationUi(
    val name: String,
    val desc: String,
    val buses: List<BusLineUi>,
)

class BusHomeNearbyAdapter : RecyclerView.Adapter<BusHomeNearbyAdapter.StationVH>() {

    private var allStations: List<StationUi> = emptyList()
    private var shown: List<StationUi> = emptyList()
    private var searchKeyword: String = ""

    fun submitList(stations: List<StationUi>) {
        allStations = stations
        applyFilter()
    }

    fun setSearchFilter(keyword: String) {
        searchKeyword = keyword.trim()
        applyFilter()
    }

    private fun applyFilter() {
        val k = searchKeyword
        shown = if (k.isEmpty()) {
            allStations
        } else {
            allStations.mapNotNull { st ->
                val buses = st.buses.filter { line ->
                    line.id.contains(k, ignoreCase = true) ||
                        line.direction.contains(k, ignoreCase = true)
                }
                when {
                    st.name.contains(k, ignoreCase = true) || st.desc.contains(k, ignoreCase = true) -> st
                    buses.isNotEmpty() -> st.copy(buses = buses)
                    else -> null
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationVH {
        val inf = LayoutInflater.from(parent.context)
        return StationVH(ItemStationCardBinding.inflate(inf, parent, false))
    }

    override fun getItemCount(): Int = shown.size

    override fun onBindViewHolder(holder: StationVH, position: Int) {
        holder.bind(shown[position])
    }

    class StationVH(private val binding: ItemStationCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(st: StationUi) {
            binding.stationName.text = st.name
            binding.stationDesc.text = st.desc
            binding.busLinesContainer.removeAllViews()
            val inflater = LayoutInflater.from(binding.root.context)
            for (line in st.buses) {
                val row = ItemBusLineRowBinding.inflate(inflater, binding.busLinesContainer, true)
                row.busLineId.text = line.id
                row.busDirection.text =
                    binding.root.context.getString(R.string.bus_towards_prefix) + line.direction
                row.busStatus.text = line.statusMain
                if (line.statusSub != null) {
                    row.busStatusDetail.visibility = View.VISIBLE
                    row.busStatusDetail.text = line.statusSub
                } else {
                    row.busStatusDetail.visibility = View.GONE
                }
            }
        }
    }
}
