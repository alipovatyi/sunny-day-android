package dev.arli.sunnyday.ui.locations.contract

import dev.arli.sunnyday.model.LocationWithCurrentWeather
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

internal class LocationsViewStateTest : ShouldSpec({
    should("return initial view state") {
        val expectedViewState = LocationsViewState(
            locations = emptyList(),
            isRefreshing = false
        )

        LocationsViewState() shouldBe expectedViewState
    }

    context("showAddLocationButton") {
        should("return true if non-current locations size below 5") {
            val givenViewState = LocationsViewState(
                locations = List(4) {
                    LocationWithCurrentWeather(
                        coordinates = Coordinates(Latitude(it.toDouble()), Longitude(it.toDouble())),
                        name = null,
                        isCurrent = false,
                        currentWeather = null
                    )
                } + LocationWithCurrentWeather(
                    coordinates = Coordinates(Latitude(4.0), Longitude(4.0)),
                    name = null,
                    isCurrent = true,
                    currentWeather = null
                )
            )

            givenViewState.showAddLocationButton shouldBe true
        }

        should("return false if non-current locations size is 5") {
            val givenViewState = LocationsViewState(
                locations = List(5) {
                    LocationWithCurrentWeather(
                        coordinates = Coordinates(Latitude(it.toDouble()), Longitude(it.toDouble())),
                        name = null,
                        isCurrent = false,
                        currentWeather = null
                    )
                }
            )

            givenViewState.showAddLocationButton shouldBe false
        }

        should("return false if non-current locations size above 5") {

            val givenViewState = LocationsViewState(
                locations = List(6) {
                    LocationWithCurrentWeather(
                        coordinates = Coordinates(Latitude(it.toDouble()), Longitude(it.toDouble())),
                        name = null,
                        isCurrent = false,
                        currentWeather = null
                    )
                }
            )

            givenViewState.showAddLocationButton shouldBe false
        }
    }
})
