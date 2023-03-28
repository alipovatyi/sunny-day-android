package dev.arli.sunnyday.data.api.retrofit

import arrow.core.Either
import dev.arli.sunnyday.data.api.WeatherApi
import dev.arli.sunnyday.data.api.dto.weather.WeatherResponseDto
import dev.arli.sunnyday.model.error.ApiError
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitWeatherApi : WeatherApi {

    @GET("forecast")
    override suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("forecast_days") forecastDaysCount: Int,
        @Query("current_weather") includeCurrentWeather: Boolean,
        @Query("hourly") hourlyVariables: List<String>,
        @Query("daily") dailyVariables: List<String>,
        @Query("timezone") timezone: String
    ): Either<ApiError, WeatherResponseDto>
}
