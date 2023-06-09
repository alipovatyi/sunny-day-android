package dev.arli.sunnyday.data.weather.mapper

import dev.arli.sunnyday.data.api.dto.weather.DailyForecastDto
import dev.arli.sunnyday.data.db.entity.DailyForecastEntity
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.weather.DailyForecast
import dev.arli.sunnyday.model.weather.WeatherCode

fun DailyForecastDto.toEntity(latitude: Latitude, longitude: Longitude): List<DailyForecastEntity> {
    return List(temperature2mMax.size) { index ->
        DailyForecastEntity(
            latitude = latitude.value,
            longitude = longitude.value,
            time = time[index],
            weatherCode = weatherCode[index],
            temperature2mMax = temperature2mMax[index],
            temperature2mMin = temperature2mMin[index],
            apparentTemperatureMax = apparentTemperatureMax[index],
            apparentTemperatureMin = apparentTemperatureMin[index],
            sunrise = sunrise[index],
            sunset = sunset[index],
            uvIndexMax = uvIndexMax[index]
        )
    }
}

fun DailyForecastEntity.toModel(): DailyForecast {
    return DailyForecast(
        latitude = Latitude(latitude),
        longitude = Longitude(longitude),
        time = time,
        weatherCode = WeatherCode.fromKey(weatherCode),
        temperature2mMax = temperature2mMax,
        temperature2mMin = temperature2mMin,
        apparentTemperatureMax = apparentTemperatureMax,
        apparentTemperatureMin = apparentTemperatureMin,
        sunrise = sunrise,
        sunset = sunset,
        uvIndexMax = uvIndexMax
    )
}
