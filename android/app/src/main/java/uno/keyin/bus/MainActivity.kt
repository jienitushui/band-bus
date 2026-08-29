package uno.keyin.bus

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.json.JSONObject
import org.json.JSONArray
import uno.keyin.bus.databinding.ActivityMainBinding
import uno.keyin.bus.location.PhoneLocationCache
import uno.keyin.bus.location.PhoneLocationHelper
import uno.keyin.bus.wear.XmsWearSdkBridge

class MainActivity : AppCompatActivity() {

    companion object {
        private const val KEY_EXCLUDE_FROM_RECENTS = "exclude_from_recents"
    }

    private lateinit var binding: ActivityMainBinding
    private val homeAdapter = BusHomeNearbyAdapter(::openNearbyStation)
    private val searchAdapter = BusSearchAdapter(::onSearchResultClicked)
    private val routeAdapter = TransferAdapter(::syncRoutePlan)
    private lateinit var routeSuggestionAdapter: TransferStationAdapter
    private var currentNodeId: String? = null
    private val mainHandler = Handler(Looper.getMainLooper())
    private val resumeRefreshRunnable = Runnable { refreshNodes() }

    private var sendLocationAfterLocationGrant = false
    private var sendLocationInFlight = false
    private var loadedCityVersion = Long.MIN_VALUE
    private var homeLoadGeneration = 0
    private var homeStateText: String? = null
    private var searchGeneration = 0
    private var nearbyStations: List<StationUi> = emptyList()
    private var nearbyStateText: String? = null
    private var hasNearbySnapshot = false
    private val searchRunnable = Runnable { runSearch(binding.inputSearch.text?.toString().orEmpty()) }
    private var routeSuggestionGeneration = 0
    private var activeRouteField = 1
    private var searchRouteAfterLocationGrant = false
    private val routeSuggestionRunnable = Runnable { loadRouteSuggestions() }

    private val citySelectionLauncher = registerForActivityResult(StartActivityForResult()) {
        binding.inputSearch.setText("")
        searchAdapter.submitList(emptyList())
        applyCityAndRefresh(force = false)
    }

    private val notifPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) {
        maybeStartLocationRelay()
    }

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { grants ->
        val fine = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarse = grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fine || coarse) {
            maybeStartLocationRelay()
            applyCityAndRefresh(force = true)
            if (sendLocationAfterLocationGrant) {
                sendLocationAfterLocationGrant = false
                sendCurrentLocationToWatch()
            }
            if (searchRouteAfterLocationGrant) {
                searchRouteAfterLocationGrant = false
                searchRouteTransfer()
            }
        } else {
            sendLocationAfterLocationGrant = false
            searchRouteAfterLocationGrant = false
        }
    }

    private enum class HomeSubTab { Nearby, Realtime }

    private var homeSubTab: HomeSubTab = HomeSubTab.Nearby

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerHomeStations.layoutManager = LinearLayoutManager(this)
        binding.recyclerHomeStations.adapter = homeAdapter
        binding.recyclerHomeStations.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && !recyclerView.canScrollVertically(1)) {
                    homeAdapter.loadMoreStations()
                }
                requestVisibleStationPreviews()
            }
        })
        binding.recyclerHomeStations.post { requestVisibleStationPreviews() }
        binding.recyclerSearchResults.layoutManager = LinearLayoutManager(this)
        binding.recyclerSearchResults.adapter = searchAdapter
        setupTransferPanel()
        binding.clearSearchHistory.setOnClickListener {
            SearchHistoryStore.clear(this, CityConfigStore.get(this))
            renderSearchHistory()
        }
        binding.cancelSearch.setOnClickListener { cancelHomeSearch() }
        renderSearchHistory()

        binding.btnOpenSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.btnSyncWatchLocation.setOnClickListener {
            applyCityAndRefresh(force = true)
            LocationRelayService.pushCityConfig(this)
            onSendLocationClicked()
        }
        binding.textHomeState.setOnClickListener { applyCityAndRefresh(force = true) }
        binding.citySelector.setOnClickListener {
            citySelectionLauncher.launch(Intent(this, CitySelectionActivity::class.java))
        }

        binding.tabNearby.setOnClickListener { selectHomeSubTab(HomeSubTab.Nearby) }
        binding.tabRealtime.setOnClickListener { selectHomeSubTab(HomeSubTab.Realtime) }
        selectHomeSubTab(HomeSubTab.Nearby)

        binding.inputSearch.doAfterTextChanged { e ->
            val keyword = e?.toString().orEmpty().trim()
            mainHandler.removeCallbacks(searchRunnable)
            if (keyword.isEmpty()) {
                searchGeneration += 1
                searchAdapter.submitList(emptyList())
                binding.cancelSearch.visibility = View.GONE
                binding.recyclerSearchResults.visibility = View.GONE
                homeAdapter.setSearchFilter("")
                renderSearchHistory()
                showMainSection(isHome = true)
                restoreNearbyAfterSearch()
            } else {
                binding.cancelSearch.visibility = View.VISIBLE
                binding.searchHistoryPanel.visibility = View.GONE
                binding.recyclerHomeStations.visibility = View.GONE
                binding.recyclerSearchResults.visibility = View.VISIBLE
                showHomeState(null)
                mainHandler.postDelayed(searchRunnable, 300)
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    showMainSection(isHome = true)
                    true
                }
                R.id.nav_route -> {
                    showTransferSection()
                    true
                }
                R.id.nav_favorites -> {
                    showMainSection(isHome = false)
                    binding.textPlaceholderTab.setText(R.string.placeholder_favorites)
                    true
                }
                R.id.nav_profile -> {
                    showMainSection(isHome = false)
                    binding.textPlaceholderTab.setText(R.string.placeholder_profile)
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.selectedItemId = R.id.nav_home
        showMainSection(isHome = true)

        requestPostNotificationsThenStartRelay()
        refreshNodes()
    }

    private fun showMainSection(isHome: Boolean) {
        binding.panelHomeHeader.visibility = if (isHome) View.VISIBLE else View.GONE
        if (!isHome) {
            binding.scrollPlaceholderTab.visibility = View.VISIBLE
            binding.panelTransfer.visibility = View.GONE
            binding.recyclerHomeStations.visibility = View.GONE
            binding.recyclerSearchResults.visibility = View.GONE
            binding.textHomeState.visibility = View.GONE
            return
        }
        binding.panelTransfer.visibility = View.GONE
        binding.scrollPlaceholderTab.visibility =
            if (homeSubTab == HomeSubTab.Realtime) View.VISIBLE else View.GONE
        binding.recyclerHomeStations.visibility =
            if (homeSubTab == HomeSubTab.Realtime || binding.inputSearch.text?.isNotBlank() == true) View.GONE else View.VISIBLE
        binding.recyclerSearchResults.visibility =
            if (homeSubTab == HomeSubTab.Realtime || binding.inputSearch.text?.isBlank() != false) View.GONE else View.VISIBLE
        binding.textHomeState.visibility =
            if (homeSubTab == HomeSubTab.Nearby && homeStateText != null) View.VISIBLE else View.GONE
        if (homeSubTab == HomeSubTab.Realtime) {
            binding.textPlaceholderTab.setText(R.string.home_realtime_empty)
        }
    }

    private fun selectHomeSubTab(tab: HomeSubTab) {
        homeSubTab = tab
        val gray = ContextCompat.getColor(this, R.color.tab_text_inactive)
        val dark = ContextCompat.getColor(this, R.color.tab_text_active)
        when (tab) {
            HomeSubTab.Nearby -> {
                binding.tabNearby.setTextColor(dark)
                binding.tabNearby.setTypeface(null, Typeface.BOLD)
                binding.tabRealtime.setTextColor(gray)
                binding.tabRealtime.setTypeface(null, Typeface.NORMAL)
                binding.tabIndicator.visibility = View.VISIBLE
            }
            HomeSubTab.Realtime -> {
                binding.tabNearby.setTextColor(gray)
                binding.tabNearby.setTypeface(null, Typeface.NORMAL)
                binding.tabRealtime.setTextColor(dark)
                binding.tabRealtime.setTypeface(null, Typeface.BOLD)
                binding.tabIndicator.visibility = View.INVISIBLE
                binding.textHomeState.visibility = View.GONE
            }
        }
        if (binding.bottomNavigation.selectedItemId == R.id.nav_home) {
            showMainSection(isHome = true)
        }
    }

    override fun onResume() {
        super.onResume()
        applyRecentTasksExcludeFromRecents(
            getSharedPreferences(RelayUiPrefs.PREFS_NAME, MODE_PRIVATE)
                .getBoolean(KEY_EXCLUDE_FROM_RECENTS, false),
        )
        mainHandler.removeCallbacks(resumeRefreshRunnable)
        mainHandler.postDelayed(resumeRefreshRunnable, 450)
        applyCityAndRefresh(force = false)
    }

    override fun onDestroy() {
        mainHandler.removeCallbacks(resumeRefreshRunnable)
        mainHandler.removeCallbacks(searchRunnable)
        mainHandler.removeCallbacks(routeSuggestionRunnable)
        super.onDestroy()
    }

    private fun setupTransferPanel() {
        binding.recyclerRouteSchemes.layoutManager = LinearLayoutManager(this)
        binding.recyclerRouteSchemes.adapter = routeAdapter
        routeSuggestionAdapter = TransferStationAdapter { value ->
            val target = if (activeRouteField == 1) binding.inputRouteStart else binding.inputRouteEnd
            target.setText(value)
            target.setSelection(value.length)
            hideRouteSuggestions(clearFocus = true)
        }
        binding.routeStationSuggestions.layoutManager = LinearLayoutManager(this)
        binding.routeStationSuggestions.adapter = routeSuggestionAdapter
        binding.inputRouteStart.setText(R.string.transfer_my_location)
        binding.inputRouteStart.doAfterTextChanged {
            if (!binding.inputRouteStart.hasFocus()) return@doAfterTextChanged
            activeRouteField = 1
            queueRouteSuggestions(it?.toString().orEmpty())
        }
        binding.inputRouteEnd.doAfterTextChanged {
            if (!binding.inputRouteEnd.hasFocus()) return@doAfterTextChanged
            activeRouteField = 2
            queueRouteSuggestions(it?.toString().orEmpty())
        }
        binding.inputRouteStart.setOnFocusChangeListener { _, focused ->
            if (focused) {
                activeRouteField = 1
                queueRouteSuggestions(binding.inputRouteStart.text?.toString().orEmpty())
            } else if (!binding.inputRouteEnd.hasFocus()) {
                hideRouteSuggestions()
            }
        }
        binding.inputRouteEnd.setOnFocusChangeListener { _, focused ->
            if (focused) {
                activeRouteField = 2
                queueRouteSuggestions(binding.inputRouteEnd.text?.toString().orEmpty())
            } else if (!binding.inputRouteStart.hasFocus()) {
                hideRouteSuggestions()
            }
        }
        binding.btnRouteSearch.setOnClickListener {
            hideRouteSuggestions(clearFocus = true)
            searchRouteTransfer()
        }
    }

    private fun showTransferSection() {
        binding.panelHomeHeader.visibility = View.GONE
        binding.scrollPlaceholderTab.visibility = View.GONE
        binding.recyclerHomeStations.visibility = View.GONE
        binding.recyclerSearchResults.visibility = View.GONE
        binding.textHomeState.visibility = View.GONE
        binding.panelTransfer.visibility = View.VISIBLE
    }

    private fun queueRouteSuggestions(value: String) {
        mainHandler.removeCallbacks(routeSuggestionRunnable)
        val keyword = value.trim()
        if (keyword.isEmpty() || keyword == getString(R.string.transfer_my_location)) {
            routeSuggestionGeneration += 1
            routeSuggestionAdapter.submitList(emptyList())
            binding.routeStationSuggestions.visibility = View.GONE
            return
        }
        mainHandler.postDelayed(routeSuggestionRunnable, 300)
    }

    private fun hideRouteSuggestions(clearFocus: Boolean = false) {
        mainHandler.removeCallbacks(routeSuggestionRunnable)
        routeSuggestionGeneration += 1
        routeSuggestionAdapter.submitList(emptyList())
        binding.routeStationSuggestions.visibility = View.GONE
        if (clearFocus) {
            binding.inputRouteStart.clearFocus()
            binding.inputRouteEnd.clearFocus()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (
            event.action == MotionEvent.ACTION_DOWN &&
            ::binding.isInitialized &&
            binding.panelTransfer.visibility == View.VISIBLE &&
            binding.routeStationSuggestions.visibility == View.VISIBLE &&
            !event.isInside(binding.inputRouteStart) &&
            !event.isInside(binding.inputRouteEnd) &&
            !event.isInside(binding.routeStationSuggestions)
        ) {
            hideRouteSuggestions(clearFocus = true)
        }
        return super.dispatchTouchEvent(event)
    }

    private fun MotionEvent.isInside(view: View): Boolean {
        if (view.visibility != View.VISIBLE) return false
        val bounds = Rect()
        return view.getGlobalVisibleRect(bounds) && bounds.contains(rawX.toInt(), rawY.toInt())
    }

    private fun loadRouteSuggestions() {
        val keyword = (if (activeRouteField == 1) binding.inputRouteStart else binding.inputRouteEnd)
            .text?.toString()?.trim().orEmpty()
        if (keyword.isEmpty()) return
        val city = CityConfigStore.get(this)
        val generation = ++routeSuggestionGeneration
        BusApiClient.executor.execute {
            val result = runCatching { BusApiClient.searchStations(city, keyword) }
            mainHandler.post {
                if (generation != routeSuggestionGeneration) return@post
                result.onSuccess { values ->
                    routeSuggestionAdapter.submitList(values)
                    val target = if (activeRouteField == 1) binding.inputRouteStart else binding.inputRouteEnd
                    binding.routeStationSuggestions.visibility =
                        if (values.isEmpty() || !target.hasFocus()) View.GONE else View.VISIBLE
                }.onFailure { binding.routeStationSuggestions.visibility = View.GONE }
            }
        }
    }

    private fun searchRouteTransfer() {
        binding.routeStationSuggestions.visibility = View.GONE
        val start = binding.inputRouteStart.text?.toString()?.trim().orEmpty()
        val end = binding.inputRouteEnd.text?.toString()?.trim().orEmpty()
        if (start.isEmpty() || end.isEmpty()) {
            binding.textRouteState.setText(R.string.transfer_input_required)
            return
        }
        val city = CityConfigStore.get(this)
        binding.textRouteState.setText(R.string.transfer_loading)
        if (start != getString(R.string.transfer_my_location)) {
            executeTransferQuery(city, start, end, "", "")
            return
        }
        if (!PhoneLocationHelper.hasLocationPermission(this)) {
            searchRouteAfterLocationGrant = true
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            )
            return
        }
        PhoneLocationHelper.fetchBestLocation(this, highAccuracy = true) { location ->
            mainHandler.post {
                if (location == null) {
                    binding.textRouteState.setText(R.string.transfer_location_failed)
                    return@post
                }
                val converted = uno.keyin.bus.location.Wgs84ToGcj02.convert(location.latitude, location.longitude)
                executeTransferQuery(city, start, end, converted.first.toString(), converted.second.toString())
            }
        }
    }

    private fun executeTransferQuery(city: CityConfig, start: String, end: String, lat: String, lng: String) {
        BusApiClient.executor.execute {
            val result = runCatching { BusApiClient.loadTransfer(city, start, end, lat, lng) }
            mainHandler.post {
                result.onSuccess { schemes ->
                    routeAdapter.submitList(schemes)
                    binding.textRouteState.text = if (schemes.isEmpty()) {
                        getString(R.string.transfer_empty)
                    } else {
                        "共 ${schemes.size} 个方案，上下滑动查看全部"
                    }
                }.onFailure { binding.textRouteState.setText(R.string.transfer_failed) }
            }
        }
    }

    private fun syncRoutePlan(scheme: TransferScheme) {
        val summary = scheme.routeSummary()
        if (summary.isBlank()) {
            binding.textRouteState.setText(R.string.transfer_plan_incomplete)
            return
        }
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
            put("summary", summary)
            put("ts", System.currentTimeMillis())
        }.toString()
        LocationRelayService.pushTransferPlan(this, payload)
        binding.textRouteState.setText(R.string.transfer_sync_requested)
    }

    private fun runSearch(keyword: String) {
        val query = keyword.trim()
        if (query.isEmpty()) return
        val city = CityConfigStore.get(this)
        val generation = ++searchGeneration
        searchAdapter.submitList(emptyList())
        BusApiClient.executor.execute {
            val result = runCatching { BusApiClient.search(city, query) }
            mainHandler.post {
                if (generation != searchGeneration || CityConfigStore.get(this).version != city.version) return@post
                result.onSuccess { items ->
                    searchAdapter.submitList(items)
                    if (items.isNotEmpty()) {
                        SearchHistoryStore.add(this, city, query)
                    }
                    showHomeState(if (items.isEmpty()) getString(R.string.home_search_empty) else null)
                }.onFailure {
                    searchAdapter.submitList(emptyList())
                    showHomeState(getString(R.string.home_search_failed))
                }
            }
        }
    }

    private fun cancelHomeSearch() {
        mainHandler.removeCallbacks(searchRunnable)
        searchGeneration += 1
        binding.inputSearch.setText("")
        binding.inputSearch.clearFocus()
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(binding.inputSearch.windowToken, 0)
    }

    private fun restoreNearbyAfterSearch() {
        if (homeSubTab != HomeSubTab.Nearby) return
        if (hasNearbySnapshot) {
            homeAdapter.submitList(nearbyStations)
            binding.recyclerHomeStations.post { requestVisibleStationPreviews() }
            showHomeState(nearbyStateText)
        } else if (PhoneLocationHelper.hasLocationPermission(this)) {
            loadNearbyForCity(CityConfigStore.get(this))
        }
    }

    private fun loadNearbyStationLines(station: StationUi) {
        if (station.linesState == StationLinesState.LOADING || station.linesState == StationLinesState.LOADED) return
        val city = CityConfigStore.get(this)
        val generation = homeLoadGeneration
        updateNearbyStation(station.copy(linesState = StationLinesState.LOADING))
        StationLinesRepository.load(city, station.name, station.lat, station.lng) { result ->
            mainHandler.post {
                if (generation != homeLoadGeneration || CityConfigStore.get(this).version != city.version) {
                    return@post
                }
                result.onSuccess { lines ->
                    updateNearbyStation(station.copy(buses = lines, linesState = StationLinesState.LOADED))
                }.onFailure {
                    updateNearbyStation(station.copy(linesState = StationLinesState.ERROR))
                }
            }
        }
    }

    private fun requestVisibleStationPreviews() {
        if (homeSubTab != HomeSubTab.Nearby || binding.recyclerHomeStations.visibility != View.VISIBLE) return
        val manager = binding.recyclerHomeStations.layoutManager as? LinearLayoutManager ?: return
        val first = manager.findFirstVisibleItemPosition()
        val last = manager.findLastVisibleItemPosition()
        if (first == RecyclerView.NO_POSITION || last == RecyclerView.NO_POSITION) return
        for (position in first..last) {
            homeAdapter.stationAt(position)?.let(::loadNearbyStationLines)
        }
    }

    private fun updateNearbyStation(station: StationUi) {
        nearbyStations = nearbyStations.map { if (it.key == station.key) station else it }
        homeAdapter.updateStation(station)
    }

    private fun openNearbyStation(station: StationUi) {
        startActivity(Intent(this, StationDetailActivity::class.java).apply {
            putExtra(StationDetailActivity.EXTRA_STATION_NAME, station.name)
            putExtra(StationDetailActivity.EXTRA_STATION_LAT, station.lat)
            putExtra(StationDetailActivity.EXTRA_STATION_LNG, station.lng)
        })
    }

    private fun onSearchResultClicked(item: SearchResult) {
        if (item.type == SearchResultType.STATION) {
            cancelHomeSearch()
            startActivity(Intent(this, StationDetailActivity::class.java).apply {
                putExtra(StationDetailActivity.EXTRA_STATION_NAME, item.name)
            })
            return
        }
        startActivity(Intent(this, LineDetailActivity::class.java).apply {
            putExtra(LineDetailActivity.EXTRA_LINE_NAME, item.lineName)
            putExtra(LineDetailActivity.EXTRA_DIRECTION, item.direction)
        })
    }

    private fun renderSearchHistory() {
        val panel = binding.searchHistoryPanel
        val group = binding.searchHistoryChips
        group.removeAllViews()
        val values = SearchHistoryStore.get(this, CityConfigStore.get(this))
        panel.visibility = if (values.isEmpty() || binding.inputSearch.text?.isNotBlank() == true) View.GONE else View.VISIBLE
        values.forEach { keyword ->
            val chip = Chip(this).apply {
                text = keyword
                isCloseIconVisible = true
                isCheckable = false
                setOnClickListener { binding.inputSearch.setText(keyword) }
                setOnCloseIconClickListener {
                    SearchHistoryStore.remove(this@MainActivity, CityConfigStore.get(this@MainActivity), keyword)
                    renderSearchHistory()
                }
            }
            group.addView(chip)
        }
    }

    private fun requestPostNotificationsThenStartRelay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                notifPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                return
            }
        }
        maybeStartLocationRelay()
    }

    private fun maybeStartLocationRelay() {
        if (!XmsWearSdkBridge.isSdkOnClasspath()) return
        if (!PhoneLocationHelper.hasLocationPermission(this)) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            )
            Toast.makeText(this, R.string.toast_requesting_location, Toast.LENGTH_SHORT).show()
            return
        }
        if (!LocationRelayService.start(this)) return
        Toast.makeText(
            this,
            getString(R.string.log_relay_started) + "\n" + getString(R.string.log_phone_relay_must_resident),
            Toast.LENGTH_LONG,
        ).show()
    }

    private fun applyRecentTasksExcludeFromRecents(exclude: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        runCatching {
            am.appTasks.forEach { task ->
                task.setExcludeFromRecents(exclude)
            }
        }
    }

    private fun refreshNodes() {
        binding.textWatchStatus.setText(R.string.home_watch_status_query)
        XmsWearSdkBridge.fetchConnectedNodeIds(
            this,
            onResult = { ids ->
                mainHandler.post {
                    if (ids.isEmpty()) {
                        currentNodeId = null
                        binding.textWatchStatus.setText(R.string.home_watch_status_none)
                    } else {
                        currentNodeId = ids.first()
                        binding.textWatchStatus.text =
                            getString(R.string.home_watch_status_one, currentNodeId!!)
                    }
                }
            },
            onError = { msg ->
                mainHandler.post {
                    binding.textWatchStatus.text = getString(R.string.home_watch_status_err, msg)
                }
            },
        )
    }

    private fun applyCityAndRefresh(force: Boolean) {
        val city = CityConfigStore.get(this)
        binding.textCityName.text = city.displayName
        if (!force && loadedCityVersion == city.version) return
        if (loadedCityVersion != city.version) {
            nearbyStations = emptyList()
            nearbyStateText = null
            hasNearbySnapshot = false
        }
        loadedCityVersion = city.version
        renderSearchHistory()
        homeAdapter.submitList(emptyList())
        if (!PhoneLocationHelper.hasLocationPermission(this)) {
            showHomeState(getString(R.string.toast_requesting_location))
            return
        }
        loadNearbyForCity(city)
    }

    private fun loadNearbyForCity(city: CityConfig) {
        homeLoadGeneration += 1
        val generation = homeLoadGeneration
        showHomeState(getString(R.string.home_nearby_loading))
        PhoneLocationHelper.fetchBestLocation(this, highAccuracy = true) { location ->
            if (location == null) {
                mainHandler.post {
                    if (generation == homeLoadGeneration) {
                        showHomeState(getString(R.string.toast_location_failed))
                    }
                }
                return@fetchBestLocation
            }
            val converted = uno.keyin.bus.location.Wgs84ToGcj02.convert(
                location.latitude,
                location.longitude,
            )
            BusApiClient.executor.execute {
                val result = runCatching {
                    BusApiClient.loadNearby(city, converted.first, converted.second)
                }
                mainHandler.post {
                    if (generation != homeLoadGeneration || CityConfigStore.get(this).version != city.version) {
                        return@post
                    }
                    result.onSuccess { stations ->
                        nearbyStations = stations
                        nearbyStateText = if (stations.isEmpty()) getString(R.string.home_nearby_empty) else null
                        hasNearbySnapshot = true
                        homeAdapter.submitList(stations)
                        binding.recyclerHomeStations.post { requestVisibleStationPreviews() }
                        showHomeState(nearbyStateText)
                    }.onFailure {
                        nearbyStations = emptyList()
                        nearbyStateText = getString(R.string.home_nearby_failed)
                        hasNearbySnapshot = true
                        homeAdapter.submitList(emptyList())
                        showHomeState(nearbyStateText)
                    }
                }
            }
        }
    }

    private fun showHomeState(message: String?) {
        homeStateText = message
        binding.textHomeState.text = message.orEmpty()
        binding.textHomeState.visibility =
            if (message != null && homeSubTab == HomeSubTab.Nearby &&
                binding.bottomNavigation.selectedItemId == R.id.nav_home
            ) View.VISIBLE else View.GONE
    }

    private fun onSendLocationClicked() {
        if (currentNodeId == null) {
            Toast.makeText(this, R.string.toast_need_watch_node, Toast.LENGTH_SHORT).show()
            return
        }
        if (!PhoneLocationHelper.hasLocationPermission(this)) {
            sendLocationAfterLocationGrant = true
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            )
            Toast.makeText(this, R.string.toast_requesting_location, Toast.LENGTH_SHORT).show()
            return
        }
        sendCurrentLocationToWatch()
    }

    private fun sendCurrentLocationToWatch() {
        val id = currentNodeId ?: return
        if (!PhoneLocationHelper.hasLocationPermission(this)) return

        val quick = PhoneLocationCache.peekForQuickReply(
            PhoneLocationCache.QUICK_REPLY_MAX_WALL_MS,
            PhoneLocationCache.QUICK_REPLY_MAX_FIX_AGE_MS,
        )
        if (quick != null) {
            val json = PhoneLocationPayload.toJson(quick)
            XmsWearSdkBridge.sendTextToNode(
                this,
                id,
                json,
                onOk = { Toast.makeText(this, R.string.toast_location_sent, Toast.LENGTH_SHORT).show() },
                onErr = { Toast.makeText(this, getString(R.string.toast_send_fail, it), Toast.LENGTH_SHORT).show() },
            )
            PhoneLocationHelper.fetchBestLocation(this, highAccuracy = true) { }
            return
        }
        if (sendLocationInFlight) {
            Toast.makeText(this, R.string.toast_location_in_progress, Toast.LENGTH_SHORT).show()
            return
        }
        sendLocationInFlight = true
        PhoneLocationHelper.fetchBestLocation(this, highAccuracy = true) { loc ->
            mainHandler.post {
                sendLocationInFlight = false
                if (loc == null) {
                    Toast.makeText(this, R.string.toast_location_failed, Toast.LENGTH_SHORT).show()
                    return@post
                }
                val json = PhoneLocationPayload.toJson(loc)
                XmsWearSdkBridge.sendTextToNode(
                    this@MainActivity,
                    id,
                    json,
                    onOk = { Toast.makeText(this, R.string.toast_location_sent, Toast.LENGTH_SHORT).show() },
                    onErr = { Toast.makeText(this, getString(R.string.toast_send_fail, it), Toast.LENGTH_SHORT).show() },
                )
            }
        }
    }
}
