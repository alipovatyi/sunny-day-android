package dev.arli.sunnyday.data.location

import arrow.core.Either
import dev.arli.sunnyday.domain.model.location.NamedLocation

interface LocationDataSource {

    suspend fun getCurrentLocation(): Either<Throwable, NamedLocation?>
}
