package dev.arli.sunnyday.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.arli.sunnyday.data.db.dao.LocationDao
import dev.arli.sunnyday.data.db.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomLocationDao : LocationDao {

    @Query("SELECT * FROM ${LocationEntity.TableName} ORDER BY ${LocationEntity.Columns.IsCurrent} DESC")
    abstract override fun observeAll(): Flow<List<LocationEntity>>

    @Query(
        "SELECT * FROM ${LocationEntity.TableName} " +
            "WHERE ${LocationEntity.Columns.Latitude} = :latitude " +
            "AND ${LocationEntity.Columns.Longitude} = :longitude"
    )
    abstract override fun observe(latitude: Double, longitude: Double): Flow<LocationEntity?>

    @Query("SELECT * FROM ${LocationEntity.TableName} WHERE ${LocationEntity.Columns.IsCurrent} = 1")
    abstract override fun observeCurrent(): Flow<LocationEntity?>

    @Query("SELECT * FROM ${LocationEntity.TableName} WHERE ${LocationEntity.Columns.IsCurrent} = 1")
    abstract override suspend fun selectCurrent(): LocationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insertOrUpdate(location: LocationEntity)

    @Query("DELETE FROM ${LocationEntity.TableName} WHERE ${LocationEntity.Columns.IsCurrent} = 1")
    abstract override suspend fun deleteCurrent()

    @Query(
        "DELETE FROM ${LocationEntity.TableName} " +
            "WHERE ${LocationEntity.Columns.Latitude} = :latitude " +
            "AND ${LocationEntity.Columns.Longitude} = :longitude"
    )
    abstract override suspend fun delete(latitude: Double, longitude: Double)

    @Query("DELETE FROM ${LocationEntity.TableName}")
    abstract override suspend fun deleteAll()
}
