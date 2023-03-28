package dev.arli.sunnyday.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import dev.arli.sunnyday.data.location.LocationRepository
import dev.arli.sunnyday.data.weather.WeatherRepository
import dev.arli.sunnyday.domain.base.SuspendOutUseCase
import javax.inject.Inject

class RefreshCurrentLocationUseCase @Inject internal constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) : SuspendOutUseCase<Either<Throwable, Unit>> {

    override suspend fun invoke(): Either<Throwable, Unit> {
        return either {
            val currentLocation = locationRepository.refreshCurrentLocation().bind()
            if (currentLocation != null) {
                weatherRepository.refreshWeather(
                    latitude = currentLocation.coordinates.latitude,
                    longitude = currentLocation.coordinates.longitude
                ).bind()
            }
        }
    }
}
