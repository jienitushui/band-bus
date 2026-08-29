package uno.keyin.bus

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import uno.keyin.bus.databinding.ActivityCitySelectionBinding

class CitySelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCitySelectionBinding
    private lateinit var adapter: CityAdapter
    private var allCities: List<String> = emptyList()
    private var selecting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CityAdapter(::selectCity)
        binding.cityList.layoutManager = LinearLayoutManager(this)
        binding.cityList.adapter = adapter
        binding.btnBack.setOnClickListener { finish() }
        binding.inputCitySearch.doAfterTextChanged { filter(it?.toString().orEmpty()) }

        val cached = CityConfigStore.getCachedCityList(this)
        if (cached.isNotEmpty()) applyCities(cached)
        loadCities()
    }

    private fun loadCities() {
        showLoading(allCities.isEmpty(), getString(R.string.city_loading))
        BusApiClient.executor.execute {
            val result = runCatching { BusApiClient.fetchCities() }
            runOnUiThread {
                result.onSuccess {
                    CityConfigStore.cacheCityList(this, it)
                    applyCities(it)
                    showLoading(false, "")
                }.onFailure {
                    showLoading(allCities.isEmpty(), getString(R.string.city_load_failed))
                }
            }
        }
    }

    private fun applyCities(cities: List<String>) {
        val recent = CityConfigStore.getRecent(this)
        allCities = (recent + cities).distinct()
        filter(binding.inputCitySearch.text?.toString().orEmpty())
    }

    private fun filter(query: String) {
        val keyword = query.trim()
        adapter.submitList(
            if (keyword.isEmpty()) allCities else allCities.filter { it.contains(keyword, true) },
        )
    }

    private fun selectCity(cityName: String) {
        if (selecting) return
        selecting = true
        showLoading(true, getString(R.string.city_validating))
        BusApiClient.executor.execute {
            val valid = runCatching { BusApiClient.validateCity(cityName) }.getOrDefault(false)
            runOnUiThread {
                selecting = false
                if (!valid) {
                    showLoading(false, "")
                    Toast.makeText(this, R.string.city_unavailable, Toast.LENGTH_SHORT).show()
                    return@runOnUiThread
                }
                CityConfigStore.save(this, cityName)
                LocationRelayService.pushCityConfig(this)
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun showLoading(show: Boolean, text: String) {
        binding.cityState.visibility = if (show) View.VISIBLE else View.GONE
        binding.cityState.text = text
    }
}
