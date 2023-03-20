package dev.arli.sunnyday.data.device.datasource

import android.location.Address
import android.location.Geocoder
import android.location.Location
import arrow.core.left
import arrow.core.right
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import dev.arli.sunnyday.data.device.base.mockTask
import dev.arli.sunnyday.domain.model.location.Coordinates
import dev.arli.sunnyday.domain.model.location.Latitude
import dev.arli.sunnyday.domain.model.location.Longitude
import dev.arli.sunnyday.domain.model.location.NamedLocation
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.called
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
internal class DeviceLocationDataSourceTest : BehaviorSpec({

    val mockFusedLocationProviderClient: FusedLocationProviderClient = mockk()
    val mockGeocoder: Geocoder = mockk()
    val dataSource = DeviceLocationDataSource(
        fusedLocationProviderClient = mockFusedLocationProviderClient,
        geocoder = mockGeocoder,
        ioDispatcher = UnconfinedTestDispatcher()
    )

    given("get current location") {
        val expectedPriority = Priority.PRIORITY_LOW_POWER

        `when`("fetching location failed") {
            val givenException = NullPointerException()
            val givenLocationTask = mockTask<Location>(exception = givenException)

            every {
                mockFusedLocationProviderClient.getCurrentLocation(expectedPriority, any())
            } returns givenLocationTask

            then("return failure result") {
                dataSource.getCurrentLocation() shouldBe givenException.left()

                verify {
                    mockFusedLocationProviderClient.getCurrentLocation(expectedPriority, any())
                    mockGeocoder wasNot called
                }
            }
        }

        `when`("fetching location succeeded") {
            and("location is null") {
                val givenLocationTask = mockTask<Location?>(result = null)

                every {
                    mockFusedLocationProviderClient.getCurrentLocation(expectedPriority, any())
                } returns givenLocationTask

                then("return success result") {
                    dataSource.getCurrentLocation() shouldBe null.right()

                    verify {
                        mockFusedLocationProviderClient.getCurrentLocation(expectedPriority, any())
                        mockGeocoder wasNot called
                    }
                }
            }

            and("location is not null") {
                val givenLatitude = 52.237049
                val givenLongitude = 21.017532
                val givenLocation: Location = mockk {
                    every { latitude } returns givenLatitude
                    every { longitude } returns givenLongitude
                }
                val givenLocationTask = mockTask(result = givenLocation)

                every {
                    mockFusedLocationProviderClient.getCurrentLocation(expectedPriority, any())
                } returns givenLocationTask

                and("fetching address failed") {
                    val givenException = NullPointerException()

                    every { mockGeocoder.getFromLocation(givenLatitude, givenLongitude, 1) } throws givenException

                    then("return failure result") {
                        dataSource.getCurrentLocation() shouldBe givenException.left()

                        verify {
                            mockFusedLocationProviderClient.getCurrentLocation(expectedPriority, any())
                            mockGeocoder.getFromLocation(givenLatitude, givenLongitude, 1)
                        }
                    }
                }

                and("fetching address succeeded") {
                    and("address is null") {
                        val givenAddress: Address = mockk {
                            every { locality } returns null
                        }

                        val expectedNamedLocation = NamedLocation(
                            coordinates = Coordinates(
                                latitude = Latitude(givenLatitude),
                                longitude = Longitude(givenLongitude)
                            ),
                            name = null
                        )

                        every {
                            mockGeocoder.getFromLocation(givenLatitude, givenLongitude, 1)
                        } returns listOf(givenAddress)

                        then("return success result") {
                            dataSource.getCurrentLocation() shouldBe expectedNamedLocation.right()

                            verify {
                                mockFusedLocationProviderClient.getCurrentLocation(expectedPriority, any())
                                mockGeocoder.getFromLocation(givenLatitude, givenLongitude, 1)
                            }
                        }
                    }

                    and("address is not null") {
                        val givenName = "Warsaw"
                        val givenAddress: Address = mockk {
                            every { locality } returns givenName
                        }

                        val expectedNamedLocation = NamedLocation(
                            coordinates = Coordinates(
                                latitude = Latitude(givenLatitude),
                                longitude = Longitude(givenLongitude)
                            ),
                            name = givenName
                        )

                        every { mockGeocoder.getFromLocation(any(), any(), any()) } returns listOf(givenAddress)

                        then("return success result") {
                            dataSource.getCurrentLocation() shouldBe expectedNamedLocation.right()

                            verify {
                                mockFusedLocationProviderClient.getCurrentLocation(expectedPriority, any())
                                mockGeocoder.getFromLocation(givenLatitude, givenLongitude, 1)
                            }
                        }
                    }
                }
            }
        }
    }

    afterEach {
        confirmVerified(mockFusedLocationProviderClient, mockGeocoder)
    }
})
