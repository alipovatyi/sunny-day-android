package dev.arli.sunnyday.ui.locations.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.arli.sunnyday.model.CurrentWeather
import dev.arli.sunnyday.model.LocationWithCurrentWeather
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.ui.common.preview.SunnyDayThemePreview
import java.time.LocalDateTime

@Composable
internal fun LocationList(
    locations: List<LocationWithCurrentWeather>,
    showCurrentLocationPlaceholder: Boolean,
    onCurrentLocationPlaceholderClick: () -> Unit,
    onLocationClick: (LocationWithCurrentWeather) -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 96.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        if (showCurrentLocationPlaceholder) {
            item { CurrentLocationPlaceholder(onClick = onCurrentLocationPlaceholderClick) }
        }
        items(
            items = locations,
            key = { "${it.coordinates.latitude.value}:${it.coordinates.longitude.value}" }
        ) { location ->
            LocationListItem(
                location = location,
                onClick = { onLocationClick(location) }
            )
        }
    }
}

@Preview
@Composable
private fun LocationListPreview() {
    SunnyDayThemePreview {
        LocationList(
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
            ),
            showCurrentLocationPlaceholder = false,
            onCurrentLocationPlaceholderClick = {},
            onLocationClick = {}
        )
    }
}

@Preview
@Composable
private fun LocationListWithCurrentLocationPlaceholderPreview() {
    SunnyDayThemePreview {
        LocationList(
            locations = listOf(
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
            ),
            showCurrentLocationPlaceholder = true,
            onCurrentLocationPlaceholderClick = {},
            onLocationClick = {}
        )
    }
}
