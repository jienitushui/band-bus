package uno.keyin.bus

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import uno.keyin.bus.databinding.ActivityMainBinding
import uno.keyin.bus.location.PhoneLocationCache
import uno.keyin.bus.location.PhoneLocationHelper
import uno.keyin.bus.wear.XmsWearSdkBridge

class MainActivity : AppCompatActivity() {

    companion object {
        private const val KEY_EXCLUDE_FROM_RECENTS = "exclude_from_recents"
    }

    private lateinit var binding: ActivityMainBinding
    private val homeAdapter = BusHomeNearbyAdapter()
    private var currentNodeId: String? = null
    private val mainHandler = Handler(Looper.getMainLooper())
    private val resumeRefreshRunnable = Runnable { refreshNodes() }

    private var sendLocationAfterLocationGrant = false
    private var sendLocationInFlight = false
    private var loadedCityVersion = Long.MIN_VALUE
    private var homeLoadGeneration = 0
    private var homeStateText: String? = null

    private val citySelectionLauncher = registerForActivityResult(StartActivityForResult()) {
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
        } else {
            sendLocationAfterLocationGrant = false
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
            homeAdapter.setSearchFilter(e?.toString().orEmpty())
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    showMainSection(isHome = true)
                    true
                }
                R.id.nav_route -> {
                    showMainSection(isHome = false)
                    binding.textPlaceholderTab.setText(R.string.placeholder_route_plan)
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
            binding.recyclerHomeStations.visibility = View.GONE
            binding.textHomeState.visibility = View.GONE
            return
        }
        binding.scrollPlaceholderTab.visibility =
            if (homeSubTab == HomeSubTab.Realtime) View.VISIBLE else View.GONE
        binding.recyclerHomeStations.visibility =
            if (homeSubTab == HomeSubTab.Realtime) View.GONE else View.VISIBLE
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
        super.onDestroy()
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
        loadedCityVersion = city.version
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
                        homeAdapter.submitList(stations)
                        showHomeState(
                            if (stations.isEmpty()) getString(R.string.home_nearby_empty) else null,
                        )
                    }.onFailure {
                        homeAdapter.submitList(emptyList())
                        showHomeState(getString(R.string.home_nearby_failed))
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
