package dev.arli.sunnyday.data.location

import arrow.core.Either
import dev.arli.sunnyday.domain.model.location.NamedLocation
import javax.inject.Inject

class LocationRepository @Inject internal constructor(
    private val locationDataSource: LocationDataSource
) {

    suspend fun getCurrentLocation(): Either<Throwable, NamedLocation?> {
        return locationDataSource.getCurrentLocation()
    }
}
