package dev.arli.sunnyday.ui.common.navigation

sealed class Screen(override val route: String) : NavigationTarget {

    fun createRoute(root: RootScreen) = "${root.route}/$route"

    object Locations : Screen("locations")
}
