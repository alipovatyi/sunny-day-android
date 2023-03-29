package dev.arli.sunnyday.data.weather.mapper

import dev.arli.sunnyday.data.api.dto.weather.HourlyForecastDto
import dev.arli.sunnyday.data.common.serializer.SerializableLocalDateTime
import dev.arli.sunnyday.data.db.entity.HourlyForecastEntity
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.weather.HourlyForecast
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

internal class HourlyForecastMapperTest : BehaviorSpec({
    given("hourly forecast dto") {
        val givenHourlyForecastDto = HourlyForecastDto(
            time = listOf(
                SerializableLocalDateTime.parse("2023-03-25T00:00"),
                SerializableLocalDateTime.parse("2023-03-25T01:00"),
                SerializableLocalDateTime.parse("2023-03-25T02:00")
            ),
            temperature2m = listOf(11.8, 11.0, 10.0),
            relativeHumidity2m = listOf(92, 91, 90),
            dewPoint2m = listOf(10.5, 9.5, 8.5),
            apparentTemperature = listOf(10.9, 9.6, 8.5),
            precipitationProbability = listOf(30, 50, 70),
            precipitation = listOf(0.10, 0.20, 0.50),
            weatherCode = listOf(3, 2, 80),
            pressureMsl = listOf(1003.6, 1004.2, 1005.5),
            windSpeed10m = listOf(6.8, 8.7, 7.9),
            windDirection10m = listOf(267, 265, 273),
            uvIndex = listOf(0.05, 1.00, 2.95)
        )

        `when`("map to entity") {
            then("return list of hourly forecast entities") {
                val givenLatitude = Latitude(52.23)
                val givenLongitude = Longitude(21.01)

                val expectedHourlyForecastEntities = listOf(
                    HourlyForecastEntity(
                        latitude = givenLatitude.value,
                        longitude = givenLongitude.value,
                        time = LocalDateTime.parse("2023-03-25T00:00"),
                        temperature2m = 11.8,
                        relativeHumidity2m = 92,
                        dewPoint2m = 10.5,
                        apparentTemperature = 10.9,
                        precipitationProbability = 30,
                        precipitation = 0.10,
                        weatherCode = 3,
                        pressureMsl = 1003.6,
                        windSpeed10m = 6.8,
                        windDirection10m = 267,
                        uvIndex = 0.05
                    ),
                    HourlyForecastEntity(
                        latitude = givenLatitude.value,
                        longitude = givenLongitude.value,
                        time = LocalDateTime.parse("2023-03-25T01:00"),
                        temperature2m = 11.0,
                        relativeHumidity2m = 91,
                        dewPoint2m = 9.5,
                        apparentTemperature = 9.6,
                        precipitationProbability = 50,
                        precipitation = 0.20,
                        weatherCode = 2,
                        pressureMsl = 1004.2,
                        windSpeed10m = 8.7,
                        windDirection10m = 265,
                        uvIndex = 1.00
                    ),
                    HourlyForecastEntity(
                        latitude = givenLatitude.value,
                        longitude = givenLongitude.value,
                        time = LocalDateTime.parse("2023-03-25T02:00"),
                        temperature2m = 10.0,
                        relativeHumidity2m = 90,
                        dewPoint2m = 8.5,
                        apparentTemperature = 8.5,
                        precipitationProbability = 70,
                        precipitation = 0.50,
                        weatherCode = 80,
                        pressureMsl = 1005.5,
                        windSpeed10m = 7.9,
                        windDirection10m = 273,
                        uvIndex = 2.95
                    )
                )

                givenHourlyForecastDto.toEntity(givenLatitude, givenLongitude) shouldBe expectedHourlyForecastEntities
            }
        }
    }

    given("hourly forecast entity") {
        val givenHourlyForecastEntity = HourlyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDateTime.parse("2023-03-25T00:00"),
            temperature2m = 11.8,
            relativeHumidity2m = 92,
            dewPoint2m = 10.5,
            apparentTemperature = 10.9,
            precipitationProbability = 30,
            precipitation = 0.10,
            weatherCode = 3,
            pressureMsl = 1003.6,
            windSpeed10m = 6.8,
            windDirection10m = 267,
            uvIndex = 0.05
        )

        `when`("map to model") {
            then("return hourly forecast model") {
                val expectedHourlyForecast = HourlyForecast(
                    latitude = Latitude(52.23),
                    longitude = Longitude(21.01),
                    time = LocalDateTime.parse("2023-03-25T00:00"),
                    temperature2m = 11.8,
                    relativeHumidity2m = 92,
                    dewPoint2m = 10.5,
                    apparentTemperature = 10.9,
                    precipitationProbability = 30,
                    precipitation = 0.10,
                    weatherCode = 3,
                    pressureMsl = 1003.6,
                    windSpeed10m = 6.8,
                    windDirection10m = 267,
                    uvIndex = 0.05
                )

                givenHourlyForecastEntity.toModel() shouldBe expectedHourlyForecast
            }
        }
    }
})
