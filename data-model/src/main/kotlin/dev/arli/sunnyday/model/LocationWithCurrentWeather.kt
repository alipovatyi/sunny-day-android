package dev.arli.sunnyday.model

import dev.arli.sunnyday.model.location.Coordinates

data class LocationWithCurrentWeather(
    val coordinates: Coordinates,
    val name: String?,
    val isCurrent: Boolean,
    val currentWeather: CurrentWeather?
)
