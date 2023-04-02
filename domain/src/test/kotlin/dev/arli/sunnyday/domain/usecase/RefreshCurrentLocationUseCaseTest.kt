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
import io.mockk.mockk

internal class RefreshCurrentLocationUseCaseTest : BehaviorSpec({

    val mockLocationRepository: LocationRepository = mockk()
    val mockWeatherRepository: WeatherRepository = mockk()
    val useCase = RefreshCurrentLocationUseCase(
        locationRepository = mockLocationRepository,
        weatherRepository = mockWeatherRepository
    )

    given("refresh current location") {
        `when`("refreshing location failed") {
            then("return either right with Unit") {
                val givenError = Throwable()

                coEvery { mockLocationRepository.refreshCurrentLocation() } returns givenError.left()

                useCase() shouldBeLeft givenError

                coVerify { mockLocationRepository.refreshCurrentLocation() }
            }
        }

        `when`("refreshing location succeeded") {
            and("location is null") {
                then("return either right with Unit") {
                    coEvery { mockLocationRepository.refreshCurrentLocation() } returns null.right()

                    useCase() shouldBeRight Unit

                    coVerify { mockLocationRepository.refreshCurrentLocation() }
                }
            }

            and("location is not null") {
                val givenNamedLocation = NamedLocation(
                    coordinates = Coordinates(
                        latitude = Latitude(52.23),
                        longitude = Longitude(21.01)
                    ),
                    name = "Warsaw",
                    isCurrent = true
                )

                coEvery { mockLocationRepository.refreshCurrentLocation() } returns givenNamedLocation.right()

                and("refreshing weather failed") {
                    then("return either left with error") {
                        val givenError = Throwable()

                        coEvery {
                            mockWeatherRepository.refreshWeather(
                                givenNamedLocation.coordinates.latitude,
                                givenNamedLocation.coordinates.longitude
                            )
                        } returns givenError.left()

                        useCase() shouldBeLeft givenError

                        coVerify {
                            mockLocationRepository.refreshCurrentLocation()
                            mockWeatherRepository.refreshWeather(
                                givenNamedLocation.coordinates.latitude,
                                givenNamedLocation.coordinates.longitude
                            )
                        }
                    }
                }

                and("refreshing weather succeeded") {
                    then("return either right with Unit") {
                        coEvery {
                            mockWeatherRepository.refreshWeather(
                                givenNamedLocation.coordinates.latitude,
                                givenNamedLocation.coordinates.longitude
                            )
                        } returns Unit.right()

                        useCase() shouldBeRight Unit

                        coVerify {
                            mockLocationRepository.refreshCurrentLocation()
                            mockWeatherRepository.refreshWeather(
                                givenNamedLocation.coordinates.latitude,
                                givenNamedLocation.coordinates.longitude
                            )
                        }
                    }
                }
            }
        }
    }

    afterEach {
        confirmVerified(mockLocationRepository, mockWeatherRepository)
    }
})
