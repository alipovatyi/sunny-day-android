package dev.arli.sunnyday.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.arli.sunnyday.data.db.dao.CurrentWeatherDao
import dev.arli.sunnyday.data.db.entity.CurrentWeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomCurrentWeatherDao : CurrentWeatherDao {

    @Query("SELECT * FROM ${CurrentWeatherEntity.TableName}")
    abstract override fun observeAll(): Flow<List<CurrentWeatherEntity>>

    @Query(
        "SELECT * FROM ${CurrentWeatherEntity.TableName} " +
            "WHERE ${CurrentWeatherEntity.Columns.Latitude} = :latitude " +
            "AND ${CurrentWeatherEntity.Columns.Longitude} = :longitude"
    )
    abstract override fun observe(latitude: Double, longitude: Double): Flow<CurrentWeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insert(currentWeather: CurrentWeatherEntity)

    @Query(
        "DELETE FROM ${CurrentWeatherEntity.TableName} " +
            "WHERE ${CurrentWeatherEntity.Columns.Latitude} = :latitude " +
            "AND ${CurrentWeatherEntity.Columns.Longitude} = :longitude"
    )
    abstract override suspend fun delete(latitude: Double, longitude: Double)

    @Query("DELETE FROM ${CurrentWeatherEntity.TableName}")
    abstract override suspend fun deleteAll()
}
