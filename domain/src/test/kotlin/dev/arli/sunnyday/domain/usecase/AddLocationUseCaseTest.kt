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

internal class AddLocationUseCaseTest : BehaviorSpec({

    val mockLocationRepository: LocationRepository = mockk()
    val mockWeatherRepository: WeatherRepository = mockk()
    val useCase = AddLocationUseCase(
        locationRepository = mockLocationRepository,
        weatherRepository = mockWeatherRepository
    )

    given("add location use case") {
        val givenLocation = NamedLocation(
            coordinates = Coordinates(
                latitude = Latitude(52.237049),
                longitude = Longitude(21.017532)
            ),
            name = "Warsaw",
            isCurrent = true
        )
        val givenInput = AddLocationUseCase.Input(location = givenLocation)

        `when`("invoke") {
            and("adding location failed") {
                then("return either left with error") {
                    val givenError = Throwable()

                    coEvery { mockLocationRepository.addLocation(givenLocation) } returns givenError.left()

                    useCase(givenInput) shouldBeLeft givenError

                    coVerify { mockLocationRepository.addLocation(givenLocation) }
                }
            }

            and("refreshing weather failed") {
                then("return either left with error") {
                    val givenError = Throwable()

                    coEvery { mockLocationRepository.addLocation(givenLocation) } returns Unit.right()
                    coEvery {
                        mockWeatherRepository.refreshWeather(
                            latitude = givenLocation.coordinates.latitude,
                            longitude = givenLocation.coordinates.longitude
                        )
                    } returns givenError.left()

                    useCase(givenInput) shouldBeLeft givenError

                    coVerify {
                        mockLocationRepository.addLocation(givenLocation)
                        mockWeatherRepository.refreshWeather(
                            latitude = givenLocation.coordinates.latitude,
                            longitude = givenLocation.coordinates.longitude
                        )
                    }
                }
            }

            and("succeeded") {
                then("return either right with Unit") {
                    coEvery { mockLocationRepository.addLocation(givenLocation) } returns Unit.right()
                    coEvery {
                        mockWeatherRepository.refreshWeather(
                            latitude = givenLocation.coordinates.latitude,
                            longitude = givenLocation.coordinates.longitude
                        )
                    } returns Unit.right()

                    useCase(givenInput) shouldBeRight Unit

                    coVerify {
                        mockLocationRepository.addLocation(givenLocation)
                        mockWeatherRepository.refreshWeather(
                            latitude = givenLocation.coordinates.latitude,
                            longitude = givenLocation.coordinates.longitude
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
