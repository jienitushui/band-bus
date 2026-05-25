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

    private val allStations: List<StationUi> = demoStationsNearby()
    private var shown: List<StationUi> = allStations

    fun setSearchFilter(keyword: String) {
        val k = keyword.trim()
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

private fun demoStationsNearby(): List<StationUi> = listOf(
    StationUi(
        name = "府文庙",
        desc = "约 320m",
        buses = listOf(
            BusLineUi("K1路", "闽台缘博物馆", "3 分钟", "点击查看发车预测"),
            BusLineUi("39路", "福厦铁路泉州站", "等待首站发车", "点击查看发车预测"),
        ),
    ),
    StationUi(
        name = "关帝庙",
        desc = "约 580m",
        buses = listOf(
            BusLineUi("3路", "霞美", "8 分钟", null),
            BusLineUi("4路", "清濛", "已到站", null),
        ),
    ),
)
