package uno.keyin.bus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uno.keyin.bus.databinding.ItemCityBinding

class CityAdapter(private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<CityAdapter.CityVH>() {
    private var cities: List<String> = emptyList()

    fun submitList(value: List<String>) {
        cities = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityVH = CityVH(
        ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun getItemCount(): Int = cities.size

    override fun onBindViewHolder(holder: CityVH, position: Int) = holder.bind(cities[position])

    inner class CityVH(private val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cityName: String) {
            binding.cityName.text = cityName
            binding.root.setOnClickListener { onClick(cityName) }
        }
    }
}
