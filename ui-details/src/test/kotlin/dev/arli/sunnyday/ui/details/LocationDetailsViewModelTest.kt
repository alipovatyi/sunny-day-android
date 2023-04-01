package dev.arli.sunnyday.ui.details

import app.cash.turbine.test
import dev.arli.sunnyday.data.config.ConfigRepository
import dev.arli.sunnyday.domain.usecase.ObserveLocationWithForecastsUseCase
import dev.arli.sunnyday.model.CurrentWeather
import dev.arli.sunnyday.model.LocationWithForecasts
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.weather.DailyForecast
import dev.arli.sunnyday.model.weather.HourlyForecast
import dev.arli.sunnyday.model.weather.WeatherCode
import dev.arli.sunnyday.ui.details.contract.LocationDetailsEffect
import dev.arli.sunnyday.ui.details.contract.LocationDetailsEvent
import dev.arli.sunnyday.ui.details.contract.LocationDetailsViewState
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
internal class LocationDetailsViewModelTest : BehaviorSpec({

    val locationWithForecastsFlow = MutableSharedFlow<LocationWithForecasts>()

    val mockConfigRepository: ConfigRepository = mockk()
    val mockObserveLocationWithForecastsUseCase: ObserveLocationWithForecastsUseCase = mockk {
        every { this@mockk.invoke(any()) } returns locationWithForecastsFlow
    }

    lateinit var viewModel: LocationDetailsViewModel

    beforeEach {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        viewModel = LocationDetailsViewModel(
            savedStateHandle = LocationDetailsScreenArgs(
                coordinates = Coordinates(
                    latitude = Latitude(52.23),
                    longitude = Longitude(21.01)
                )
            ).toStateHandle(),
            configRepository = mockConfigRepository,
            observeLocationWithForecastsUseCase = mockObserveLocationWithForecastsUseCase
        )
    }

    afterEach {
        Dispatchers.resetMain()
    }

    given("view model") {
        `when`("init") {
            then("observe location with forecasts") {
                val expectedInput = ObserveLocationWithForecastsUseCase.Input(
                    coordinates = Coordinates(
                        latitude = Latitude(52.23),
                        longitude = Longitude(21.01)
                    )
                )

                verify { mockObserveLocationWithForecastsUseCase(expectedInput) }

                confirmVerified(mockObserveLocationWithForecastsUseCase)
            }
        }
    }

    given("BackClick event") {
        `when`("event sent") {
            then("send NavigateUp effect") {
                viewModel.effect.test {
                    viewModel.onEventSent(LocationDetailsEvent.BackClick)

                    awaitItem() shouldBe LocationDetailsEffect.NavigateUp

                    expectNoEvents()
                }
            }
        }
    }

    given("DeleteClick event") {
        `when`("event sent") {
            then("send OpenAddLocation effect") {
                // TODO
            }
        }
    }

    given("CopyrightClick event") {
        `when`("event sent") {
            then("send OpenUrl effect") {
                val givenDataSourceUrl = URL("https://open-meteo.com/")

                val expectedEffect = LocationDetailsEffect.OpenUrl(url = givenDataSourceUrl)

                every { mockConfigRepository.getDataSourceUrl() } returns givenDataSourceUrl

                viewModel.effect.test {
                    viewModel.onEventSent(LocationDetailsEvent.CopyrightClick)

                    awaitItem() shouldBe expectedEffect

                    expectNoEvents()
                }

                verify { mockConfigRepository.getDataSourceUrl() }
            }
        }
    }

    given("observing location location with forecasts") {
        `when`("location is emitted") {
            then("update view state") {
                val givenLocationWithForecasts1 = LocationWithForecasts(
                    coordinates = Coordinates(
                        latitude = Latitude(52.23),
                        longitude = Longitude(21.01),
                    ),
                    name = "Warsaw",
                    isCurrent = true,
                    currentWeather = CurrentWeather(
                        latitude = Latitude(52.23),
                        longitude = Longitude(21.01),
                        temperature = 12.6,
                        windSpeed = 13.2,
                        windDirection = 244,
                        weatherCode = WeatherCode.RainShowersSlight,
                        time = LocalDateTime.parse("2023-03-25T15:00")
                    ),
                    dailyForecasts = listOf(
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
                    ),
                    hourlyForecasts = listOf(
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
                )
                val givenLocationWithForecasts2 = LocationWithForecasts(
                    coordinates = Coordinates(
                        latitude = Latitude(52.23),
                        longitude = Longitude(21.01),
                    ),
                    name = "Warsaw",
                    isCurrent = true,
                    currentWeather = CurrentWeather(
                        latitude = Latitude(52.23),
                        longitude = Longitude(21.01),
                        temperature = 10.0,
                        windSpeed = 20.2,
                        windDirection = 180,
                        weatherCode = WeatherCode.PartlyCloudy,
                        time = LocalDateTime.parse("2023-03-25T15:00")
                    ),
                    dailyForecasts = listOf(
                        DailyForecast(
                            latitude = Latitude(52.23),
                            longitude = Longitude(21.01),
                            time = LocalDate.parse("2023-03-25"),
                            weatherCode = WeatherCode.RainSlight,
                            temperature2mMax = 9.9,
                            temperature2mMin = 6.2,
                            apparentTemperatureMax = 6.9,
                            apparentTemperatureMin = 3.4,
                            sunrise = LocalDateTime.parse("2023-03-25T05:23"),
                            sunset = LocalDateTime.parse("2023-03-25T18:00"),
                            uvIndexMax = 3.95
                        )
                    ),
                    hourlyForecasts = listOf(
                        HourlyForecast(
                            latitude = Latitude(52.23),
                            longitude = Longitude(21.01),
                            time = LocalDateTime.parse("2023-03-25T00:00"),
                            temperature2m = 11.0,
                            relativeHumidity2m = 91,
                            dewPoint2m = 9.5,
                            apparentTemperature = 9.6,
                            precipitationProbability = 50,
                            precipitation = 0.20,
                            weatherCode = WeatherCode.PartlyCloudy,
                            pressureMsl = 1004.2,
                            windSpeed10m = 8.7,
                            windDirection10m = 265,
                            uvIndex = 1.00
                        )
                    )
                )

                val expectedViewState1 = LocationDetailsViewState(locationWithForecasts = givenLocationWithForecasts1)
                val expectedViewState2 = LocationDetailsViewState(locationWithForecasts = givenLocationWithForecasts2)

                viewModel.viewState.test {
                    skipItems(1) // skip initial state

                    locationWithForecastsFlow.emit(givenLocationWithForecasts1)
                    awaitItem() shouldBe expectedViewState1

                    locationWithForecastsFlow.emit(givenLocationWithForecasts2)
                    awaitItem() shouldBe expectedViewState2

                    expectNoEvents()
                }
            }
        }
    }
})
