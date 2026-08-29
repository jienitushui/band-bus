package uno.keyin.bus

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uno.keyin.bus.databinding.ActivityStationDetailBinding
import uno.keyin.bus.databinding.ItemSearchResultBinding

class StationLineAdapter(private val onClick: (BusLineUi) -> Unit) :
    RecyclerView.Adapter<StationLineAdapter.VH>() {
    companion object {
        private const val PAGE_SIZE = 5
    }

    private var allItems: List<BusLineUi> = emptyList()
    private var items: List<BusLineUi> = emptyList()

    fun submitAll(value: List<BusLineUi>) {
        allItems = value
        items = value.take(PAGE_SIZE)
        notifyDataSetChanged()
    }

    fun loadMore(): Boolean {
        if (items.size >= allItems.size) return false
        items = allItems.take((items.size + PAGE_SIZE).coerceAtMost(allItems.size))
        notifyDataSetChanged()
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    inner class VH(private val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BusLineUi) {
            binding.resultType.text = "线路"
            binding.resultName.text = item.id
            binding.resultDescription.text = buildList {
                add("开往 ${item.direction}")
                item.statusMain.takeIf { it.isNotBlank() }?.let(::add)
                item.statusSub?.takeIf { it.isNotBlank() }?.let(::add)
            }.joinToString(" · ")
            binding.root.setOnClickListener { onClick(item) }
        }
    }
}

class StationDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStationDetailBinding
    private val adapter = StationLineAdapter(::openLineDetail)
    private lateinit var city: CityConfig
    private var stationName = ""
    private var stationLat = ""
    private var stationLng = ""
    private var stationOrder = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        city = CityConfigStore.get(this)
        stationName = intent.getStringExtra(EXTRA_STATION_NAME).orEmpty()
        stationLat = intent.getStringExtra(EXTRA_STATION_LAT).orEmpty()
        stationLng = intent.getStringExtra(EXTRA_STATION_LNG).orEmpty()
        binding.stationTitle.text = stationName.ifBlank { getString(R.string.title_station_detail) }
        binding.btnBack.setOnClickListener { finish() }
        binding.lineList.layoutManager = LinearLayoutManager(this)
        binding.lineList.adapter = adapter
        binding.lineList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && !recyclerView.canScrollVertically(1)) adapter.loadMore()
            }
        })
        binding.state.setOnClickListener { loadStationLines() }
        loadStationLines()
    }

    private fun loadStationLines() {
        binding.state.isClickable = false
        binding.state.setText(R.string.station_detail_loading)
        StationLinesRepository.load(city, stationName, stationLat, stationLng) { result ->
            runOnUiThread {
                if (isFinishing || isDestroyed) return@runOnUiThread
                result.onSuccess { lines ->
                    adapter.submitAll(lines)
                    binding.state.text = if (lines.isEmpty()) {
                        getString(R.string.station_detail_empty)
                    } else {
                        "共 ${lines.size} 条线路"
                    }
                }.onFailure {
                    adapter.submitAll(emptyList())
                    binding.state.setText(R.string.station_detail_failed)
                    binding.state.isClickable = true
                }
            }
        }
    }

    private fun openLineDetail(line: BusLineUi) {
        startActivity(Intent(this, LineDetailActivity::class.java).apply {
            putExtra(LineDetailActivity.EXTRA_LINE_NAME, line.id)
            putExtra(LineDetailActivity.EXTRA_DIRECTION, line.directionCode)
            putExtra(LineDetailActivity.EXTRA_CURRENT_STATION_NAME, stationName)
            putExtra(LineDetailActivity.EXTRA_CURRENT_STATION_ORDER, line.stationOrder)
        })
    }

    companion object {
        const val EXTRA_STATION_NAME = "stationName"
        const val EXTRA_STATION_LAT = "stationLat"
        const val EXTRA_STATION_LNG = "stationLng"
    }
}
