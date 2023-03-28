package dev.arli.sunnyday.ui.locations.contract

import androidx.compose.runtime.Immutable
import dev.arli.sunnyday.model.LocationWithCurrentWeather
import dev.arli.sunnyday.ui.common.base.ViewState

@Immutable
data class LocationsViewState(
    val locations: List<LocationWithCurrentWeather> = emptyList(),
    val isRefreshing: Boolean = false
) : ViewState {

    val showAddLocationButton: Boolean = locations.filter { it.isCurrent.not() }.size < MaxLocationCount

    private companion object {
        const val MaxLocationCount = 5
    }
}
