package dev.arli.sunnyday.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.arli.sunnyday.data.config.ConfigRepository
import dev.arli.sunnyday.domain.usecase.ObserveLocationWithForecastsUseCase
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.ui.common.base.BaseViewModel
import dev.arli.sunnyday.ui.details.contract.LocationDetailsEffect
import dev.arli.sunnyday.ui.details.contract.LocationDetailsEvent
import dev.arli.sunnyday.ui.details.contract.LocationDetailsViewState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LocationDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val configRepository: ConfigRepository,
    private val observeLocationWithForecastsUseCase: ObserveLocationWithForecastsUseCase
) : BaseViewModel<LocationDetailsEvent, LocationDetailsViewState, LocationDetailsEffect>() {

    private val args: LocationDetailsScreenArgs by screenArgs(savedStateHandle)

    init {
        observeLocationWithForecasts(args.coordinates)
    }

    override fun initialViewState(): LocationDetailsViewState = LocationDetailsViewState()

    override fun handleEvent(event: LocationDetailsEvent) {
        when (event) {
            LocationDetailsEvent.BackClick -> sendEffect(LocationDetailsEffect.NavigateUp)
            LocationDetailsEvent.DeleteClick -> {
                // TODO
            }
            LocationDetailsEvent.CopyrightClick -> {
                sendEffect(LocationDetailsEffect.OpenUrl(configRepository.getDataSourceUrl()))
            }
        }
    }

    private fun observeLocationWithForecasts(coordinates: Coordinates) {
        val input = ObserveLocationWithForecastsUseCase.Input(coordinates)

        observeLocationWithForecastsUseCase(input).onEach { locationWithForecasts ->
            // TODO: filter out old daily forecasts
            // TODO: filter out old hourly forecasts
            setState { it.copy(locationWithForecasts = locationWithForecasts) }
        }.launchIn(viewModelScope)
    }
}
