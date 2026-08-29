package uno.keyin.bus

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Gravity
import androidx.core.content.ContextCompat
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uno.keyin.bus.databinding.ActivityLineDetailBinding
import uno.keyin.bus.databinding.ItemLineStationBinding

data class LineStation(
    val order: Int,
    val name: String,
    val isCurrent: Boolean = false,
    val hasVehicle: Boolean = false,
    val vehicleArrived: Boolean = false,
    val vehicleText: String = "",
    val lat: String = "",
    val lng: String = "",
)
data class LineVehicle(val stationOrder: Int, val busNumber: String, val arrived: Boolean)
data class LineRealtime(val vehicles: List<LineVehicle> = emptyList(), val etaText: String = "", val planTime: String = "")
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

class LineStationAdapter(private val onClick: (LineStation) -> Unit = {}) : RecyclerView.Adapter<LineStationAdapter.VH>() {
    private var items: List<LineStation> = emptyList()
    fun submitList(value: List<LineStation>) { items = value; notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemLineStationBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position], position, items.lastIndex)
    inner class VH(private val binding: ItemLineStationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LineStation, position: Int, lastIndex: Int) {
            binding.stationOrder.text = item.order.toString()
            binding.stationName.text = item.name
            binding.root.setBackgroundColor(if (item.isCurrent) 0x0D2563EB else 0x00000000)
            binding.stationDot.background = ContextCompat.getDrawable(
                binding.root.context,
                if (item.isCurrent) R.drawable.bg_station_dot_current else R.drawable.bg_station_dot,
            )
            binding.stationConnectorTop.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
            binding.stationConnectorBottom.visibility = if (position == lastIndex) View.INVISIBLE else View.VISIBLE
            binding.stationVehicle.visibility = if (item.hasVehicle) View.VISIBLE else View.GONE
            binding.stationBusMarker.visibility = if (item.hasVehicle) View.VISIBLE else View.GONE
            (binding.stationBusMarker.layoutParams as? ViewGroup.MarginLayoutParams)?.let { params ->
                (params as? android.widget.FrameLayout.LayoutParams)?.gravity =
                    if (item.vehicleArrived) Gravity.CENTER else Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                binding.stationBusMarker.layoutParams = params
            }
            binding.stationVehicle.text = item.vehicleText
            binding.stationStatus.text = when {
                item.isCurrent -> "当前站"
                position == 0 -> "起点"
                position == lastIndex -> "终点"
                else -> ""
            }
            binding.stationStatus.visibility = if (binding.stationStatus.text.isBlank()) View.GONE else View.VISIBLE
            binding.root.setOnClickListener { onClick(item) }
        }
    }
}

class LineDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_LINE_NAME = "lineName"
        const val EXTRA_DIRECTION = "direction"
        const val EXTRA_CURRENT_STATION_NAME = "currentStationName"
        const val EXTRA_CURRENT_STATION_ORDER = "currentStationOrder"
        private const val REALTIME_REFRESH_MS = 15_000L
        private const val REALTIME_STALE_MS = 45_000L
    }

    private lateinit var binding: ActivityLineDetailBinding
    private val adapter = LineStationAdapter(::showStationActions)
    private var lineName = ""
    private var city = CityConfig("泉州市", "泉州", "qz595803", 0L)
    private var direction = "1"
    private var currentStationName = ""
    private var currentStationOrder = 0
    private val mainHandler = Handler(Looper.getMainLooper())
    private var currentDetail: LineDetail? = null
    private var realtimeLoading = false
    private var resumed = false
    private var lastRealtimeSuccessAt = 0L
    private var detailGeneration = 0
    private var realtimeGeneration = 0
    private val realtimeRefresh = Runnable { currentDetail?.let(::loadRealtime) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLineDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lineName = intent.getStringExtra(EXTRA_LINE_NAME).orEmpty()
        direction = intent.getStringExtra(EXTRA_DIRECTION) ?: "1"
        currentStationName = intent.getStringExtra(EXTRA_CURRENT_STATION_NAME).orEmpty()
        currentStationOrder = intent.getIntExtra(EXTRA_CURRENT_STATION_ORDER, 0)
        city = CityConfigStore.get(this)
        binding.btnBack.setOnClickListener { finish() }
        binding.btnDirection.setOnClickListener {
            direction = if (direction == "1") "2" else "1"
            currentStationOrder = 0
            stopRealtimePolling()
            load()
        }
        binding.stationList.layoutManager = LinearLayoutManager(this)
        binding.stationList.adapter = adapter
        load()
    }

    override fun onResume() {
        super.onResume()
        resumed = true
        currentDetail?.let(::loadRealtime)
    }

    override fun onPause() {
        resumed = false
        stopRealtimePolling()
        super.onPause()
    }

    override fun onDestroy() {
        stopRealtimePolling()
        super.onDestroy()
    }

    private fun load() {
        val generation = ++detailGeneration
        currentDetail = null
        binding.loading.visibility = View.VISIBLE
        binding.stationList.visibility = View.GONE
        BusApiClient.executor.execute {
            val result = runCatching { BusApiClient.loadLineDetail(city, lineName, direction) }
            runOnUiThread {
                if (generation != detailGeneration || isFinishing || isDestroyed) return@runOnUiThread
                binding.loading.visibility = View.GONE
                result.onSuccess { detail ->
                    currentDetail = detail
                    binding.lineTitle.text = detail.lineName
                    binding.lineDirection.text = "${detail.from} → ${detail.to}"
                    binding.lineSchedule.text = "首班 ${detail.firstTime}    末班 ${detail.lastTime}"
                    binding.lineComment.text = detail.comment
                    binding.lineComment.visibility = if (detail.comment.isBlank()) View.GONE else View.VISIBLE
                    val baseStations = markCurrent(detail.stations)
                    adapter.submitList(baseStations)
                    scrollToCurrent(baseStations)
                    loadRealtime(detail)
                    binding.stationList.visibility = View.VISIBLE
                }.onFailure {
                    Toast.makeText(this, R.string.line_detail_failed, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadRealtime(detail: LineDetail) {
        if (realtimeLoading) return
        realtimeLoading = true
        val generation = ++realtimeGeneration
        mainHandler.removeCallbacks(realtimeRefresh)
        val queryOrder = detail.stations.firstOrNull(::isCurrentStation)?.order ?: currentStationOrder
        BusApiClient.executor.execute {
            val result = runCatching { BusApiClient.loadLineRealtime(city, detail.lineName, direction, queryOrder) }
            runOnUiThread {
                if (generation != realtimeGeneration || isFinishing || isDestroyed || currentDetail !== detail) return@runOnUiThread
                realtimeLoading = false
                val realtime = result.getOrNull()
                if (realtime == null) {
                    showRealtimeAge()
                    scheduleRealtimeRefresh()
                    return@runOnUiThread
                }
                lastRealtimeSuccessAt = System.currentTimeMillis()
                binding.realtime.text = realtime.etaText.takeIf { it.isNotBlank() }?.let { "$it 到当前站" }
                    ?: realtime.planTime.takeIf { it.isNotBlank() }?.let { time -> "等待发车 · 预计 $time 发车" }.orEmpty()
                binding.realtimePanel.visibility = if (binding.realtime.text.isNullOrBlank()) View.GONE else View.VISIBLE
                val byOrder = realtime.vehicles.groupBy { it.stationOrder }
                val stations = detail.stations.map { station ->
                    val current = isCurrentStation(station)
                    val vehicle = byOrder[station.order]?.firstOrNull()
                    station.copy(
                        isCurrent = current,
                        hasVehicle = vehicle != null,
                        vehicleArrived = vehicle?.arrived == true,
                        vehicleText = vehicle?.busNumber.orEmpty(),
                    )
                }
                adapter.submitList(stations)
                scrollToCurrent(stations)
                scheduleRealtimeRefresh()
            }
        }
    }

    private fun showRealtimeAge() {
        if (lastRealtimeSuccessAt > 0L &&
            System.currentTimeMillis() - lastRealtimeSuccessAt >= REALTIME_STALE_MS
        ) {
            val current = binding.realtime.text?.toString().orEmpty()
            binding.realtime.text = listOf(current, getString(R.string.realtime_data_stale))
                .filter { it.isNotBlank() }.joinToString(" · ")
            binding.realtimePanel.visibility = View.VISIBLE
        }
    }

    private fun scheduleRealtimeRefresh() {
        if (!resumed) return
        mainHandler.removeCallbacks(realtimeRefresh)
        mainHandler.postDelayed(realtimeRefresh, REALTIME_REFRESH_MS)
    }

    private fun stopRealtimePolling() {
        mainHandler.removeCallbacks(realtimeRefresh)
        realtimeGeneration += 1
        realtimeLoading = false
    }

    private fun scrollToCurrent(stations: List<LineStation>) {
        stations.indexOfFirst { it.isCurrent }.takeIf { it >= 0 }?.let { (binding.stationList.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(it, 120) }
    }

    private fun markCurrent(stations: List<LineStation>) = stations.map { it.copy(isCurrent = isCurrentStation(it)) }

    private fun isCurrentStation(station: LineStation): Boolean {
        if (currentStationOrder > 0 && station.order == currentStationOrder) return true
        val expected = normalizeStationName(currentStationName)
        val actual = normalizeStationName(station.name)
        return expected.isNotBlank() && actual.isNotBlank() &&
            (expected == actual || expected.contains(actual) || actual.contains(expected))
    }

    private fun normalizeStationName(value: String): String = value
        .replace(Regex("[（）()\\s]"), "")
        .removeSuffix("站")

    private fun showStationActions(station: LineStation) {
        val detail = currentDetail ?: return
        val target = RealtimeWatchTarget(
            cityName = city.cityName,
            cityKey = city.cityKey,
            stationName = station.name,
            stationLat = station.lat,
            stationLng = station.lng,
            lineName = detail.lineName,
            direction = detail.to,
            directionCode = direction,
            stationOrder = station.order,
        )
        AlertDialog.Builder(this)
            .setTitle(station.name)
            .setItems(arrayOf(getString(R.string.action_follow_realtime), getString(R.string.action_favorite))) { _, which ->
                if (which == 0) {
                    val added = RealtimeWatchStore.add(this, target)
                    if (added) LocationRelayService.pushRealtimeTargets(this)
                    Toast.makeText(this, if (added) R.string.realtime_added else R.string.realtime_limit_reached, Toast.LENGTH_SHORT).show()
                } else {
                    val added = FavoriteBusStore.toggle(this, target)
                    Toast.makeText(this, if (added) R.string.favorite_added else R.string.favorite_removed, Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }

}
