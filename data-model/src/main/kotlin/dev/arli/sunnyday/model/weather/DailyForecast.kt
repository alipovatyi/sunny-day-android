package dev.arli.sunnyday.model.weather

import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import java.time.LocalDate
import java.time.LocalDateTime

@Suppress("ForbiddenComment")
// TODO: consider using Coordinates class instead of latitude/longitude
data class DailyForecast(
    val latitude: Latitude,
    val longitude: Longitude,
    val time: LocalDate,
    val weatherCode: Int,
    val temperature2mMax: Double,
    val temperature2mMin: Double,
    val apparentTemperatureMax: Double,
    val apparentTemperatureMin: Double,
    val sunrise: LocalDateTime,
    val sunset: LocalDateTime,
    val uvIndexMax: Double
)
