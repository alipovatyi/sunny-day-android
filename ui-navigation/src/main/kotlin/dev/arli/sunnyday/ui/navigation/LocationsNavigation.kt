package dev.arli.sunnyday.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.arli.sunnyday.ui.common.navigation.RootScreen
import dev.arli.sunnyday.ui.common.navigation.Screen
import dev.arli.sunnyday.ui.locations.LocationsScreen

internal fun NavGraphBuilder.addLocationsTopLevel(navController: NavController) {
    val rootScreen = RootScreen.Locations

    navigation(
        startDestination = rootScreen.startDestination.createRoute(rootScreen),
        route = rootScreen.route
    ) {
        addLocations(rootScreen, navController)
    }
}

private fun NavGraphBuilder.addLocations(
    rootScreen: RootScreen,
    navController: NavController
) {
    composable(route = Screen.Locations.createRoute(rootScreen)) {
        LocationsScreen(
            viewModel = hiltViewModel()
        )
    }
}
