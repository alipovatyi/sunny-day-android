package dev.arli.sunnyday.ui.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.arli.sunnyday.ui.common.navigation.RootScreen
import dev.arli.sunnyday.ui.common.navigation.Screen
import dev.arli.sunnyday.ui.details.LocationDetailsScreen
import dev.arli.sunnyday.ui.details.LocationDetailsScreenArgs
import dev.arli.sunnyday.ui.locations.LocationsScreen
import dev.arli.sunnyday.ui.navigation.navigator.Navigator

internal fun NavGraphBuilder.addLocationsTopLevel(
    navController: NavController,
    navigator: Navigator
) {
    val rootScreen = RootScreen.Locations

    navigation(
        startDestination = rootScreen.startDestination.createRoute(rootScreen),
        route = rootScreen.route
    ) {
        addLocations(rootScreen, navController, navigator)
        addLocationDetails(rootScreen, navController, navigator)
    }
}

private fun NavGraphBuilder.addLocations(
    rootScreen: RootScreen,
    navController: NavController,
    navigator: Navigator
) {
    composable(route = Screen.Locations.createRoute(rootScreen)) {
        val context = LocalContext.current

        LocationsScreen(
            viewModel = hiltViewModel(),
            openLocationDetails = { coordinates ->
                navController.navigate(Screen.LocationDetails.createRoute(rootScreen, coordinates))
            },
            openUrl = { url -> navigator.openUrl(context, url) }
        )
    }
}

private fun NavGraphBuilder.addLocationDetails(
    rootScreen: RootScreen,
    navController: NavController,
    navigator: Navigator
) {
    composable(
        route = Screen.LocationDetails.createRoute(rootScreen),
        arguments = LocationDetailsScreenArgs.navArguments
    ) {
        val context = LocalContext.current

        LocationDetailsScreen(
            viewModel = hiltViewModel(),
            navigateUp = navController::popBackStack,
            openUrl = { url -> navigator.openUrl(context, url) }
        )
    }
}
