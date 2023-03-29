package dev.arli.sunnyday.data.db.dao

import dev.arli.sunnyday.data.db.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

interface LocationDao {

    fun observeAll(): Flow<List<LocationEntity>>

    fun observe(latitude: Double, longitude: Double): Flow<LocationEntity>

    fun observeCurrent(): Flow<LocationEntity>

    suspend fun selectCurrent(): LocationEntity?

    suspend fun insertOrUpdate(location: LocationEntity)

    suspend fun deleteCurrent()

    suspend fun delete(latitude: Double, longitude: Double)

    suspend fun deleteAll()
}
