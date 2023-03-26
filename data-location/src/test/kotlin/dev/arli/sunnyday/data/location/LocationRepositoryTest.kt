package dev.arli.sunnyday.data.location

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
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs

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
            val givenException = Throwable()

            coEvery { mockDeviceLocationDataSource.getCurrentLocation() } returns givenException.left()

            repository.refreshCurrentLocation()

            coVerify { mockDeviceLocationDataSource.getCurrentLocation() }
        }

        `when`("fetching succeeded") {
            and("old location is null") {
                coEvery { mockLocationDao.selectCurrent() } returns null

                and("new location is null") {
                    coEvery { mockDeviceLocationDataSource.getCurrentLocation() } returns null.right()

                    then("do nothing and return success result") {
                        repository.refreshCurrentLocation() shouldBe Unit.right()

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
                        name = "Warsaw"
                    )

                    val expectedLocationEntity = givenNewLocation.toLocationEntity(isCurrent = true)

                    coEvery { mockDeviceLocationDataSource.getCurrentLocation() } returns givenNewLocation.right()

                    then("save new location in database") {
                        coEvery { mockLocationDao.insertOrUpdate(expectedLocationEntity) } just runs

                        repository.refreshCurrentLocation() shouldBe Unit.right()

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

                        repository.refreshCurrentLocation() shouldBe Unit.right()

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
                        name = "Warsaw"
                    )

                    coEvery { mockDeviceLocationDataSource.getCurrentLocation() } returns givenNewLocation.right()

                    then("do nothing and return success result") {

                        repository.refreshCurrentLocation() shouldBe Unit.right()

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
                        name = "Kyiv"
                    )

                    coEvery { mockDeviceLocationDataSource.getCurrentLocation() } returns givenNewLocation.right()

                    then("update current location in database") {
                        val expectedLocationEntity = givenNewLocation.toLocationEntity(isCurrent = true)

                        coEvery { mockLocationDao.deleteCurrent() } just runs
                        coEvery { mockLocationDao.insertOrUpdate(expectedLocationEntity) } just runs

                        repository.refreshCurrentLocation() shouldBe Unit.right()

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

    afterEach {
        confirmVerified(mockDeviceLocationDataSource, mockLocationDao)
    }
})
