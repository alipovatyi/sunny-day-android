package dev.arli.sunnyday.ui.locations

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.arli.sunnyday.domain.usecase.ObserveLocationsWithCurrentWeatherUseCase
import dev.arli.sunnyday.ui.common.base.BaseViewModel
import dev.arli.sunnyday.ui.locations.contract.LocationsEffect
import dev.arli.sunnyday.ui.locations.contract.LocationsEvent
import dev.arli.sunnyday.ui.locations.contract.LocationsViewState
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val observeLocationsWithCurrentWeatherUseCase: ObserveLocationsWithCurrentWeatherUseCase
) : BaseViewModel<LocationsEvent, LocationsViewState, LocationsEffect>() {

    init {
        observeLocationsWithCurrentWeather()
    }

    override fun initialViewState(): LocationsViewState = LocationsViewState()

    override fun handleEvent(event: LocationsEvent) {
        when (event) {
            is LocationsEvent.AddLocationClick -> sendEffect(LocationsEffect.OpenAddLocation)
            is LocationsEvent.LocationClick -> {
                sendEffect(LocationsEffect.OpenLocationDetails(event.location.coordinates))
            }
        }
    }

    private fun observeLocationsWithCurrentWeather() {
        observeLocationsWithCurrentWeatherUseCase().onEach { locations ->
            setState { it.copy(locations = locations) }
        }.launchIn(viewModelScope)
    }
}
