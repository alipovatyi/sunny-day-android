package dev.arli.sunnyday.ui.locations.contract

import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.ui.common.base.ViewEffect
import java.net.URL

sealed class LocationsEffect : ViewEffect {
    object OpenAddLocation : LocationsEffect()
    data class OpenLocationDetails(val coordinates: Coordinates) : LocationsEffect()
    object ScrollToBottom : LocationsEffect()
    data class OpenUrl(val url: URL) : LocationsEffect()
}
