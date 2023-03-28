package dev.arli.sunnyday.data.location

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.arli.sunnyday.data.db.DatabaseTransactionRunner
import dev.arli.sunnyday.data.db.dao.LocationDao
import dev.arli.sunnyday.data.db.entity.LocationEntity
import dev.arli.sunnyday.data.location.datasource.DeviceLocationDataSource
import dev.arli.sunnyday.data.location.mapper.toLocationEntity
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.location.NamedLocation
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

internal class LocationRepositoryTest : BehaviorSpec({

    val mockDeviceLocationDataSource: DeviceLocationDataSource = mockk()
    val mockLocationDao: LocationDao = mockk()
    val repository = LocationRepository(
        deviceLocationDataSource = mockDeviceLocationDataSource,
        databaseTransactionRunner = object : DatabaseTransactionRunner {
            override suspend fun <T> invoke(block: suspend () -> T): T = block()
        },
        locationDao = mockLocationDao
    )

    given("refresh current location") {
        `when`("fetching failed") {
            val givenError = Throwable()

            coEvery { mockDeviceLocationDataSource.getCurrentLocation() } returns givenError.left()

            repository.refreshCurrentLocation()

            coVerify { mockDeviceLocationDataSource.getCurrentLocation() }
        }

        `when`("fetching succeeded") {
            and("old location is null") {
                coEvery { mockLocationDao.selectCurrent() } returns null

                and("new location is null") {
                    coEvery { mockDeviceLocationDataSource.getCurrentLocation() } returns null.right()

                    then("do nothing and return success result") {
                        repository.refreshCurrentLocation() shouldBeRight Unit

                        coVerify {
                            mockDeviceLocationDataSource.getCurrentLocation()
                            mockLocationDao.selectCurrent()
                        }
                    }
                }

                and("new location is not null") {
                    val givenNewLocation = NamedLocation(
                        coordinates = Coordinates(
                            latitude = Latitude(52.237049),
                            longitude = Longitude(21.017532)
                        ),
                        name = "Warsaw",
                        isCurrent = true
                    )

                    val expectedLocationEntity = givenNewLocation.toLocationEntity()

                    coEvery { mockDeviceLocationDataSource.getCurrentLocation() } returns givenNewLocation.right()

                    then("save new location in database") {
                        coEvery { mockLocationDao.insertOrUpdate(expectedLocationEntity) } just runs

                        repository.refreshCurrentLocation() shouldBeRight Unit

                        coVerify {
                            mockDeviceLocationDataSource.getCurrentLocation()
                            mockLocationDao.selectCurrent()
                            mockLocationDao.insertOrUpdate(expectedLocationEntity)
                        }
                    }
                }
            }

            and("old location is not null") {
                val givenOldLocationEntity = LocationEntity(
                    latitude = 52.237049,
                    longitude = 21.017532,
                    name = "Warsaw",
                    isCurrent = true
                )

                coEvery { mockLocationDao.selectCurrent() } returns givenOldLocationEntity

                and("new location is null") {
                    coEvery { mockDeviceLocationDataSource.getCurrentLocation() } returns null.right()

                    then("delete old location and return success result") {
                        coEvery { mockLocationDao.deleteCurrent() } just runs

                        repository.refreshCurrentLocation() shouldBeRight Unit

                        coVerify {
                            mockDeviceLocationDataSource.getCurrentLocation()
                            mockLocationDao.selectCurrent()
                            mockLocationDao.deleteCurrent()
                        }
                    }
                }

                and("new location is the same") {
                    val givenNewLocation = NamedLocation(
                        coordinates = Coordinates(
                            latitude = Latitude(52.237049),
                            longitude = Longitude(21.017532)
                        ),
                        name = "Warsaw",
                        isCurrent = true
                    )

                    coEvery { mockDeviceLocationDataSource.getCurrentLocation() } returns givenNewLocation.right()

                    then("do nothing and return success result") {

                        repository.refreshCurrentLocation() shouldBeRight Unit

                        coVerify {
                            mockDeviceLocationDataSource.getCurrentLocation()
                            mockLocationDao.selectCurrent()
                        }
                    }
                }

                and("new location is different") {
                    val givenNewLocation = NamedLocation(
                        coordinates = Coordinates(
                            latitude = Latitude(50.45),
                            longitude = Longitude(30.52)
                        ),
                        name = "Kyiv",
                        isCurrent = true
                    )

                    coEvery { mockDeviceLocationDataSource.getCurrentLocation() } returns givenNewLocation.right()

                    then("update current location in database") {
                        val expectedLocationEntity = givenNewLocation.toLocationEntity()

                        coEvery { mockLocationDao.deleteCurrent() } just runs
                        coEvery { mockLocationDao.insertOrUpdate(expectedLocationEntity) } just runs

                        repository.refreshCurrentLocation() shouldBeRight Unit

                        coVerify {
                            mockDeviceLocationDataSource.getCurrentLocation()
                            mockDeviceLocationDataSource.getCurrentLocation()
                            mockLocationDao.selectCurrent()
                            mockLocationDao.deleteCurrent()
                            mockLocationDao.insertOrUpdate(expectedLocationEntity)
                        }
                    }
                }
            }
        }
    }

    given("observe all locations") {
        `when`("called") {
            then("return flow of list of named locations") {
                val givenLocationEntities = listOf(
                    LocationEntity(
                        latitude = 52.23,
                        longitude = 21.01,
                        name = "Warsaw",
                        isCurrent = true
                    ),
                    LocationEntity(
                        latitude = 50.45,
                        longitude = 30.52,
                        name = "Kyiv",
                        isCurrent = false
                    )
                )

                val expectedNamedLocations = listOf(
                    NamedLocation(
                        coordinates = Coordinates(
                            latitude = Latitude(52.23),
                            longitude = Longitude(21.01)
                        ),
                        name = "Warsaw",
                        isCurrent = true
                    ),
                    NamedLocation(
                        coordinates = Coordinates(
                            latitude = Latitude(50.45),
                            longitude = Longitude(30.52)
                        ),
                        name = "Kyiv",
                        isCurrent = false
                    )
                )

                every { mockLocationDao.observeAll() } returns flowOf(givenLocationEntities)

                repository.observeLocations().test {
                    awaitItem() shouldBe expectedNamedLocations

                    expectNoEvents()
                }

                verify { mockLocationDao.observeAll() }
            }
        }
    }

    given("add location") {
        `when`("failed") {
            then("return either left with error") {
                val givenNamedLocation = NamedLocation(
                    coordinates = Coordinates(
                        latitude = Latitude(52.23),
                        longitude = Longitude(21.01)
                    ),
                    name = "Warsaw",
                    isCurrent = true
                )
                val givenError = Throwable()

                val expectedLocationEntity = LocationEntity(
                    latitude = 52.23,
                    longitude = 21.01,
                    name = "Warsaw",
                    isCurrent = true
                )

                coEvery { mockLocationDao.insertOrUpdate(expectedLocationEntity) } throws givenError

                repository.addLocation(givenNamedLocation) shouldBeLeft givenError

                coVerify { mockLocationDao.insertOrUpdate(expectedLocationEntity) }
            }
        }

        `when`("succeeded") {
            then("return either right with Unit") {
                val givenNamedLocation = NamedLocation(
                    coordinates = Coordinates(
                        latitude = Latitude(52.23),
                        longitude = Longitude(21.01)
                    ),
                    name = "Warsaw",
                    isCurrent = true
                )

                val expectedLocationEntity = LocationEntity(
                    latitude = 52.23,
                    longitude = 21.01,
                    name = "Warsaw",
                    isCurrent = true
                )

                coEvery { mockLocationDao.insertOrUpdate(expectedLocationEntity) } just runs

                repository.addLocation(givenNamedLocation) shouldBeRight Unit

                coVerify { mockLocationDao.insertOrUpdate(expectedLocationEntity) }
            }
        }
    }

    afterEach {
        confirmVerified(mockDeviceLocationDataSource, mockLocationDao)
    }
})
