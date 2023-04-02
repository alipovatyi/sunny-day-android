package dev.arli.sunnyday.ui.locations

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.arli.sunnyday.data.config.ConfigRepository
import dev.arli.sunnyday.domain.usecase.AddLocationUseCase
import dev.arli.sunnyday.domain.usecase.ObserveLocationsWithCurrentWeatherUseCase
import dev.arli.sunnyday.domain.usecase.RefreshCurrentLocationUseCase
import dev.arli.sunnyday.domain.usecase.RefreshWeatherForAllLocationsUseCase
import dev.arli.sunnyday.model.location.NamedLocation
import dev.arli.sunnyday.ui.common.base.BaseViewModel
import dev.arli.sunnyday.ui.locations.contract.LocationsEffect
import dev.arli.sunnyday.ui.locations.contract.LocationsEvent
import dev.arli.sunnyday.ui.locations.contract.LocationsViewState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val observeLocationsWithCurrentWeatherUseCase: ObserveLocationsWithCurrentWeatherUseCase,
    private val addLocationUseCase: AddLocationUseCase,
    private val refreshWeatherForAllLocationsUseCase: RefreshWeatherForAllLocationsUseCase,
    private val refreshCurrentLocationUseCase: RefreshCurrentLocationUseCase,
    private val configRepository: ConfigRepository
) : BaseViewModel<LocationsEvent, LocationsViewState, LocationsEffect>() {

    init {
        observeLocationsWithCurrentWeather()
        refreshWeatherForAllLocations()
    }

    override fun initialViewState(): LocationsViewState = LocationsViewState()

    override fun handleEvent(event: LocationsEvent) {
        when (event) {
            is LocationsEvent.AddLocationClick -> sendEffect(LocationsEffect.OpenAddLocation)
            is LocationsEvent.LocationClick -> {
                sendEffect(LocationsEffect.OpenLocationDetails(event.location.coordinates))
            }
            is LocationsEvent.AddLocation -> addLocation(event.location)
            is LocationsEvent.Refresh -> refreshWeatherForAllLocations()
            is LocationsEvent.LocationPermissionStateChange -> {
                if (event.isGranted) {
                    refreshCurrentLocation()
                }
            }
            is LocationsEvent.CopyrightClick -> {
                sendEffect(LocationsEffect.OpenUrl(configRepository.getDataSourceUrl()))
            }
        }
    }

    private fun observeLocationsWithCurrentWeather() {
        observeLocationsWithCurrentWeatherUseCase().onEach { locations ->
            setState { it.copy(locations = locations) }
        }.launchIn(viewModelScope)
    }

    private fun addLocation(location: NamedLocation) {
        viewModelScope.launch {
            addLocationUseCase(AddLocationUseCase.Input(location = location)).tap {
                sendEffect(LocationsEffect.ScrollToBottom)
            }
        }
    }

    private fun refreshWeatherForAllLocations() {
        setState { it.copy(isRefreshing = true) }

        viewModelScope.launch {
            refreshWeatherForAllLocationsUseCase().fold(
                ifLeft = {
                    setState { it.copy(isRefreshing = false) }
                },
                ifRight = {
                    setState { it.copy(isRefreshing = false) }
                }
            )
        }
    }

    private fun refreshCurrentLocation() {
        setState { it.copy(isRefreshing = true) }

        viewModelScope.launch {
            refreshCurrentLocationUseCase().fold(
                ifLeft = {
                    setState { it.copy(isRefreshing = false) }
                },
                ifRight = {
                    setState { it.copy(isRefreshing = false) }
                }
            )
        }
    }
}
