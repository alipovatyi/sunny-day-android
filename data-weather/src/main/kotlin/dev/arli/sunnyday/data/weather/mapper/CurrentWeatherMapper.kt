package dev.arli.sunnyday.data.weather.mapper

import dev.arli.sunnyday.data.api.dto.weather.CurrentWeatherDto
import dev.arli.sunnyday.data.db.entity.CurrentWeatherEntity
import dev.arli.sunnyday.model.CurrentWeather
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.weather.WeatherCode

fun CurrentWeatherDto.toEntity(latitude: Latitude, longitude: Longitude): CurrentWeatherEntity {
    return CurrentWeatherEntity(
        latitude = latitude.value,
        longitude = longitude.value,
        temperature = temperature,
        windSpeed = windSpeed,
        windDirection = windDirection.toInt(),
        weatherCode = weatherCode,
        time = time
    )
}

fun CurrentWeatherEntity.toModel(): CurrentWeather {
    return CurrentWeather(
        latitude = Latitude(latitude),
        longitude = Longitude(longitude),
        temperature = temperature,
        windSpeed = windSpeed,
        windDirection = windDirection,
        weatherCode = WeatherCode.fromKey(weatherCode),
        time = time
    )
}
