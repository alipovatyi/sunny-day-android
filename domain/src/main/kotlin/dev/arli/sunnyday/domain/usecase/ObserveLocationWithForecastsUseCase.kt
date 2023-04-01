package dev.arli.sunnyday.domain.usecase

import dev.arli.sunnyday.data.location.LocationRepository
import dev.arli.sunnyday.data.weather.WeatherRepository
import dev.arli.sunnyday.domain.base.InOutUseCase
import dev.arli.sunnyday.model.LocationWithForecasts
import dev.arli.sunnyday.model.location.Coordinates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import kotlinx.coroutines.flow.filterNotNull

class ObserveLocationWithForecastsUseCase @Inject internal constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) : InOutUseCase<ObserveLocationWithForecastsUseCase.Input, Flow<LocationWithForecasts>> {

    // TODO: add filter out old daily and hourly forecasts
    override fun invoke(input: Input): Flow<LocationWithForecasts> {
        return combine(
            locationRepository.observeLocation(input.coordinates).filterNotNull(),
            weatherRepository.observeCurrentWeather(input.coordinates).filterNotNull(),
            weatherRepository.observeDailyForecast(input.coordinates),
            weatherRepository.observeHourlyForecast(input.coordinates)
        ) { location, currentWeather, dailyForecasts, hourlyForecasts ->
            LocationWithForecasts(
                coordinates = location.coordinates,
                name = location.name,
                isCurrent = location.isCurrent,
                currentWeather = currentWeather,
                dailyForecasts = dailyForecasts,
                hourlyForecasts = hourlyForecasts
            )
        }
    }

    data class Input(val coordinates: Coordinates)
}
