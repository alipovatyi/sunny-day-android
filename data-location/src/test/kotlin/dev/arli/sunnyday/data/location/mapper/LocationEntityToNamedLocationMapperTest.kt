package dev.arli.sunnyday.data.location.mapper

import dev.arli.sunnyday.data.db.entity.LocationEntity
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.location.NamedLocation
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class LocationEntityToNamedLocationMapperTest {

    @Test
    fun `Should map LocationEntity to NamedLocation`() {
        val givenLocationEntity = LocationEntity(
            id = 1,
            latitude = 52.237049,
            longitude = 21.017532,
            name = "Warsaw",
            isCurrent = true
        )

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
