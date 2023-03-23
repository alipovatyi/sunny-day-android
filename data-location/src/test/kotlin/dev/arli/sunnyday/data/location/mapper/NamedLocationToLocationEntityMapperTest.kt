package dev.arli.sunnyday.data.location.mapper

import dev.arli.sunnyday.data.db.entity.LocationEntity
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.location.NamedLocation
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class NamedLocationToLocationEntityMapperTest {

    @Test
    fun `Should map NamedLocation to LocationEntity`() {
        val givenNamedLocation = NamedLocation(
            coordinates = Coordinates(
                latitude = Latitude(52.237049),
                longitude = Longitude(21.017532)
            ),
            name = "Warsaw"
        )

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
