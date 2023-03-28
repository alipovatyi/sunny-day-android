package dev.arli.sunnyday.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.arli.sunnyday.ui.common.navigation.RootScreen
import dev.arli.sunnyday.ui.common.navigation.Screen
import dev.arli.sunnyday.ui.locations.LocationsScreen

internal fun NavGraphBuilder.addLocationsTopLevel() {
    val rootScreen = RootScreen.Locations

    navigation(
        startDestination = rootScreen.startDestination.createRoute(rootScreen),
        route = rootScreen.route
    ) {
        addLocations(rootScreen)
    }
}

private fun NavGraphBuilder.addLocations(
    rootScreen: RootScreen
) {
    composable(route = Screen.Locations.createRoute(rootScreen)) {
        LocationsScreen(
            viewModel = hiltViewModel()
        )
    }
}
