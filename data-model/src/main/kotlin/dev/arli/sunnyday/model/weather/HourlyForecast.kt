package dev.arli.sunnyday.model.weather

import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import java.time.LocalDateTime

data class HourlyForecast(
    val latitude: Latitude,
    val longitude: Longitude,
    val time: LocalDateTime,
    val temperature2m: Double,
    val relativeHumidity2m: Int,
    val dewPoint2m: Double,
    val apparentTemperature: Double,
    val precipitationProbability: Int?,
    val precipitation: Double,
    val weatherCode: Int,
    val pressureMsl: Double,
    val windSpeed10m: Double,
    val windDirection10m: Int,
    val uvIndex: Double
)
