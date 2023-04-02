package dev.arli.sunnyday.data.api.dto.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponseDto(
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("current_weather") val currentWeather: CurrentWeatherDto,
    @SerialName("hourly") val hourly: HourlyForecastDto,
    @SerialName("daily") val daily: DailyForecastDto
)
