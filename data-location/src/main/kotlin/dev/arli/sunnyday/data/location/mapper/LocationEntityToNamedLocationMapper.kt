package dev.arli.sunnyday.data.location.mapper

import dev.arli.sunnyday.data.db.entity.LocationEntity
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.location.NamedLocation

fun LocationEntity.toNamedLocation(): NamedLocation {
    return NamedLocation(
        coordinates = Coordinates(
            Latitude(value = latitude),
            Longitude(longitude)
        ) ,
        name = name
    )
}