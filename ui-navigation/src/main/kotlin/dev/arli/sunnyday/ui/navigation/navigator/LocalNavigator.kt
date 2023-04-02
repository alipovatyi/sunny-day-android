package dev.arli.sunnyday.ui.navigation.navigator

import androidx.compose.runtime.staticCompositionLocalOf

val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("No LocalNavigator provided")
}
