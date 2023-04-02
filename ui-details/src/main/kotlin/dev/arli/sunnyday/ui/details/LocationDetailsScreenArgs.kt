package dev.arli.sunnyday.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.ui.common.navigation.ScreenArguments

data class LocationDetailsScreenArgs internal constructor(
    val coordinates: Coordinates
) : ScreenArguments {

    override fun toStateHandle(): SavedStateHandle {
        return SavedStateHandle(
            mapOf(
                LATITUDE_ARG_NAME to coordinates.latitude.value.toString(),
                LONGITUDE_ARG_NAME to coordinates.longitude.value.toString()
            )
        )
    }

    companion object : ScreenArguments.Companion {
        private const val LATITUDE_ARG_NAME = "latitude"
        private const val LONGITUDE_ARG_NAME = "longitude"

        override val navArguments: List<NamedNavArgument> = listOf(
            navArgument(LATITUDE_ARG_NAME) { type = NavType.StringType },
            navArgument(LONGITUDE_ARG_NAME) { type = NavType.StringType }
        )

        @JvmStatic
        override fun fromStateHandle(stateHandle: SavedStateHandle): LocationDetailsScreenArgs {
            return LocationDetailsScreenArgs(
                coordinates = Coordinates(
                    latitude = Latitude(requireNotNull(stateHandle.get<String>(LATITUDE_ARG_NAME)).toDouble()),
                    longitude = Longitude(requireNotNull(stateHandle.get<String>(LONGITUDE_ARG_NAME)).toDouble())
                )
            )
        }
    }
}
