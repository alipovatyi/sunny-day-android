package dev.arli.sunnyday.data.location.mapper

import dev.arli.sunnyday.data.db.entity.LocationEntity
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.location.NamedLocation
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

internal class LocationMapperTest : BehaviorSpec({
    given("location entity") {
        val givenLocationEntity = LocationEntity(
            latitude = 52.237049,
            longitude = 21.017532,
            name = "Warsaw",
            isCurrent = true
        )

        `when`("map to named location") {
            then("return named location model") {
                val expectedNamedLocation = NamedLocation(
                    coordinates = Coordinates(
                        latitude = Latitude(52.237049),
                        longitude = Longitude(21.017532)
                    ),
                    name = "Warsaw"
                )

                givenLocationEntity.toNamedLocation() shouldBe expectedNamedLocation
            }
        }
    }

    given("named location model") {
        val givenNamedLocation = NamedLocation(
            coordinates = Coordinates(
                latitude = Latitude(52.237049),
                longitude = Longitude(21.017532)
            ),
            name = "Warsaw"
        )
        
        `when`("map to location entity") {
            val expectedLocationEntity = LocationEntity(
                latitude = 52.237049,
                longitude = 21.017532,
                name = "Warsaw",
                isCurrent = true
            )

            val actualLocationEntity = givenNamedLocation.toLocationEntity(isCurrent = true)

            actualLocationEntity shouldBe expectedLocationEntity
        }
    }
})
