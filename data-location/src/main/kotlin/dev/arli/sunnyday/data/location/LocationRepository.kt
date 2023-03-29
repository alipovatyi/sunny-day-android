package dev.arli.sunnyday.data.location

import arrow.core.Either
import arrow.core.continuations.either
import dev.arli.sunnyday.data.db.DatabaseTransactionRunner
import dev.arli.sunnyday.data.db.dao.LocationDao
import dev.arli.sunnyday.data.location.datasource.DeviceLocationDataSource
import dev.arli.sunnyday.data.location.mapper.toLocationEntity
import dev.arli.sunnyday.data.location.mapper.toNamedLocation
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.NamedLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationRepository @Inject internal constructor(
    private val deviceLocationDataSource: DeviceLocationDataSource,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
    private val locationDao: LocationDao
) {

    suspend fun refreshCurrentLocation(): Either<Throwable, NamedLocation?> {
        return either {
            val newCurrentLocation = deviceLocationDataSource.getCurrentLocation().tapLeft { error ->
                if (error is SecurityException) {
                    locationDao.deleteCurrent()
                }
            }.bind()
            databaseTransactionRunner {
                val oldCurrentLocation = locationDao.selectCurrent()?.toNamedLocation()
                if (oldCurrentLocation != newCurrentLocation) {
                    if (oldCurrentLocation != null) {
                        locationDao.deleteCurrent()
                    }
                    if (newCurrentLocation != null) {
                        val locationEntity = newCurrentLocation.toLocationEntity()
                        locationDao.insertOrUpdate(locationEntity)
                    }
                }
                newCurrentLocation
            }
        }
    }

    fun observeLocations(): Flow<List<NamedLocation>> {
        return locationDao.observeAll().map { locationEntities ->
            locationEntities.map { it.toNamedLocation() }
        }
    }

    fun observeLocation(coordinates: Coordinates): Flow<NamedLocation> {
        return locationDao.observe(
            latitude = coordinates.latitude.value,
            longitude = coordinates.longitude.value
        ).map {
            it.toNamedLocation()
        }
    }

    suspend fun addLocation(location: NamedLocation): Either<Throwable, Unit> {
        return Either.catch {
            databaseTransactionRunner {
                locationDao.insertOrUpdate(location.toLocationEntity())
            }
        }
    }
}
