package dev.arli.sunnyday.data.db.dao

import dev.arli.sunnyday.data.db.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

interface LocationDao {

    fun observeAll(): Flow<List<LocationEntity>>

    suspend fun selectCurrent(): LocationEntity?

    suspend fun insert(location: LocationEntity)

    suspend fun deleteCurrent()

    suspend fun delete(id: Long)

    suspend fun deleteAll()
}
