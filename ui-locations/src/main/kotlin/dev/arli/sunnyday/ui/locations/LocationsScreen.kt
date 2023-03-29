package dev.arli.sunnyday.ui.locations

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dev.arli.sunnyday.model.CurrentWeather
import dev.arli.sunnyday.model.LocationWithCurrentWeather
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.resources.R
import dev.arli.sunnyday.ui.common.contract.GoogleLocationSelector
import dev.arli.sunnyday.ui.common.preview.SunnyDayThemePreview
import dev.arli.sunnyday.ui.locations.components.LocationList
import dev.arli.sunnyday.ui.locations.contract.LocationsEffect
import dev.arli.sunnyday.ui.locations.contract.LocationsEvent
import dev.arli.sunnyday.ui.locations.contract.LocationsViewState
import java.net.URL
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import kotlin.math.max

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationsScreen(
    viewModel: LocationsViewModel,
    openLocationDetails: (Coordinates) -> Unit,
    openUrl: (URL) -> Unit
) {
    val viewState by viewModel.viewState.collectAsState()
    val lazyListState = rememberLazyListState()
    val googleLocationSelectorLauncher = rememberLauncherForActivityResult(
        contract = GoogleLocationSelector,
        onResult = { result ->
            result.orNull()?.let { viewModel.onEventSent(LocationsEvent.AddLocation(it)) }
        }
    )
    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_COARSE_LOCATION,
        onPermissionResult = { isGranted ->
            viewModel.onEventSent(LocationsEvent.LocationPermissionStateChange(isGranted = isGranted))
        }
    )

    LaunchedEffect(Unit) {
        if (locationPermissionState.status.isGranted.not()) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is LocationsEffect.OpenAddLocation -> googleLocationSelectorLauncher.launch()
                is LocationsEffect.OpenLocationDetails -> openLocationDetails(effect.coordinates)
                is LocationsEffect.ScrollToBottom -> {
                    lazyListState.scrollToItem(max(0, viewState.locations.lastIndex))
                }
                is LocationsEffect.OpenUrl -> openUrl(effect.url)
            }
        }.collect()
    }

    LocationsScreen(
        viewState = viewState,
        lazyListState = lazyListState,
        locationPermissionGranted = locationPermissionState.status.isGranted,
        onEventSent = viewModel::onEventSent,
        onRequestLocationPermissionClick = {
            if (locationPermissionState.status.shouldShowRationale) {
                // TODO
            } else {
                locationPermissionState.launchPermissionRequest()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun LocationsScreen(
    viewState: LocationsViewState,
    locationPermissionGranted: Boolean,
    onRequestLocationPermissionClick: () -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
    onEventSent: (LocationsEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewState.isRefreshing,
        onRefresh = { onEventSent(LocationsEvent.Refresh) }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.my_locations))
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            if (viewState.showAddLocationButton) {
                FloatingActionButton(onClick = { onEventSent(LocationsEvent.AddLocationClick) }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add_location)
                    )
                }
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .pullRefresh(pullRefreshState)
        ) {
            LocationList(
                lazyListState = lazyListState,
                locations = viewState.locations,
                showCurrentLocationPlaceholder = locationPermissionGranted.not(),
                onCurrentLocationPlaceholderClick = onRequestLocationPermissionClick,
                onLocationClick = { onEventSent(LocationsEvent.LocationClick(it)) },
                onCopyrightClick = { onEventSent(LocationsEvent.CopyrightClick) }
            )

            PullRefreshIndicator(
                refreshing = viewState.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun LocationsScreenPreview() {
    SunnyDayThemePreview {
        LocationsScreen(
            viewState = LocationsViewState(
                locations = listOf(
                    LocationWithCurrentWeather(
                        coordinates = Coordinates(
                            latitude = Latitude(52.23),
                            longitude = Longitude(21.01)
                        ),
                        name = "Warsaw",
                        isCurrent = true,
                        currentWeather = CurrentWeather(
                            latitude = Latitude(52.23),
                            longitude = Longitude(21.01),
                            temperature = 12.6,
                            windSpeed = 13.2,
                            windDirection = 244,
                            weatherCode = 80,
                            time = LocalDateTime.parse("2023-03-25T15:00")
                        )
                    ),
                    LocationWithCurrentWeather(
                        coordinates = Coordinates(
                            latitude = Latitude(50.45),
                            longitude = Longitude(30.52)
                        ),
                        name = "Kyiv",
                        isCurrent = false,
                        currentWeather = CurrentWeather(
                            latitude = Latitude(50.45),
                            longitude = Longitude(30.52),
                            temperature = 10.0,
                            windSpeed = 25.0,
                            windDirection = 90,
                            weatherCode = 1,
                            time = LocalDateTime.parse("2023-03-25T15:00")
                        )
                    )
                )
            ),
            locationPermissionGranted = true,
            onEventSent = {},
            onRequestLocationPermissionClick = {}
        )
    }
}

@Preview
@Composable
private fun LocationsScreenEmptyPreview() {
    SunnyDayThemePreview {
        LocationsScreen(
            viewState = LocationsViewState(),
            locationPermissionGranted = true,
            onEventSent = {},
            onRequestLocationPermissionClick = {}
        )
    }
}
