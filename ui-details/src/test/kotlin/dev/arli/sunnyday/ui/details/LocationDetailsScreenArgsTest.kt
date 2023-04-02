package dev.arli.sunnyday.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

internal class LocationDetailsScreenArgsTest : ShouldSpec({
    should("return location details screen arguments") {
        val givenLatitude = 52.23
        val givenLongitude = 21.01
        val givenArguments = mapOf(
            "latitude" to givenLatitude.toString(),
            "longitude" to givenLongitude.toString()
        )
        val givenSavedStateHandle = SavedStateHandle(givenArguments)

        val expectedScreenArguments = LocationDetailsScreenArgs(
            coordinates = Coordinates(
                latitude = Latitude(givenLatitude),
                longitude = Longitude(givenLongitude)
            )
        )

        val actualScreenArguments = LocationDetailsScreenArgs.fromStateHandle(givenSavedStateHandle)

        actualScreenArguments shouldBe expectedScreenArguments
    }

    should("return saved state handle") {
        val givenLatitude = 52.23
        val givenLongitude = 21.01
        val givenScreenArguments = LocationDetailsScreenArgs(
            coordinates = Coordinates(
                latitude = Latitude(givenLatitude),
                longitude = Longitude(givenLongitude)
            )
        )

        val actualSavedStateHandle = givenScreenArguments.toStateHandle()

        actualSavedStateHandle.get<String>("latitude") shouldBe givenLatitude.toString()
        actualSavedStateHandle.get<String>("longitude") shouldBe givenLongitude.toString()
    }

    should("return nav arguments list") {
        val expectedLatitudeArgument = navArgument("latitude") { type = NavType.StringType }
        val expectedLongitudeArgument = navArgument("longitude") { type = NavType.StringType }
        val expectedNavArgumentsMap = listOf(
            expectedLatitudeArgument,
            expectedLongitudeArgument
        ).map { it.name to it.argument }

        val actualNavArguments = LocationDetailsScreenArgs.navArguments.map { it.name to it.argument }

        actualNavArguments shouldContainExactly expectedNavArgumentsMap
    }
})
