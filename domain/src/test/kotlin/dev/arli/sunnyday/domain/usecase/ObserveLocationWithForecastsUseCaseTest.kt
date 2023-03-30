package dev.arli.sunnyday.domain.usecase

import app.cash.turbine.test
import dev.arli.sunnyday.data.location.LocationRepository
import dev.arli.sunnyday.data.weather.WeatherRepository
import dev.arli.sunnyday.model.CurrentWeather
import dev.arli.sunnyday.model.LocationWithForecasts
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.location.NamedLocation
import dev.arli.sunnyday.model.weather.DailyForecast
import dev.arli.sunnyday.model.weather.HourlyForecast
import dev.arli.sunnyday.model.weather.WeatherCode
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.flow.flowOf

internal class ObserveLocationWithForecastsUseCaseTest : ShouldSpec({

    val mockLocationRepository: LocationRepository = mockk()
    val mockWeatherRepository: WeatherRepository = mockk()
    val useCase = ObserveLocationWithForecastsUseCase(
        locationRepository = mockLocationRepository,
        weatherRepository = mockWeatherRepository
    )

    should("return flow of location with current weather and forecasts") {
        val givenCoordinates = Coordinates(
            latitude = Latitude(52.23),
            longitude = Longitude(21.01),
        )
        val givenInput = ObserveLocationWithForecastsUseCase.Input(coordinates = givenCoordinates)
        val givenLocation = NamedLocation(
            coordinates = givenCoordinates,
            name = "Warsaw",
            isCurrent = true
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
        val givenDailyForecasts = listOf(
            DailyForecast(
                latitude = Latitude(52.23),
                longitude = Longitude(21.01),
                time = LocalDate.parse("2023-03-25"),
                weatherCode = WeatherCode.ThunderstormSlightOrModerate,
                temperature2mMax = 13.8,
                temperature2mMin = 7.4,
                apparentTemperatureMax = 11.1,
                apparentTemperatureMin = 4.9,
                sunrise = LocalDateTime.parse("2023-03-25T05:25"),
                sunset = LocalDateTime.parse("2023-03-25T17:58"),
                uvIndexMax = 4.30
            )
        )
        val givenHourlyForecasts = listOf(
            HourlyForecast(
                latitude = Latitude(52.23),
                longitude = Longitude(21.01),
                time = LocalDateTime.parse("2023-03-25T00:00"),
                temperature2m = 11.8,
                relativeHumidity2m = 92,
                dewPoint2m = 10.5,
                apparentTemperature = 10.9,
                precipitationProbability = 30,
                precipitation = 0.10,
                weatherCode = WeatherCode.Overcast,
                pressureMsl = 1003.6,
                windSpeed10m = 6.8,
                windDirection10m = 267,
                uvIndex = 0.05
            )
        )

        val expectedLocationWithForecasts = LocationWithForecasts(
            coordinates = givenCoordinates,
            name = "Warsaw",
            isCurrent = true,
            currentWeather = givenCurrentWeather,
            dailyForecasts = givenDailyForecasts,
            hourlyForecasts = givenHourlyForecasts
        )

        every { mockLocationRepository.observeLocation(givenCoordinates) } returns flowOf(givenLocation)
        every { mockWeatherRepository.observeCurrentWeather(givenCoordinates) } returns flowOf(givenCurrentWeather)
        every { mockWeatherRepository.observeDailyForecast(givenCoordinates) } returns flowOf(givenDailyForecasts)
        every { mockWeatherRepository.observeHourlyForecast(givenCoordinates) } returns flowOf(givenHourlyForecasts)

        useCase(givenInput).test {
            awaitItem() shouldBe expectedLocationWithForecasts

            expectNoEvents()
        }

        verify {
            mockLocationRepository.observeLocation(givenCoordinates)
            mockWeatherRepository.observeCurrentWeather(givenCoordinates)
            mockWeatherRepository.observeDailyForecast(givenCoordinates)
            mockWeatherRepository.observeHourlyForecast(givenCoordinates)
        }
        confirmVerified(mockLocationRepository, mockWeatherRepository)
    }
})
