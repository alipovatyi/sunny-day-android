package dev.arli.sunnyday.ui.locations.contract

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
})
