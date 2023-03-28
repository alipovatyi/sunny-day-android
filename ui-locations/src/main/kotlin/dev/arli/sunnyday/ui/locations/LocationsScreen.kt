package dev.arli.sunnyday.ui.locations

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.launch
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.arli.sunnyday.model.CurrentWeather
import dev.arli.sunnyday.model.LocationWithCurrentWeather
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.resources.R
import dev.arli.sunnyday.ui.common.contract.GoogleLocationSelector
import dev.arli.sunnyday.ui.common.preview.SunnyDayThemePreview
import dev.arli.sunnyday.ui.locations.components.LocationEmptyState
import dev.arli.sunnyday.ui.locations.components.LocationList
import dev.arli.sunnyday.ui.locations.contract.LocationsEffect
import dev.arli.sunnyday.ui.locations.contract.LocationsEvent
import dev.arli.sunnyday.ui.locations.contract.LocationsViewState
import java.time.LocalDateTime
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun LocationsScreen(
    viewModel: LocationsViewModel
) {
    val viewState by viewModel.viewState.collectAsState()
    val lazyListState = rememberLazyListState()
    val googleLocationSelectorLauncher = rememberLauncherForActivityResult(
        contract = GoogleLocationSelector,
        onResult = { result ->
            result.orNull()?.let { viewModel.onEventSent(LocationsEvent.AddLocation(it)) }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.effect.onEach { effect ->
            when(effect) {
                LocationsEffect.OpenAddLocation -> googleLocationSelectorLauncher.launch()
                is LocationsEffect.OpenLocationDetails -> {
                    // TODO
                }
            }
        }.collect()
    }

    LocationsScreen(
        viewState = viewState,
        lazyListState = lazyListState,
        onEventSent = viewModel::onEventSent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationsScreen(
    viewState: LocationsViewState,
    lazyListState: LazyListState = rememberLazyListState(),
    onEventSent: (LocationsEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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
            if (viewState.locations.isNotEmpty()) {
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
        if (viewState.locations.isEmpty()) {
            LocationEmptyState(
                onAddLocationClick = { onEventSent(LocationsEvent.AddLocationClick) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            )
        } else {
            LocationList(
                lazyListState = lazyListState,
                locations = viewState.locations,
                onLocationClick = { onEventSent(LocationsEvent.LocationClick(it)) },
                modifier = Modifier.padding(contentPadding)
            )
        }
    }
}

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
                    ), LocationWithCurrentWeather(
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
            onEventSent = {}
        )
    }
}

@Preview
@Composable
private fun LocationsScreenEmptyPreview() {
    SunnyDayThemePreview {
        LocationsScreen(
            viewState = LocationsViewState(),
            onEventSent = {}
        )
    }
}
