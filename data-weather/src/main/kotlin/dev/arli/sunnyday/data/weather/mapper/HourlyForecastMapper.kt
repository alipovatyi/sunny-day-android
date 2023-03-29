package dev.arli.sunnyday.data.weather.mapper

import dev.arli.sunnyday.data.api.dto.weather.HourlyForecastDto
import dev.arli.sunnyday.data.db.entity.HourlyForecastEntity
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.weather.HourlyForecast

fun HourlyForecastDto.toEntity(latitude: Latitude, longitude: Longitude): List<HourlyForecastEntity> {
    return List(temperature2m.size) { index ->
        HourlyForecastEntity(
            latitude = latitude.value,
            longitude = longitude.value,
            time = time[index],
            temperature2m = temperature2m[index],
            relativeHumidity2m = relativeHumidity2m[index],
            dewPoint2m = dewPoint2m[index],
            apparentTemperature = apparentTemperature[index],
            precipitationProbability = precipitationProbability[index],
            precipitation = precipitation[index],
            weatherCode = weatherCode[index],
            pressureMsl = pressureMsl[index],
            windSpeed10m = windSpeed10m[index],
            windDirection10m = windDirection10m[index],
            uvIndex = uvIndex[index],
        )
    }
}

fun HourlyForecastEntity.toModel(): HourlyForecast {
    return HourlyForecast(
        latitude = Latitude(latitude),
        longitude = Longitude(longitude),
        time = time,
        temperature2m = temperature2m,
        relativeHumidity2m = relativeHumidity2m,
        dewPoint2m = dewPoint2m,
        apparentTemperature = apparentTemperature,
        precipitationProbability = precipitationProbability,
        precipitation = precipitation,
        weatherCode = weatherCode,
        pressureMsl = pressureMsl,
        windSpeed10m = windSpeed10m,
        windDirection10m = windDirection10m,
        uvIndex = uvIndex
    )
}
