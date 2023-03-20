package dev.arli.sunnyday.data.location

import arrow.core.left
import arrow.core.right
import dev.arli.sunnyday.domain.model.location.Coordinates
import dev.arli.sunnyday.domain.model.location.Latitude
import dev.arli.sunnyday.domain.model.location.Longitude
import dev.arli.sunnyday.domain.model.location.NamedLocation
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk

internal class LocationRepositoryTest  : BehaviorSpec({

    val mockLocationDataSource: LocationDataSource = mockk()
    val repository = LocationRepository(
        locationDataSource = mockLocationDataSource
    )

    given("get current location") {
        `when`("failed") {
            val givenResult = IllegalStateException().left()

            coEvery { mockLocationDataSource.getCurrentLocation() } returns givenResult

            then("return failure result") {
                repository.getCurrentLocation() shouldBe givenResult

                coVerify { mockLocationDataSource.getCurrentLocation() }
            }
        }

        `when`("succeeded") {
            val givenNamedLocation = NamedLocation(
                coordinates = Coordinates(
                    latitude = Latitude(52.237049),
                    longitude = Longitude(21.017532)
                ),
                name = "Warsaw"
            )
            val givenResult = givenNamedLocation.right()

            coEvery { mockLocationDataSource.getCurrentLocation() } returns givenResult

            then("return success result") {
                repository.getCurrentLocation() shouldBe givenResult

                coVerify { mockLocationDataSource.getCurrentLocation() }
            }
        }
    }

    afterEach {
        confirmVerified(mockLocationDataSource)
    }
})
