package dev.arli.sunnyday.ui.details.contract

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

internal class LocationDetailsViewStateTest : ShouldSpec({
    should("return initial view state") {
        val expectedViewState = LocationDetailsViewState(
            locationName = null,
            isCurrent = false,
            currentWeather = null,
            hourlyForecasts = emptyList(),
            dailyForecasts = emptyList(),
            currentHourlyForecast = null,
            currentDailyForecast = null,
            isRefreshing = false
        )

        LocationDetailsViewState() shouldBe expectedViewState
    }
})
