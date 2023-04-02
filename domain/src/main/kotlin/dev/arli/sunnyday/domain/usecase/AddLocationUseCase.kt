package dev.arli.sunnyday.domain.usecase

import arrow.core.Either
import arrow.core.flatMap
import dev.arli.sunnyday.data.location.LocationRepository
import dev.arli.sunnyday.data.weather.WeatherRepository
import dev.arli.sunnyday.domain.base.SuspendInOutUseCase
import dev.arli.sunnyday.model.location.NamedLocation
import javax.inject.Inject

class AddLocationUseCase @Inject internal constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) : SuspendInOutUseCase<AddLocationUseCase.Input, Either<Throwable, Unit>> {

    override suspend fun invoke(input: Input): Either<Throwable, Unit> {
        return locationRepository.addLocation(input.location).flatMap {
            weatherRepository.refreshWeather(
                latitude = input.location.coordinates.latitude,
                longitude = input.location.coordinates.longitude
            )
        }
    }

    data class Input(val location: NamedLocation)
}
