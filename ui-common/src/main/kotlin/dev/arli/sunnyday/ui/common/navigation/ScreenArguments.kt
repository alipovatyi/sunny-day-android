package dev.arli.sunnyday.ui.common.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument

interface ScreenArguments {

    interface Companion {

        val navArguments: List<NamedNavArgument>

        fun fromStateHandle(stateHandle: SavedStateHandle): ScreenArguments
    }
}
