package dev.arli.sunnyday.ui.common.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

inline fun <reified Args : ScreenArguments> ViewModel.screenArgs(
    savedStateHandle: SavedStateHandle
): Lazy<Args> = ScreenArgsLazyImpl(Args::class, savedStateHandle)
