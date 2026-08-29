package uno.keyin.bus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.text.format.DateUtils
import androidx.recyclerview.widget.RecyclerView
import uno.keyin.bus.databinding.ItemRealtimeBusBinding

data class RealtimeBusRow(
    val target: RealtimeWatchTarget,
    val line: BusLineUi? = null,
    val updatedAt: Long = 0L,
    val refreshFailed: Boolean = false,
    val actionActive: Boolean = true,
)

class RealtimeBusAdapter(
    private val onOpen: (RealtimeWatchTarget) -> Unit,
    private val onRemove: (RealtimeWatchTarget) -> Unit,
    private val onReminder: ((RealtimeWatchTarget) -> Unit)? = null,
    private val onFavoriteRemove: ((RealtimeWatchTarget) -> Unit)? = null,
) : RecyclerView.Adapter<RealtimeBusAdapter.VH>() {
    private var rows: List<RealtimeBusRow> = emptyList()

    fun submitList(value: List<RealtimeBusRow>) {
        rows = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemRealtimeBusBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun getItemCount() = rows.size
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(rows[position], position)

    inner class VH(private val binding: ItemRealtimeBusBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(row: RealtimeBusRow, position: Int) {
            val target = row.target
            val firstAtStation = position == 0 || rows[position - 1].target.stationKey != target.stationKey
            binding.stationName.visibility = if (firstAtStation) View.VISIBLE else View.GONE
            binding.stationName.text = listOf(target.stationName, target.platformLabel)
                .filter { it.isNotBlank() }.joinToString(" · ")
            binding.lineName.text = target.lineName
            binding.direction.text = binding.root.context.getString(R.string.realtime_direction, target.direction)
            binding.statusMain.text = when {
                row.line != null -> row.line.statusMain
                row.refreshFailed -> binding.root.context.getString(R.string.realtime_refresh_failed_short)
                else -> binding.root.context.getString(R.string.realtime_refreshing)
            }
            binding.statusSub.text = buildList {
                row.line?.statusSub?.takeIf { it.isNotBlank() }?.let(::add)
                if (row.refreshFailed) add(binding.root.context.getString(R.string.realtime_refresh_failed_short))
                if (row.updatedAt > 0L) {
                    add(
                        DateUtils.getRelativeTimeSpanString(
                            row.updatedAt,
                            System.currentTimeMillis(),
                            DateUtils.SECOND_IN_MILLIS,
                        ).toString(),
                    )
                }
            }.joinToString(" · ")
            binding.statusSub.visibility = if (binding.statusSub.text.isBlank()) View.GONE else View.VISIBLE
            binding.removeTarget.setOnClickListener { onRemove(target) }
            binding.removeTarget.setImageResource(
                if (row.actionActive) R.drawable.ic_star_filled_24 else R.drawable.ic_star_outline_24,
            )
            binding.reminder.visibility = if (onReminder == null && onFavoriteRemove == null) View.GONE else View.VISIBLE
            when {
                onReminder != null -> {
                    binding.reminder.contentDescription = binding.root.context.getString(R.string.action_arrival_reminder)
                    binding.reminder.setImageResource(
                        if (target.reminderEnabled) R.drawable.ic_notifications_filled_24 else R.drawable.ic_notifications_outline_24,
                    )
                    binding.reminder.setOnClickListener { onReminder.invoke(target) }
                }
                onFavoriteRemove != null -> {
                    binding.reminder.contentDescription = binding.root.context.getString(R.string.action_remove_favorite)
                    binding.reminder.setImageResource(R.drawable.ic_bookmark_filled_24)
                    binding.reminder.setOnClickListener { onFavoriteRemove.invoke(target) }
                }
            }
            binding.root.setOnClickListener { onOpen(target) }
        }
    }
}
