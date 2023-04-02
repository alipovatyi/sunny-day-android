package dev.arli.sunnyday.model

import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.weather.DailyForecast
import dev.arli.sunnyday.model.weather.HourlyForecast

data class LocationWithForecasts(
    val coordinates: Coordinates,
    val name: String?,
    val isCurrent: Boolean,
    val currentWeather: CurrentWeather,
    val dailyForecasts: List<DailyForecast>,
    val hourlyForecasts: List<HourlyForecast>
)
