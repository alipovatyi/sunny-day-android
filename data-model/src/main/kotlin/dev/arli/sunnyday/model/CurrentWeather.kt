package dev.arli.sunnyday.model

import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import java.time.LocalDateTime

data class CurrentWeather(
    val latitude: Latitude,
    val longitude: Longitude,
    val temperature: Double,
    val windSpeed: Double,
    val windDirection: Int,
    val weatherCode: Int,
    val time: LocalDateTime
)
