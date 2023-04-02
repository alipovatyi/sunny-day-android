package dev.arli.sunnyday.domain.usecase

import app.cash.turbine.test
import dev.arli.sunnyday.data.location.LocationRepository
import dev.arli.sunnyday.data.weather.WeatherRepository
import dev.arli.sunnyday.model.CurrentWeather
import dev.arli.sunnyday.model.LocationWithCurrentWeather
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.location.NamedLocation
import dev.arli.sunnyday.model.weather.WeatherCode
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime

internal class ObserveLocationsWithCurrentWeatherUseCaseTest : ShouldSpec({

    val mockLocationRepository: LocationRepository = mockk()
    val mockWeatherRepository: WeatherRepository = mockk()
    val useCase = ObserveLocationsWithCurrentWeatherUseCase(
        locationRepository = mockLocationRepository,
        weatherRepository = mockWeatherRepository
    )

    should("return flow of list of locations with current weather") {
        val givenLocations = listOf(
            NamedLocation(
                coordinates = Coordinates(
                    latitude = Latitude(52.23),
                    longitude = Longitude(21.01),
                ),
                name = "Warsaw",
                isCurrent = true
            ),
            NamedLocation(
                coordinates = Coordinates(
                    latitude = Latitude(50.45),
                    longitude = Longitude(30.52),
                ),
                name = "Kyiv",
                isCurrent = false
            )
        )
        val givenCurrentWeather = CurrentWeather(
            latitude = Latitude(52.23),
            longitude = Longitude(21.01),
            temperature = 12.6,
            windSpeed = 13.2,
            windDirection = 244,
            weatherCode = WeatherCode.RainShowersSlight,
            time = LocalDateTime.parse("2023-03-25T15:00")
        )

        val expectedLocationsWithCurrentWeather = listOf(
            LocationWithCurrentWeather(
                coordinates = Coordinates(
                    latitude = Latitude(52.23),
                    longitude = Longitude(21.01),
                ),
                name = "Warsaw",
                isCurrent = true,
                currentWeather = givenCurrentWeather
            ),
            LocationWithCurrentWeather(
                coordinates = Coordinates(
                    latitude = Latitude(50.45),
                    longitude = Longitude(30.52),
                ),
                name = "Kyiv",
                isCurrent = false,
                currentWeather = null
            )
        )

        every { mockLocationRepository.observeLocations() } returns flowOf(givenLocations)
        every { mockWeatherRepository.observeAllCurrentWeather() } returns flowOf(listOf(givenCurrentWeather))

        useCase().test {
            awaitItem() shouldBe expectedLocationsWithCurrentWeather

            expectNoEvents()
        }

        verify {
            mockLocationRepository.observeLocations()
            mockWeatherRepository.observeAllCurrentWeather()
        }
        confirmVerified(mockLocationRepository, mockWeatherRepository)
    }
})
