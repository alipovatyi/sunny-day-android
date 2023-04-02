package dev.arli.sunnyday.ui.common.navigation

import dev.arli.sunnyday.model.location.Coordinates

sealed class Screen(override val route: String) : NavigationTarget {

    fun createRoute(root: RootScreen) = "${root.route}/$route"

    object Locations : Screen("locations")

    object LocationDetails : Screen("locationDetails/{latitude}/{longitude}") {
        fun createRoute(root: RootScreen, coordinates: Coordinates): String {
            return "${root.route}/locationDetails/${coordinates.latitude.value}/${coordinates.longitude.value}"
        }
    }
}
