package dev.arli.sunnyday.domain.usecase

import arrow.core.left
import arrow.core.right
import dev.arli.sunnyday.data.location.LocationRepository
import dev.arli.sunnyday.data.weather.WeatherRepository
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.location.NamedLocation
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

internal class RefreshWeatherForAllLocationsUseCaseTest : BehaviorSpec({

    val mockLocationRepository: LocationRepository = mockk()
    val mockWeatherRepository: WeatherRepository = mockk()
    val useCase = RefreshWeatherForAllLocationsUseCase(
        locationRepository = mockLocationRepository,
        weatherRepository = mockWeatherRepository
    )

    given("refresh weather for all locations") {
        `when`("location list is empty") {
            every { mockLocationRepository.observeLocations() } returns emptyFlow()

            then("return either right with Unit") {
                useCase() shouldBeRight Unit

                verify { mockLocationRepository.observeLocations() }
            }
        }

        `when`("location list is not empty") {
            val givenNamedLocation1 = NamedLocation(
                coordinates = Coordinates(
                    latitude = Latitude(52.23),
                    longitude = Longitude(21.01)
                ),
                name = "Warsaw",
                isCurrent = true
            )
            val givenNamedLocation2 = NamedLocation(
                coordinates = Coordinates(
                    latitude = Latitude(50.45),
                    longitude = Longitude(30.52)
                ),
                name = "Kyiv",
                isCurrent = false
            )
            val givenNamedLocations = listOf(givenNamedLocation1, givenNamedLocation2)

            every { mockLocationRepository.observeLocations() } returns flowOf(givenNamedLocations)

            and("refreshing failed") {
                then("return either left with error") {
                    val givenError = Throwable()

                    coEvery {
                        mockWeatherRepository.refreshWeather(
                            latitude = givenNamedLocation1.coordinates.latitude,
                            longitude = givenNamedLocation1.coordinates.longitude
                        )
                    } returns Unit.right()
                    coEvery {
                        mockWeatherRepository.refreshWeather(
                            latitude = givenNamedLocation2.coordinates.latitude,
                            longitude = givenNamedLocation2.coordinates.longitude
                        )
                    } returns givenError.left()

                    useCase() shouldBeLeft givenError

                    coVerify {
                        mockLocationRepository.observeLocations()
                        mockWeatherRepository.refreshWeather(
                            latitude = givenNamedLocation1.coordinates.latitude,
                            longitude = givenNamedLocation1.coordinates.longitude
                        )
                        mockWeatherRepository.refreshWeather(
                            latitude = givenNamedLocation2.coordinates.latitude,
                            longitude = givenNamedLocation2.coordinates.longitude
                        )
                    }
                }
            }

            and("refreshing succeeded") {
                then("return either right with Unit") {
                    coEvery {
                        mockWeatherRepository.refreshWeather(
                            latitude = givenNamedLocation1.coordinates.latitude,
                            longitude = givenNamedLocation1.coordinates.longitude
                        )
                    } returns Unit.right()
                    coEvery {
                        mockWeatherRepository.refreshWeather(
                            latitude = givenNamedLocation2.coordinates.latitude,
                            longitude = givenNamedLocation2.coordinates.longitude
                        )
                    } returns Unit.right()

                    useCase() shouldBeRight Unit

                    coVerify {
                        mockLocationRepository.observeLocations()
                        mockWeatherRepository.refreshWeather(
                            latitude = givenNamedLocation1.coordinates.latitude,
                            longitude = givenNamedLocation1.coordinates.longitude
                        )
                        mockWeatherRepository.refreshWeather(
                            latitude = givenNamedLocation2.coordinates.latitude,
                            longitude = givenNamedLocation2.coordinates.longitude
                        )
                    }
                }
            }
        }
    }

    afterEach {
        confirmVerified(mockLocationRepository, mockWeatherRepository)
    }
})
