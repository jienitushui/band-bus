package uno.keyin.bus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uno.keyin.bus.databinding.ItemSearchResultBinding

enum class SearchResultType { LINE, STATION }

data class SearchResult(
    val type: SearchResultType,
    val name: String,
    val description: String,
    val lineName: String = "",
    val direction: String = "1",
)

class BusSearchAdapter(private val onClick: (SearchResult) -> Unit) :
    RecyclerView.Adapter<BusSearchAdapter.ResultVH>() {
    private var items: List<SearchResult> = emptyList()

    fun submitList(value: List<SearchResult>) {
        items = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultVH = ResultVH(
        ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: ResultVH, position: Int) = holder.bind(items[position])

    inner class ResultVH(private val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchResult) {
            binding.resultType.text = if (item.type == SearchResultType.LINE) "线路" else "站点"
            binding.resultName.text = item.name
            binding.resultDescription.text = item.description
            binding.root.setOnClickListener { onClick(item) }
        }
    }
}
