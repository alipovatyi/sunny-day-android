package dev.arli.sunnyday.ui.locations.contract

import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.ui.common.base.ViewEffect

sealed class LocationsEffect : ViewEffect {
    object OpenAddLocation : LocationsEffect()
    data class OpenLocationDetails(val coordinates: Coordinates) : LocationsEffect()
}
