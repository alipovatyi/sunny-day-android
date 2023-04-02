package dev.arli.sunnyday.ui.locations.contract

import dev.arli.sunnyday.model.LocationWithCurrentWeather
import dev.arli.sunnyday.model.location.NamedLocation
import dev.arli.sunnyday.ui.common.base.ViewEvent

sealed class LocationsEvent : ViewEvent {
    object AddLocationClick : LocationsEvent()
    data class LocationClick(val location: LocationWithCurrentWeather) : LocationsEvent()
    data class AddLocation(val location: NamedLocation) : LocationsEvent()
    object Refresh : LocationsEvent()
    data class LocationPermissionStateChange(val isGranted: Boolean) : LocationsEvent()
    object CopyrightClick : LocationsEvent()
}
