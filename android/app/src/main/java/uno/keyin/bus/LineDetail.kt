package uno.keyin.bus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uno.keyin.bus.databinding.ActivityLineDetailBinding
import uno.keyin.bus.databinding.ItemLineStationBinding

data class LineStation(val order: Int, val name: String)
data class LineDetail(
    val lineName: String,
    val direction: String,
    val from: String,
    val to: String,
    val firstTime: String,
    val lastTime: String,
    val comment: String,
    val stations: List<LineStation>,
)

class LineStationAdapter : RecyclerView.Adapter<LineStationAdapter.VH>() {
    private var items: List<LineStation> = emptyList()
    fun submitList(value: List<LineStation>) { items = value; notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemLineStationBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    class VH(private val binding: ItemLineStationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LineStation) {
            binding.stationOrder.text = item.order.toString()
            binding.stationName.text = item.name
        }
    }
}

class LineDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLineDetailBinding
    private val adapter = LineStationAdapter()
    private var lineName = ""
    private var city = CityConfig("泉州市", "泉州", "qz595803", 0L)
    private var direction = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLineDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lineName = intent.getStringExtra(EXTRA_LINE_NAME).orEmpty()
        direction = intent.getStringExtra(EXTRA_DIRECTION) ?: "1"
        city = CityConfigStore.get(this)
        binding.btnBack.setOnClickListener { finish() }
        binding.btnDirection.setOnClickListener {
            direction = if (direction == "1") "2" else "1"
            load()
        }
        binding.stationList.layoutManager = LinearLayoutManager(this)
        binding.stationList.adapter = adapter
        load()
    }

    private fun load() {
        binding.loading.visibility = View.VISIBLE
        binding.stationList.visibility = View.GONE
        BusApiClient.executor.execute {
            val result = runCatching { BusApiClient.loadLineDetail(city, lineName, direction) }
            runOnUiThread {
                binding.loading.visibility = View.GONE
                result.onSuccess { detail ->
                    binding.lineTitle.text = detail.lineName
                    binding.lineDirection.text = "${detail.from} → ${detail.to}"
                    binding.lineSchedule.text = "首班 ${detail.firstTime}    末班 ${detail.lastTime}"
                    binding.lineComment.text = detail.comment
                    binding.lineComment.visibility = if (detail.comment.isBlank()) View.GONE else View.VISIBLE
                    adapter.submitList(detail.stations)
                    binding.stationList.visibility = View.VISIBLE
                }.onFailure {
                    Toast.makeText(this, R.string.line_detail_failed, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        const val EXTRA_LINE_NAME = "lineName"
        const val EXTRA_DIRECTION = "direction"
    }
}
