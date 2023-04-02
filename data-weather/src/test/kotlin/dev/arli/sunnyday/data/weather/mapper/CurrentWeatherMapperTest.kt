package dev.arli.sunnyday.data.weather.mapper

import dev.arli.sunnyday.data.api.dto.weather.CurrentWeatherDto
import dev.arli.sunnyday.data.common.serializer.SerializableLocalDateTime
import dev.arli.sunnyday.data.db.entity.CurrentWeatherEntity
import dev.arli.sunnyday.model.CurrentWeather
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.weather.WeatherCode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

internal class CurrentWeatherMapperTest : BehaviorSpec({
    given("current weather dto") {
        val givenCurrentWeatherDto = CurrentWeatherDto(
            temperature = 12.6,
            windSpeed = 13.2,
            windDirection = 244.0,
            weatherCode = 80,
            time = SerializableLocalDateTime.parse("2023-03-25T15:00")
        )

        `when`("map to entity") {
            then("return current weather entity") {
                val givenLatitude = Latitude(52.23)
                val givenLongitude = Longitude(21.01)

                val expectedCurrentWeatherEntity = CurrentWeatherEntity(
                    latitude = givenLatitude.value,
                    longitude = givenLongitude.value,
                    temperature = 12.6,
                    windSpeed = 13.2,
                    windDirection = 244,
                    weatherCode = 80,
                    time = LocalDateTime.parse("2023-03-25T15:00")
                )

                givenCurrentWeatherDto.toEntity(givenLatitude, givenLongitude) shouldBe expectedCurrentWeatherEntity
            }
        }
    }

    given("current weather entity") {
        val givenCurrentWeatherEntity = CurrentWeatherEntity(
            latitude = 52.23,
            longitude = 21.01,
            temperature = 12.6,
            windSpeed = 13.2,
            windDirection = 244,
            weatherCode = 80,
            time = LocalDateTime.parse("2023-03-25T15:00")
        )

        `when`("map to model") {
            then("return current weather model") {
                val givenLatitude = Latitude(52.23)
                val givenLongitude = Longitude(21.01)

                val expectedCurrentWeather = CurrentWeather(
                    latitude = givenLatitude,
                    longitude = givenLongitude,
                    temperature = 12.6,
                    windSpeed = 13.2,
                    windDirection = 244,
                    weatherCode = WeatherCode.RainShowersSlight,
                    time = LocalDateTime.parse("2023-03-25T15:00")
                )

                givenCurrentWeatherEntity.toModel() shouldBe expectedCurrentWeather
            }
        }
    }
})
