package uno.keyin.bus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import android.os.Handler
import android.os.Looper
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.core.content.ContextCompat
import org.json.JSONArray
import uno.keyin.bus.databinding.ActivityTransferBinding
import uno.keyin.bus.databinding.ItemTransferSchemeBinding

data class TransferLineOption(
    val lineName: String,
    val direction: String = "",
    val boardStation: String = "",
    val alightStation: String = "",
    val stationCount: Int? = null,
    val distance: String = "",
    val duration: String = "",
    val entryName: String = "",
    val exitName: String = "",
)

data class TransferLeg(
    val options: List<TransferLineOption>,
    val walkAfterDistance: String = "",
) {
    val boardStation: String get() = options.firstOrNull()?.boardStation.orEmpty()
    val alightStation: String get() = options.firstOrNull()?.alightStation.orEmpty()
    val lineNames: String get() = options.map { it.lineName }.filter { it.isNotBlank() }.distinct().joinToString(" / ")
}

data class TransferScheme(
    val startStation: String,
    val endStation: String,
    val startLine: String,
    val changeStation: String,
    val endLine: String,
    val totalTime: String = "",
    val walkDistance: String = "",
    val boardingStation: String = "",
    val alightingStation: String = "",
    val stationCount: String = "",
    val lineSegments: List<String> = emptyList(),
    val realtimeLine: String = "",
    val realtimeDirection: String = "",
    val realtimeOrder: String = "",
    val realtimeStation: String = "",
    val realtimeDisplayLine: String = "",
    val realtimeText: String = "",
    val startWalkDistance: String = "",
    val endWalkDistance: String = "",
    val totalDistance: String = "",
    val legs: List<TransferLeg> = emptyList(),
)

internal fun TransferScheme.routeSummary(): String {
    if (legs.isEmpty()) return listOf(
        startStation,
        boardingStation.takeIf { it.isNotBlank() && it != startStation },
        startLine,
        changeStation,
        endLine,
        alightingStation.takeIf { it.isNotBlank() && it != endStation },
        endStation,
    ).filterNotNull().filter { it.isNotBlank() }.joinToString(" → ")
    return buildList {
        add(startStation)
        legs.forEach { leg ->
            if (leg.boardStation.isNotBlank() && leg.boardStation != lastOrNull()) add(leg.boardStation)
            if (leg.lineNames.isNotBlank()) add(leg.lineNames)
            if (leg.alightStation.isNotBlank()) add(leg.alightStation)
        }
        if (endStation.isNotBlank() && endStation != lastOrNull()) add(endStation)
    }.filter { it.isNotBlank() }.joinToString(" → ")
}

internal fun TransferScheme.legsJson(): JSONArray = JSONArray().apply {
    legs.forEach { leg ->
        put(JSONObject().apply {
            put("boardStation", leg.boardStation)
            put("alightStation", leg.alightStation)
            put("walkAfterDistance", leg.walkAfterDistance)
            put("options", JSONArray().apply {
                leg.options.forEach { option ->
                    put(JSONObject().apply {
                        put("lineName", option.lineName)
                        put("direction", option.direction)
                        put("boardStation", option.boardStation)
                        put("alightStation", option.alightStation)
                        put("stationCount", option.stationCount ?: JSONObject.NULL)
                        put("distance", option.distance)
                        put("duration", option.duration)
                        put("entryName", option.entryName)
                        put("exitName", option.exitName)
                    })
                }
            })
        })
    }
}

class TransferAdapter(private val onClick: (TransferScheme) -> Unit) : RecyclerView.Adapter<TransferAdapter.VH>() {
    private var items: List<TransferScheme> = emptyList()
    fun submitList(value: List<TransferScheme>) { items = value; notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(ItemTransferSchemeBinding.inflate(LayoutInflater.from(parent.context), parent, false), onClick)
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    class VH(private val binding: ItemTransferSchemeBinding, private val onClick: (TransferScheme) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TransferScheme) {
            binding.totalTime.text = item.totalTime.ifBlank { "--" }
            binding.walkDistance.text = item.walkDistance.ifBlank { "--" }
            val boarding = item.boardingStation.ifBlank { item.startStation }
            binding.rideInfo.text = listOf(
                item.stationCount.takeIf { it.isNotBlank() }?.let { "车程$it" },
                boarding.takeIf { it.isNotBlank() }?.let { "在公交站 $it 上车" },
            ).filterNotNull().joinToString(" · ")
            renderSegments(item)
            renderLegDetails(item)
            binding.realtimePanel.visibility = if (item.realtimeText.isBlank()) View.GONE else View.VISIBLE
            binding.realtimeLine.text = item.realtimeDisplayLine.ifBlank {
                item.lineSegments.firstOrNull().orEmpty().substringBefore(" / ")
            }
            binding.realtimeStatus.text = item.realtimeText
            binding.root.setOnClickListener { onClick(item) }
        }

        private fun renderLegDetails(item: TransferScheme) {
            val context = binding.root.context
            binding.routeDetailContainer.removeAllViews()
            binding.routeDetailContainer.visibility = if (item.legs.isEmpty()) View.GONE else View.VISIBLE
            item.legs.forEachIndexed { index, leg ->
                val stationPair = when {
                    leg.boardStation.isNotBlank() && leg.alightStation.isNotBlank() ->
                        "${leg.boardStation} → ${leg.alightStation}"
                    else -> "站点信息暂缺"
                }
                val optionText = leg.options.joinToString("\n") { option ->
                    listOf(
                        option.lineName,
                        option.direction.takeIf { it.isNotBlank() }?.let { "开往$it" },
                        option.stationCount?.let { "${it}站" },
                    ).filterNotNull().joinToString(" · ")
                }
                binding.routeDetailContainer.addView(TextView(context).apply {
                    text = "第${index + 1}段  $stationPair\n$optionText"
                    setTextColor(Color.parseColor("#374151"))
                    textSize = 13f
                    setLineSpacing(dp(2).toFloat(), 1f)
                    setPadding(0, dp(7), 0, dp(4))
                })
                if (index < item.legs.lastIndex) {
                    val nextBoard = item.legs[index + 1].boardStation
                    val sameStation = leg.alightStation.isNotBlank() && leg.alightStation == nextBoard
                    val transferText = when {
                        sameStation && leg.walkAfterDistance.isBlank() -> "同站换乘"
                        leg.walkAfterDistance.isNotBlank() -> listOf(
                            "步行${leg.walkAfterDistance}",
                            nextBoard.takeIf { it.isNotBlank() }?.let { "前往${it}换乘" },
                        ).filterNotNull().joinToString(" · ")
                        else -> "换乘站信息暂缺"
                    }
                    binding.routeDetailContainer.addView(TextView(context).apply {
                        text = transferText
                        setTextColor(Color.parseColor("#F97316"))
                        textSize = 12f
                        setPadding(dp(12), dp(3), 0, dp(3))
                    })
                }
            }
        }

        private fun renderSegments(item: TransferScheme) {
            val context = binding.root.context
            val segments = item.lineSegments.ifEmpty {
                listOf(item.startLine) + item.endLine.split(" → ")
            }.map { it.trim() }.filter { it.isNotEmpty() }
            binding.routeChipGroup.removeAllViews()
            segments.forEachIndexed { position, segment ->
                val segmentUnit = LinearLayout(context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
                }
                segmentUnit.addView(TextView(context).apply {
                    text = segment
                    setTextColor(Color.parseColor("#1685E6"))
                    textSize = 16f
                    gravity = Gravity.CENTER_VERTICAL
                    minHeight = dp(40)
                    maxWidth = dp(if (position < segments.lastIndex) 255 else 292)
                    background = ContextCompat.getDrawable(context, R.drawable.bg_route_chip)
                    setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_bus, 0, 0, 0)
                    compoundDrawablePadding = dp(8)
                    setPadding(dp(12), dp(7), dp(14), dp(7))
                })
                if (position < segments.lastIndex) {
                    segmentUnit.addView(TextView(context).apply {
                        text = "›"
                        gravity = Gravity.CENTER
                        setTextColor(Color.parseColor("#B8C0CC"))
                        textSize = 24f
                        setPadding(dp(7), 0, dp(3), 0)
                    })
                }
                binding.routeChipGroup.addView(segmentUnit)
            }
        }

        private fun dp(value: Int): Int =
            (value * binding.root.resources.displayMetrics.density).toInt()
    }
}

class TransferActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransferBinding
    private val adapter = TransferAdapter(::syncPlan)
    private lateinit var suggestionAdapter: TransferStationAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var suggestionGeneration = 0
    private var activeField = 1
    private val suggestionRunnable = Runnable { loadSuggestions() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener { finish() }
        binding.schemeList.layoutManager = LinearLayoutManager(this)
        binding.schemeList.adapter = adapter
        suggestionAdapter = TransferStationAdapter { value ->
            val target = if (activeField == 1) binding.inputStart else binding.inputEnd
            target.setText(value)
            target.setSelection(value.length)
            binding.stationSuggestions.visibility = ViewGroup.GONE
        }
        binding.stationSuggestions.layoutManager = LinearLayoutManager(this)
        binding.stationSuggestions.adapter = suggestionAdapter
        binding.inputStart.doAfterTextChanged { activeField = 1; queueSuggestions(it?.toString().orEmpty()) }
        binding.inputEnd.doAfterTextChanged { activeField = 2; queueSuggestions(it?.toString().orEmpty()) }
        binding.btnSearchTransfer.setOnClickListener { search() }
        binding.inputStart.setText(R.string.transfer_my_location)
    }
    private fun queueSuggestions(value: String) {
        handler.removeCallbacks(suggestionRunnable)
        if (value.trim().length < 1) {
            suggestionGeneration += 1
            suggestionAdapter.submitList(emptyList())
            binding.stationSuggestions.visibility = ViewGroup.GONE
            return
        }
        binding.stationSuggestions.visibility = ViewGroup.VISIBLE
        handler.postDelayed(suggestionRunnable, 300)
    }
    private fun loadSuggestions() {
        val keyword = (if (activeField == 1) binding.inputStart else binding.inputEnd).text?.toString()?.trim().orEmpty()
        if (keyword.isEmpty()) return
        val city = CityConfigStore.get(this)
        val generation = ++suggestionGeneration
        BusApiClient.executor.execute {
            val result = runCatching { BusApiClient.searchStations(city, keyword) }
            runOnUiThread {
                if (generation != suggestionGeneration) return@runOnUiThread
                result.onSuccess { values ->
                    suggestionAdapter.submitList(values)
                    binding.stationSuggestions.visibility = if (values.isEmpty()) ViewGroup.GONE else ViewGroup.VISIBLE
                }.onFailure { binding.stationSuggestions.visibility = ViewGroup.GONE }
            }
        }
    }
    private fun search() {
        binding.stationSuggestions.visibility = ViewGroup.GONE
        val start = binding.inputStart.text?.toString()?.trim().orEmpty()
        val end = binding.inputEnd.text?.toString()?.trim().orEmpty()
        if (start.isEmpty() || end.isEmpty()) {
            binding.transferState.setText(R.string.transfer_input_required)
            return
        }
        val city = CityConfigStore.get(this)
        binding.transferState.setText(R.string.transfer_loading)
        BusApiClient.executor.execute {
            val result = runCatching { BusApiClient.loadTransfer(city, start, end) }
            runOnUiThread {
                result.onSuccess { schemes ->
                    adapter.submitList(schemes)
                    binding.transferState.text = if (schemes.isEmpty()) getString(R.string.transfer_empty) else ""
                }.onFailure { binding.transferState.setText(R.string.transfer_failed) }
            }
        }
    }

    private fun syncPlan(scheme: TransferScheme) {
        val city = CityConfigStore.get(this)
        val payload = JSONObject().apply {
            put("type", LocationRelayService.TYPE_TRANSFER_PLAN)
            put("cityName", city.cityName)
            put("cityKey", city.cityKey)
            put("startStation", scheme.startStation)
            put("endStation", scheme.endStation)
            put("boardingStation", scheme.boardingStation)
            put("alightingStation", scheme.alightingStation)
            put("startLine", scheme.startLine)
            put("changeStation", scheme.changeStation)
            put("endLine", scheme.endLine)
            put("totalTime", scheme.totalTime)
            put("walkDistance", scheme.walkDistance)
            put("lineSegments", JSONArray(scheme.lineSegments))
            put("startWalkDistance", scheme.startWalkDistance)
            put("endWalkDistance", scheme.endWalkDistance)
            put("totalDistance", scheme.totalDistance)
            put("legs", scheme.legsJson())
            put("summary", scheme.routeSummary())
            put("ts", System.currentTimeMillis())
        }.toString()
        LocationRelayService.pushTransferPlan(this, payload)
        binding.transferState.setText(R.string.transfer_sync_requested)
    }
    override fun onDestroy() {
        handler.removeCallbacks(suggestionRunnable)
        super.onDestroy()
    }
}
