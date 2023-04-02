package dev.arli.sunnyday.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.arli.sunnyday.data.common.DateTimeRepository
import dev.arli.sunnyday.data.config.ConfigRepository
import dev.arli.sunnyday.data.location.LocationRepository
import dev.arli.sunnyday.data.weather.WeatherRepository
import dev.arli.sunnyday.domain.usecase.ObserveLocationWithForecastsUseCase
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.ui.common.base.BaseViewModel
import dev.arli.sunnyday.ui.details.contract.LocationDetailsEffect
import dev.arli.sunnyday.ui.details.contract.LocationDetailsEvent
import dev.arli.sunnyday.ui.details.contract.LocationDetailsViewState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val configRepository: ConfigRepository,
    private val dateTimeRepository: DateTimeRepository,
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
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
            LocationDetailsEvent.DeleteClick -> deleteLocation()
            LocationDetailsEvent.CopyrightClick -> {
                sendEffect(LocationDetailsEffect.OpenUrl(configRepository.getDataSourceUrl()))
            }
            LocationDetailsEvent.Refresh -> refreshLocationWithForecasts()
        }
    }

    private fun observeLocationWithForecasts(coordinates: Coordinates) {
        val input = ObserveLocationWithForecastsUseCase.Input(coordinates)

        observeLocationWithForecastsUseCase(input).onEach { locationWithForecasts ->
            val currentDateTime = dateTimeRepository.currentLocalDateTime()
            val currentDate = dateTimeRepository.currentLocalDate()
            val hourlyForecasts = locationWithForecasts.hourlyForecasts.filter { hourlyForecast ->
                hourlyForecast.time in currentDateTime..currentDateTime.plusDays(1)
            }
            val dailyForecasts = locationWithForecasts.dailyForecasts.filter { dailyForecast ->
                dailyForecast.time >= currentDate
            }
            setState {
                it.copy(
                    locationName = locationWithForecasts.name,
                    isCurrent = locationWithForecasts.isCurrent,
                    currentWeather = locationWithForecasts.currentWeather,
                    hourlyForecasts = hourlyForecasts,
                    dailyForecasts = dailyForecasts,
                    currentHourlyForecast = hourlyForecasts.firstOrNull(),
                    currentDailyForecast = dailyForecasts.firstOrNull()
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun deleteLocation() {
        viewModelScope.launch {
            locationRepository.deleteLocation(args.coordinates).tap {
                sendEffect(LocationDetailsEffect.NavigateUp)
            }
        }
    }

    private fun refreshLocationWithForecasts() {
        setState { it.copy(isRefreshing = true) }

        viewModelScope.launch {
            weatherRepository.refreshWeather(
                latitude = args.coordinates.latitude,
                longitude = args.coordinates.longitude
            ).fold(
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
