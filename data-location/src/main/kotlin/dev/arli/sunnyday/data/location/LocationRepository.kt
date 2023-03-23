package dev.arli.sunnyday.data.location

import arrow.core.Either
import arrow.core.continuations.either
import dev.arli.sunnyday.data.db.DatabaseTransactionRunner
import dev.arli.sunnyday.data.db.dao.LocationDao
import dev.arli.sunnyday.data.location.datasource.DeviceLocationDataSource
import dev.arli.sunnyday.data.location.mapper.toLocationEntity
import dev.arli.sunnyday.data.location.mapper.toNamedLocation
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.ForecastLocation
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationRepository @Inject internal constructor(
    private val deviceLocationDataSource: DeviceLocationDataSource,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
    private val locationDao: LocationDao
) {

    suspend fun refreshCurrentLocation(): Either<Throwable, Unit> {
        return either {
            val newCurrentLocation = deviceLocationDataSource.getCurrentLocation().bind()
            databaseTransactionRunner {
                val oldCurrentLocation = locationDao.selectCurrent()?.toNamedLocation()
                if (oldCurrentLocation != newCurrentLocation) {
                    if (oldCurrentLocation != null) {
                        locationDao.deleteCurrent()
                    }
                    if (newCurrentLocation != null) {
                        val locationEntity = newCurrentLocation.toLocationEntity(isCurrent = true)
                        locationDao.insert(locationEntity)
                    }
                }
            }
        }
    }

    fun observeLocations(): Flow<List<ForecastLocation>> {
        return locationDao.observeAll().map { locations ->
            locations.map {
                ForecastLocation(
                    coordinates = Coordinates(
                        latitude = Latitude(it.latitude),
                        longitude = Longitude(it.longitude)
                    ),
                    name = it.name,
                    isCurrent = it.isCurrent
                )
            }
        }
    }
}
