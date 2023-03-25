package dev.arli.sunnyday.data.weather.mapper

import dev.arli.sunnyday.data.api.dto.weather.DailyForecastDto
import dev.arli.sunnyday.data.common.serializer.SerializableLocalDate
import dev.arli.sunnyday.data.common.serializer.SerializableLocalDateTime
import dev.arli.sunnyday.data.db.entity.DailyForecastEntity
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

internal class DailyForecastMapperTest : BehaviorSpec({
    given("daily forecast dto") {
        val givenDailyForecastDto = DailyForecastDto(
            time = listOf(
                SerializableLocalDate.parse("2023-03-25"),
                SerializableLocalDate.parse("2023-03-26"),
                SerializableLocalDate.parse("2023-03-27")
            ),
            weatherCode = listOf(95, 61, 80),
            temperature2mMax = listOf(13.8, 9.9, 5.8),
            temperature2mMin = listOf(7.4, 6.2, -0.7),
            apparentTemperatureMax = listOf(11.1, 6.9, 3.8),
            apparentTemperatureMin = listOf(4.9, 3.4, -6.1),
            sunrise = listOf(
                SerializableLocalDateTime.parse("2023-03-25T05:25"),
                SerializableLocalDateTime.parse("2023-03-26T05:23"),
                SerializableLocalDateTime.parse("2023-03-27T05:20")
            ),
            sunset = listOf(
                SerializableLocalDateTime.parse("2023-03-25T17:58"),
                SerializableLocalDateTime.parse("2023-03-26T18:00"),
                SerializableLocalDateTime.parse("2023-03-27T18:02")
            ),
            uvIndexMax = listOf(4.30, 3.95, 3.10)
        )

        `when`("map to entity") {
            then("return list of daily forecast entities") {
                val givenLatitude = Latitude(52.23)
                val givenLongitude = Longitude(21.01)

                val expectedDailyForecastEntities = listOf(
                    DailyForecastEntity(
                        latitude = givenLatitude.value,
                        longitude = givenLongitude.value,
                        time = "2023-03-25",
                        weatherCode = 95,
                        temperature2mMax = 13.8,
                        temperature2mMin = 7.4,
                        apparentTemperatureMax = 11.1,
                        apparentTemperatureMin = 4.9,
                        sunrise = "2023-03-25T05:25",
                        sunset = "2023-03-25T17:58",
                        uvIndexMax = 4.30
                    ),
                    DailyForecastEntity(
                        latitude = givenLatitude.value,
                        longitude = givenLongitude.value,
                        time = "2023-03-26",
                        weatherCode = 61,
                        temperature2mMax = 9.9,
                        temperature2mMin = 6.2,
                        apparentTemperatureMax = 6.9,
                        apparentTemperatureMin = 3.4,
                        sunrise = "2023-03-26T05:23",
                        sunset = "2023-03-26T18:00",
                        uvIndexMax = 3.95
                    ),
                    DailyForecastEntity(
                        latitude = givenLatitude.value,
                        longitude = givenLongitude.value,
                        time = "2023-03-27",
                        weatherCode = 80,
                        temperature2mMax = 5.8,
                        temperature2mMin = -0.7,
                        apparentTemperatureMax = 3.8,
                        apparentTemperatureMin = -6.1,
                        sunrise = "2023-03-27T05:20",
                        sunset = "2023-03-27T18:02",
                        uvIndexMax = 3.10
                    )
                )

                givenDailyForecastDto.toEntity(givenLatitude, givenLongitude) shouldBe expectedDailyForecastEntities
            }
        }
    }
})
