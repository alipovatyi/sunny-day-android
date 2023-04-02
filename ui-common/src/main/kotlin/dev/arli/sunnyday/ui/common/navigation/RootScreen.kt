package dev.arli.sunnyday.ui.common.navigation

sealed class RootScreen(
    override val route: String,
    val startDestination: Screen
) : NavigationTarget {

    object Locations : RootScreen(route = "locations", startDestination = Screen.Locations)
}
