package dev.arli.sunnyday.data.location.datasource

import arrow.core.Either
import dev.arli.sunnyday.model.location.NamedLocation

interface DeviceLocationDataSource {

    suspend fun getCurrentLocation(): Either<Throwable, NamedLocation?>
}
