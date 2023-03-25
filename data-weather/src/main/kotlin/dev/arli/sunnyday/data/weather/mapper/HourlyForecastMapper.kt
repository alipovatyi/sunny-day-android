package dev.arli.sunnyday.data.weather.mapper

import dev.arli.sunnyday.data.api.dto.weather.HourlyForecastDto
import dev.arli.sunnyday.data.db.entity.HourlyForecastEntity
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude

fun HourlyForecastDto.toEntity(latitude: Latitude, longitude: Longitude): List<HourlyForecastEntity> {
    return List(temperature2m.size) { index ->
        HourlyForecastEntity(
            latitude = latitude.value,
            longitude = longitude.value,
            time = time[index].toString(),
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
