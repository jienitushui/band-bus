package uno.keyin.bus

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uno.keyin.bus.databinding.ActivityStationDetailBinding
import uno.keyin.bus.databinding.ItemStationLineBinding

class StationLineAdapter(
    private val onClick: (BusLineUi) -> Unit,
    private val isFollowing: (BusLineUi) -> Boolean,
    private val onFollowClick: (BusLineUi) -> Boolean,
    private val isFavorite: (BusLineUi) -> Boolean,
    private val onFavoriteClick: (BusLineUi) -> Unit,
) :
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
        ItemStationLineBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    inner class VH(private val binding: ItemStationLineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BusLineUi) {
            binding.lineName.text = item.id
            binding.lineDescription.text = buildList {
                item.platformLabel.takeIf { it.isNotBlank() }?.let(::add)
                add("开往 ${item.direction}")
                item.statusMain.takeIf { it.isNotBlank() }?.let(::add)
                item.statusSub?.takeIf { it.isNotBlank() }?.let(::add)
            }.joinToString(" · ")
            binding.root.setOnClickListener { onClick(item) }
            renderFollowing(isFollowing(item))
            binding.followLine.setOnClickListener { renderFollowing(onFollowClick(item)) }
            binding.favoriteLine.visibility = View.VISIBLE
            binding.favoriteLine.setImageResource(if (isFavorite(item)) R.drawable.ic_bookmark_filled_24 else R.drawable.ic_bookmark_outline_24)
            binding.favoriteLine.setOnClickListener { onFavoriteClick(item); bind(item) }
        }

        private fun renderFollowing(following: Boolean) {
            binding.followLine.setImageResource(
                if (following) R.drawable.ic_star_filled_24 else R.drawable.ic_star_outline_24,
            )
            binding.followLine.contentDescription = binding.root.context.getString(
                if (following) R.string.action_stop_following else R.string.action_follow_realtime,
            )
            binding.followLine.imageTintList = ContextCompat.getColorStateList(
                binding.root.context,
                if (following) android.R.color.holo_orange_dark else android.R.color.darker_gray,
            )
        }
    }
}

class StationDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStationDetailBinding
    private val adapter = StationLineAdapter(::openLineDetail, ::isFollowing, ::toggleFollowing, ::isFavorite, ::toggleFavorite)
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

    private fun targetFor(line: BusLineUi) = RealtimeWatchTarget(
        cityName = city.cityName,
        cityKey = city.cityKey,
        stationName = line.platformName.ifBlank { stationName },
        stationLat = line.platformLat.ifBlank { stationLat },
        stationLng = line.platformLng.ifBlank { stationLng },
        platformLabel = line.platformLabel,
        lineName = line.id,
        direction = line.direction,
        directionCode = line.directionCode,
        stationOrder = line.stationOrder,
    )

    private fun isFollowing(line: BusLineUi): Boolean =
        RealtimeWatchStore.isFollowing(this, targetFor(line))

    private fun toggleFollowing(line: BusLineUi): Boolean {
        val target = targetFor(line)
        if (RealtimeWatchStore.isFollowing(this, target)) {
            RealtimeWatchStore.remove(this, target)
            LocationRelayService.pushRealtimeTargets(this)
            Toast.makeText(this, R.string.realtime_removed, Toast.LENGTH_SHORT).show()
            return false
        }
        val added = RealtimeWatchStore.add(this, target)
        if (added) LocationRelayService.pushRealtimeTargets(this)
        Toast.makeText(
            this,
            if (added) R.string.realtime_added else R.string.realtime_limit_reached,
            Toast.LENGTH_SHORT,
        ).show()
        return added
    }

    private fun isFavorite(line: BusLineUi): Boolean = FavoriteBusStore.isFavorite(this, targetFor(line))

    private fun toggleFavorite(line: BusLineUi) {
        val added = FavoriteBusStore.toggle(this, targetFor(line))
        Toast.makeText(this, if (added) R.string.favorite_added else R.string.favorite_removed, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_STATION_NAME = "stationName"
        const val EXTRA_STATION_LAT = "stationLat"
        const val EXTRA_STATION_LNG = "stationLng"
    }
}
