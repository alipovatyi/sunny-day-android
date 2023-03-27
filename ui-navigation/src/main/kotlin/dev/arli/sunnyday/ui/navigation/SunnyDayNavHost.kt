package dev.arli.sunnyday.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.arli.sunnyday.ui.common.navigation.RootScreen

@Composable
fun SunnyDayNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = RootScreen.Locations.route,
        modifier = modifier
    ) {
        addLocationsTopLevel(navHostController)
    }
}
