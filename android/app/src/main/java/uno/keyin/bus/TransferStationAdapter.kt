package uno.keyin.bus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uno.keyin.bus.databinding.ItemTransferStationBinding

class TransferStationAdapter(private val onClick: (String) -> Unit) : RecyclerView.Adapter<TransferStationAdapter.VH>() {
    private var items: List<String> = emptyList()
    fun submitList(value: List<String>) { items = value; notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(ItemTransferStationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    inner class VH(private val binding: ItemTransferStationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(value: String) { binding.stationName.text = value; binding.root.setOnClickListener { onClick(value) } }
    }
}
