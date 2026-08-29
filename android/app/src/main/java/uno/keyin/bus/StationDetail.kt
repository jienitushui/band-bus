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
    private var items: List<BusLineUi> = emptyList()

    fun submitList(value: List<BusLineUi>) {
        items = value
        notifyDataSetChanged()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        city = CityConfigStore.get(this)
        stationName = intent.getStringExtra(EXTRA_STATION_NAME).orEmpty()
        binding.stationTitle.text = stationName.ifBlank { getString(R.string.title_station_detail) }
        binding.btnBack.setOnClickListener { finish() }
        binding.lineList.layoutManager = LinearLayoutManager(this)
        binding.lineList.adapter = adapter
        loadStationLines()
    }

    private fun loadStationLines() {
        binding.state.setText(R.string.station_detail_loading)
        BusApiClient.executor.execute {
            val result = runCatching { BusApiClient.loadStationLines(city, stationName, "", "") }
            runOnUiThread {
                result.onSuccess { lines ->
                    adapter.submitList(lines)
                    binding.state.text = if (lines.isEmpty()) {
                        getString(R.string.station_detail_empty)
                    } else {
                        "共 ${lines.size} 条线路"
                    }
                }.onFailure {
                    adapter.submitList(emptyList())
                    binding.state.setText(R.string.station_detail_failed)
                }
            }
        }
    }

    private fun openLineDetail(line: BusLineUi) {
        startActivity(Intent(this, LineDetailActivity::class.java).apply {
            putExtra(LineDetailActivity.EXTRA_LINE_NAME, line.id)
            putExtra(LineDetailActivity.EXTRA_DIRECTION, line.directionCode)
        })
    }

    companion object {
        const val EXTRA_STATION_NAME = "stationName"
    }
}
