package dev.arli.sunnyday.ui.details.contract

import androidx.compose.runtime.Immutable
import dev.arli.sunnyday.model.CurrentWeather
import dev.arli.sunnyday.model.weather.DailyForecast
import dev.arli.sunnyday.model.weather.HourlyForecast
import dev.arli.sunnyday.ui.common.base.ViewState

@Immutable
data class LocationDetailsViewState(
    val locationName: String? = null,
    val isCurrent: Boolean = false,
    val currentWeather: CurrentWeather? = null,
    val hourlyForecasts: List<HourlyForecast> = emptyList(),
    val dailyForecasts: List<DailyForecast> = emptyList(),
    val currentHourlyForecast: HourlyForecast? = null,
    val currentDailyForecast: DailyForecast? = null
) : ViewState
