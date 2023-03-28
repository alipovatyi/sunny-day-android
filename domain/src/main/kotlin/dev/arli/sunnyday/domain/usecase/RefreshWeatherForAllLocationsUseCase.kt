package dev.arli.sunnyday.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import dev.arli.sunnyday.data.location.LocationRepository
import dev.arli.sunnyday.data.weather.WeatherRepository
import dev.arli.sunnyday.domain.base.SuspendOutUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class RefreshWeatherForAllLocationsUseCase @Inject internal constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) : SuspendOutUseCase<Either<Throwable, Unit>> {

    override suspend fun invoke(): Either<Throwable, Unit> {
        return either {
            val locations = locationRepository.observeLocations().firstOrNull() ?: return@either
            coroutineScope {
                locations.map { location ->
                    async {
                        weatherRepository.refreshWeather(
                            latitude = location.coordinates.latitude,
                            longitude = location.coordinates.longitude
                        ).bind()
                    }
                }
            }.awaitAll()
        }
    }
}
