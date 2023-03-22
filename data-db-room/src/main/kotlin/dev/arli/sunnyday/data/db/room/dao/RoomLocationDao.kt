package dev.arli.sunnyday.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.arli.sunnyday.data.db.dao.LocationDao
import dev.arli.sunnyday.data.db.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomLocationDao : LocationDao {

    @Query("SELECT * FROM ${LocationEntity.TableName} ORDER BY is_current DESC")
    abstract override fun observeAll(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM ${LocationEntity.TableName} WHERE is_current = 1")
    abstract override suspend fun selectCurrent(): LocationEntity?

    @Insert
    abstract override suspend fun insert(location: LocationEntity)

    @Query("DELETE FROM ${LocationEntity.TableName} WHERE is_current = 1")
    abstract override suspend fun deleteCurrent()

    @Query("DELETE FROM ${LocationEntity.TableName} WHERE id = :id")
    abstract override suspend fun delete(id: Long)

    @Query("DELETE FROM ${LocationEntity.TableName}")
    abstract override suspend fun deleteAll()
}
