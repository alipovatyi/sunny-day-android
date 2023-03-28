package dev.arli.sunnyday.ui.locations.contract

import dev.arli.sunnyday.model.LocationWithCurrentWeather
import dev.arli.sunnyday.ui.common.base.ViewState

data class LocationsViewState(
    val locations: List<LocationWithCurrentWeather> = emptyList(),
    val showEmptyState: Boolean = false,
    val isRefreshing: Boolean = false
) : ViewState
