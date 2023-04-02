package dev.arli.sunnyday.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import dev.arli.sunnyday.model.CurrentWeather
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.weather.DailyForecast
import dev.arli.sunnyday.model.weather.HourlyForecast
import dev.arli.sunnyday.model.weather.WeatherCode
import dev.arli.sunnyday.resources.R
import dev.arli.sunnyday.ui.common.components.CopyrightButton
import dev.arli.sunnyday.ui.common.components.CrossfadeVisibility
import dev.arli.sunnyday.ui.common.preview.SunnyDayThemePreview
import dev.arli.sunnyday.ui.details.components.AirConditionsSection
import dev.arli.sunnyday.ui.details.components.CurrentWeatherSection
import dev.arli.sunnyday.ui.details.components.DailyForecastSection
import dev.arli.sunnyday.ui.details.components.HourlyForecastSection
import dev.arli.sunnyday.ui.details.components.SunTimeSection
import dev.arli.sunnyday.ui.details.contract.LocationDetailsEffect
import dev.arli.sunnyday.ui.details.contract.LocationDetailsEvent
import dev.arli.sunnyday.ui.details.contract.LocationDetailsViewState
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun LocationDetailsScreen(
    viewModel: LocationDetailsViewModel,
    navigateUp: () -> Unit,
    openUrl: (URL) -> Unit
) {
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is LocationDetailsEffect.NavigateUp -> navigateUp()
                is LocationDetailsEffect.OpenUrl -> openUrl(effect.url)
            }
        }.collect()
    }

    LocationDetailsScreen(
        viewState = viewState,
        onEventSent = viewModel::onEventSent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationDetailsScreen(
    viewState: LocationDetailsViewState,
    onEventSent: (LocationDetailsEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = viewState.locationName.orEmpty())
                        if (viewState.isCurrent) {
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onEventSent(LocationDetailsEvent.BackClick) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.go_back)
                        )
                    }
                },
                actions = {
                    if (viewState.isCurrent.not()) {
                        IconButton(onClick = { onEventSent(LocationDetailsEvent.DeleteClick) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(id = R.string.delete_location)
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val currentWeather = viewState.currentWeather
            val currentHourlyForecast = viewState.currentHourlyForecast
            val currentDailyForecast = viewState.currentDailyForecast

            CrossfadeVisibility(currentWeather != null) {
                Column {
                    Spacer(Modifier.height(24.dp))

                    if (currentWeather != null) {
                        CurrentWeatherSection(
                            currentWeather = currentWeather,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(Modifier.height(32.dp))

                    HourlyForecastSection(
                        hourlyForecasts = viewState.hourlyForecasts,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    DailyForecastSection(
                        dailyForecasts = viewState.dailyForecasts,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    if (currentDailyForecast != null && currentHourlyForecast != null) {
                        AirConditionsSection(
                            currentDailyForecast = currentDailyForecast,
                            currentHourlyForecast = currentHourlyForecast,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    if (currentDailyForecast != null) {
                        SunTimeSection(
                            currentDailyForecast = currentDailyForecast,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    CopyrightButton(
                        onClick = { onEventSent(LocationDetailsEvent.CopyrightClick) },
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LocationDetailsScreenPreview() {
    SunnyDayThemePreview {
        LocationDetailsScreen(
            viewState = LocationDetailsViewState(
                locationName = "Warsaw",
                isCurrent = true,
                currentWeather = CurrentWeather(
                    latitude = Latitude(52.23),
                    longitude = Longitude(21.01),
                    temperature = 12.6,
                    windSpeed = 13.2,
                    windDirection = 244,
                    weatherCode = WeatherCode.RainShowersSlight,
                    time = LocalDateTime.parse("2023-03-25T15:00")
                ),
                dailyForecasts = listOf(
                    DailyForecast(
                        latitude = Latitude(52.23),
                        longitude = Longitude(21.01),
                        time = LocalDate.parse("2023-03-25"),
                        weatherCode = WeatherCode.ThunderstormSlightOrModerate,
                        temperature2mMax = 13.8,
                        temperature2mMin = 7.4,
                        apparentTemperatureMax = 11.1,
                        apparentTemperatureMin = 4.9,
                        sunrise = LocalDateTime.parse("2023-03-25T05:25"),
                        sunset = LocalDateTime.parse("2023-03-25T17:58"),
                        uvIndexMax = 4.30
                    )
                ),
                hourlyForecasts = listOf(
                    HourlyForecast(
                        latitude = Latitude(52.23),
                        longitude = Longitude(21.01),
                        time = LocalDateTime.parse("2023-03-25T00:00"),
                        temperature2m = 11.8,
                        relativeHumidity2m = 92,
                        dewPoint2m = 10.5,
                        apparentTemperature = 10.9,
                        precipitationProbability = 30,
                        precipitation = 0.10,
                        weatherCode = WeatherCode.Overcast,
                        pressureMsl = 1003.6,
                        windSpeed10m = 6.8,
                        windDirection10m = 267,
                        uvIndex = 0.05
                    )
                ),
                currentDailyForecast = DailyForecast(
                    latitude = Latitude(52.23),
                    longitude = Longitude(21.01),
                    time = LocalDate.parse("2023-03-25"),
                    weatherCode = WeatherCode.ThunderstormSlightOrModerate,
                    temperature2mMax = 13.8,
                    temperature2mMin = 7.4,
                    apparentTemperatureMax = 11.1,
                    apparentTemperatureMin = 4.9,
                    sunrise = LocalDateTime.parse("2023-03-25T05:25"),
                    sunset = LocalDateTime.parse("2023-03-25T17:58"),
                    uvIndexMax = 4.30
                ),
                currentHourlyForecast = HourlyForecast(
                    latitude = Latitude(52.23),
                    longitude = Longitude(21.01),
                    time = LocalDateTime.parse("2023-03-25T00:00"),
                    temperature2m = 11.8,
                    relativeHumidity2m = 92,
                    dewPoint2m = 10.5,
                    apparentTemperature = 10.9,
                    precipitationProbability = 30,
                    precipitation = 0.10,
                    weatherCode = WeatherCode.Overcast,
                    pressureMsl = 1003.6,
                    windSpeed10m = 6.8,
                    windDirection10m = 267,
                    uvIndex = 0.05
                )
            ),
            onEventSent = {}
        )
    }
}
