package dev.arli.sunnyday.data.location.mapper

import dev.arli.sunnyday.data.db.entity.LocationEntity
import dev.arli.sunnyday.model.location.NamedLocation

fun NamedLocation.toLocationEntity(isCurrent: Boolean): LocationEntity {
    return LocationEntity(
        latitude = coordinates.latitude.value,
        longitude = coordinates.longitude.value,
        name = name,
        isCurrent = isCurrent
    )
}
