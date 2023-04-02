package dev.arli.sunnyday.domain.usecase

import dev.arli.sunnyday.data.location.LocationRepository
import dev.arli.sunnyday.data.weather.WeatherRepository
import dev.arli.sunnyday.domain.base.OutUseCase
import dev.arli.sunnyday.model.LocationWithCurrentWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveLocationsWithCurrentWeatherUseCase @Inject internal constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) : OutUseCase<Flow<List<LocationWithCurrentWeather>>> {

    override fun invoke(): Flow<List<LocationWithCurrentWeather>> {
        return locationRepository.observeLocations()
            .combine(weatherRepository.observeAllCurrentWeather()) { locations, currentWeatherItems ->
                locations.map { location ->
                    val (locationLatitude, locationLongitude) = location.coordinates
                    LocationWithCurrentWeather(
                        coordinates = location.coordinates,
                        name = location.name,
                        isCurrent = location.isCurrent,
                        currentWeather = currentWeatherItems.firstOrNull {
                            it.latitude == locationLatitude && it.longitude == locationLongitude
                        }
                    )
                }
            }
    }
}
