package dev.arli.sunnyday.data.api

import arrow.core.Either
import dev.arli.sunnyday.data.api.dto.weather.WeatherResponseDto
import dev.arli.sunnyday.model.error.ApiError

interface WeatherApi {

    suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        forecastDaysCount: Int,
        includeCurrentWeather: Boolean,
        hourlyVariables: List<String>,
        dailyVariables: List<String>,
        timezone: String
    ): Either<ApiError, WeatherResponseDto>
}
