package dev.arli.sunnyday.ui.locations

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.arli.sunnyday.data.config.ConfigRepository
import dev.arli.sunnyday.domain.usecase.AddLocationUseCase
import dev.arli.sunnyday.domain.usecase.ObserveLocationsWithCurrentWeatherUseCase
import dev.arli.sunnyday.domain.usecase.RefreshCurrentLocationUseCase
import dev.arli.sunnyday.domain.usecase.RefreshWeatherForAllLocationsUseCase
import dev.arli.sunnyday.model.CurrentWeather
import dev.arli.sunnyday.model.LocationWithCurrentWeather
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.location.NamedLocation
import dev.arli.sunnyday.ui.locations.contract.LocationsEffect
import dev.arli.sunnyday.ui.locations.contract.LocationsEvent
import dev.arli.sunnyday.ui.locations.contract.LocationsViewState
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
internal class LocationsViewModelTest : BehaviorSpec({

    val locationsWithCurrentWeatherFlow = MutableSharedFlow<List<LocationWithCurrentWeather>>()

    val mockObserveLocationsWithCurrentWeatherUseCase: ObserveLocationsWithCurrentWeatherUseCase = mockk {
        every { this@mockk.invoke() } returns locationsWithCurrentWeatherFlow
    }
    val mockAddLocationUseCase: AddLocationUseCase = mockk()
    val mockRefreshWeatherForAllLocationsUseCase: RefreshWeatherForAllLocationsUseCase = mockk {
        coEvery { this@mockk.invoke() } returns Unit.right()
    }
    val mockRefreshCurrentLocationUseCase: RefreshCurrentLocationUseCase = mockk()
    val mockConfigRepository: ConfigRepository = mockk()

    lateinit var viewModel: LocationsViewModel

    beforeEach {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        viewModel = LocationsViewModel(
            observeLocationsWithCurrentWeatherUseCase = mockObserveLocationsWithCurrentWeatherUseCase,
            addLocationUseCase = mockAddLocationUseCase,
            refreshWeatherForAllLocationsUseCase = mockRefreshWeatherForAllLocationsUseCase,
            refreshCurrentLocationUseCase = mockRefreshCurrentLocationUseCase,
            configRepository = mockConfigRepository
        )
    }

    afterEach {
        Dispatchers.resetMain()
    }

    given("view model") {
        `when`("init") {
            then("observe locations with current weather") {
                verify { mockObserveLocationsWithCurrentWeatherUseCase() }

                confirmVerified(mockObserveLocationsWithCurrentWeatherUseCase)
            }

            then("refresh weather for all locations") {
                coVerify { mockRefreshWeatherForAllLocationsUseCase() }

                confirmVerified(mockRefreshWeatherForAllLocationsUseCase)
            }
        }
    }

    given("AddLocationClick event") {
        `when`("event sent") {
            then("send OpenAddLocation effect") {
                viewModel.effect.test {
                    viewModel.onEventSent(LocationsEvent.AddLocationClick)

                    awaitItem() shouldBe LocationsEffect.OpenAddLocation

                    expectNoEvents()
                }
            }
        }
    }

    given("LocationClick event") {
        `when`("event sent") {
            then("send OpenLocationDetails effect") {
                val givenLocation = LocationWithCurrentWeather(
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
                        weatherCode = 80,
                        time = LocalDateTime.parse("2023-03-25T15:00")
                    )
                )

                val expectedEffect = LocationsEffect.OpenLocationDetails(
                    coordinates = givenLocation.coordinates
                )

                viewModel.effect.test {
                    viewModel.onEventSent(LocationsEvent.LocationClick(givenLocation))

                    awaitItem() shouldBe expectedEffect

                    expectNoEvents()
                }
            }
        }
    }

    given("AddLocation event") {
        val givenLocation = NamedLocation(
            coordinates = Coordinates(
                latitude = Latitude(50.45),
                longitude = Longitude(30.52),
            ),
            name = "Kyiv",
            isCurrent = false
        )

        `when`("event sent") {
            and("adding location failed") {
                then("do nothing") {
                    val givenError = Throwable()

                    val expectedInput = AddLocationUseCase.Input(location = givenLocation)

                    coEvery { mockAddLocationUseCase(expectedInput) } returns givenError.left()

                    viewModel.effect.test {
                        viewModel.onEventSent(LocationsEvent.AddLocation(givenLocation))

                        expectNoEvents()
                    }

                    coVerify { mockAddLocationUseCase(expectedInput) }
                    confirmVerified(mockAddLocationUseCase)
                }
            }

            and("adding location succeeded") {
                then("send ScrollToBottom effect") {
                    val expectedInput = AddLocationUseCase.Input(location = givenLocation)

                    coEvery { mockAddLocationUseCase(expectedInput) } returns Unit.right()

                    viewModel.effect.test {
                        viewModel.onEventSent(LocationsEvent.AddLocation(givenLocation))

                        awaitItem() shouldBe LocationsEffect.ScrollToBottom

                        expectNoEvents()
                    }

                    coVerify { mockAddLocationUseCase(expectedInput) }
                    confirmVerified(mockAddLocationUseCase)
                }
            }
            then("add location and send ScrollToBottom effect") {
            }
        }
    }

    given("Refresh event") {
        `when`("event sent") {
            and("refreshing failed") {
                then("update view state") {
                    val givenError = Throwable()

                    val expectedViewState1 = LocationsViewState(isRefreshing = true)
                    val expectedViewState2 = LocationsViewState(isRefreshing = false)

                    coEvery { mockRefreshWeatherForAllLocationsUseCase() } returns givenError.left()

                    viewModel.viewState.test {
                        skipItems(1) // skip initial state

                        viewModel.onEventSent(LocationsEvent.Refresh)

                        awaitItem() shouldBe expectedViewState1
                        awaitItem() shouldBe expectedViewState2

                        expectNoEvents()
                    }

                    coVerify { mockRefreshWeatherForAllLocationsUseCase() }
                    confirmVerified(mockRefreshWeatherForAllLocationsUseCase)
                }
            }

            and("refreshing succeeded") {
                then("update view state") {
                    val expectedViewState1 = LocationsViewState(isRefreshing = true)
                    val expectedViewState2 = LocationsViewState(isRefreshing = false)

                    coEvery { mockRefreshWeatherForAllLocationsUseCase() } returns Unit.right()

                    viewModel.viewState.test {
                        skipItems(1) // skip initial state

                        viewModel.onEventSent(LocationsEvent.Refresh)

                        awaitItem() shouldBe expectedViewState1
                        awaitItem() shouldBe expectedViewState2

                        expectNoEvents()
                    }

                    coVerify { mockRefreshWeatherForAllLocationsUseCase() }
                    confirmVerified(mockRefreshWeatherForAllLocationsUseCase)
                }
            }
        }
    }

    given("LocationPermissionStateChange event") {
        `when`("event sent") {
            and("permission not granted") {
                then("do nothing") {
                    viewModel.viewState.test {
                        skipItems(1) // skip initial state

                        viewModel.onEventSent(LocationsEvent.LocationPermissionStateChange(isGranted = false))

                        expectNoEvents()
                    }

                    confirmVerified(mockRefreshCurrentLocationUseCase)
                }
            }

            and("permission granted") {
                and("refreshing current location failed") {
                    then("update view state") {
                        val givenError = Throwable()

                        val expectedViewState1 = LocationsViewState(isRefreshing = true)
                        val expectedViewState2 = LocationsViewState(isRefreshing = false)

                        coEvery { mockRefreshCurrentLocationUseCase() } returns givenError.left()

                        viewModel.viewState.test {
                            skipItems(1) // skip initial state

                            viewModel.onEventSent(LocationsEvent.LocationPermissionStateChange(isGranted = true))

                            awaitItem() shouldBe expectedViewState1
                            awaitItem() shouldBe expectedViewState2

                            expectNoEvents()
                        }

                        coVerify { mockRefreshCurrentLocationUseCase() }
                        confirmVerified(mockRefreshCurrentLocationUseCase)
                    }
                }
            }

            and("refreshing current location succeeded") {
                then("update view state") {
                    val expectedViewState1 = LocationsViewState(isRefreshing = true)
                    val expectedViewState2 = LocationsViewState(isRefreshing = false)

                    coEvery { mockRefreshCurrentLocationUseCase() } returns Unit.right()

                    viewModel.viewState.test {
                        skipItems(1) // skip initial state

                        viewModel.onEventSent(LocationsEvent.LocationPermissionStateChange(isGranted = true))

                        awaitItem() shouldBe expectedViewState1
                        awaitItem() shouldBe expectedViewState2

                        expectNoEvents()
                    }

                    coVerify { mockRefreshCurrentLocationUseCase() }
                    confirmVerified(mockRefreshCurrentLocationUseCase)
                }
            }
        }
    }

    given("CopyrightClick") {
        `when`("event sent") {
            then("send OpenUrl effect") {
                val givenDataSourceUrl = URL("https://open-meteo.com/")

                val expectedEffect = LocationsEffect.OpenUrl(url = givenDataSourceUrl)

                every { mockConfigRepository.getDataSourceUrl() } returns givenDataSourceUrl

                viewModel.effect.test {
                    viewModel.onEventSent(LocationsEvent.CopyrightClick)

                    awaitItem() shouldBe expectedEffect

                    expectNoEvents()
                }

                verify { mockConfigRepository.getDataSourceUrl() }
            }
        }
    }

    given("observing locations with current weather") {
        `when`("locations are emitted") {
            and("list is empty") {
                then("update view state") {
                    viewModel.viewState.test {
                        skipItems(1) // skip initial state

                        locationsWithCurrentWeatherFlow.emit(emptyList())

                        expectNoEvents()
                    }
                }
            }

            and("list is not empty") {
                then("update view state") {
                    val givenLocation1 = LocationWithCurrentWeather(
                        coordinates = Coordinates(
                            latitude = Latitude(52.23),
                            longitude = Longitude(21.01)
                        ),
                        name = "Warsaw",
                        isCurrent = true,
                        currentWeather = CurrentWeather(
                            latitude = Latitude(52.23),
                            longitude = Longitude(21.01),
                            temperature = 12.6,
                            windSpeed = 13.2,
                            windDirection = 244,
                            weatherCode = 80,
                            time = LocalDateTime.parse("2023-03-25T15:00")
                        )
                    )
                    val givenLocation2 = LocationWithCurrentWeather(
                        coordinates = Coordinates(
                            latitude = Latitude(50.45),
                            longitude = Longitude(30.52)
                        ),
                        name = "Kyiv",
                        isCurrent = false,
                        currentWeather = CurrentWeather(
                            latitude = Latitude(50.45),
                            longitude = Longitude(30.52),
                            temperature = 10.0,
                            windSpeed = 25.0,
                            windDirection = 90,
                            weatherCode = 1,
                            time = LocalDateTime.parse("2023-03-25T15:00")
                        )
                    )

                    val expectedViewState1 = LocationsViewState(locations = listOf(givenLocation1))
                    val expectedViewState2 = LocationsViewState(locations = listOf(givenLocation1, givenLocation2))

                    viewModel.viewState.test {
                        skipItems(1) // skip initial state

                        locationsWithCurrentWeatherFlow.emit(listOf(givenLocation1))
                        awaitItem() shouldBe expectedViewState1

                        locationsWithCurrentWeatherFlow.emit(listOf(givenLocation1, givenLocation2))
                        awaitItem() shouldBe expectedViewState2

                        expectNoEvents()
                    }
                }
            }
        }
    }
})
