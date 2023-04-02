package dev.arli.sunnyday.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.arli.sunnyday.data.db.dao.HourlyForecastDao
import dev.arli.sunnyday.data.db.entity.HourlyForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomHourlyForecastDao : HourlyForecastDao {

    @Query("SELECT * FROM ${HourlyForecastEntity.TableName}")
    abstract override fun observeAll(): Flow<List<HourlyForecastEntity>>

    @Query(
        "SELECT * FROM ${HourlyForecastEntity.TableName} " +
            "WHERE ${HourlyForecastEntity.Columns.Latitude} = :latitude " +
            "AND ${HourlyForecastEntity.Columns.Longitude} = :longitude"
    )
    abstract override fun observeAll(latitude: Double, longitude: Double): Flow<List<HourlyForecastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insertOrUpdateAll(hourlyForecasts: List<HourlyForecastEntity>)

    @Query("DELETE FROM ${HourlyForecastEntity.TableName}")
    abstract override suspend fun deleteAll()

    @Query(
        "DELETE FROM ${HourlyForecastEntity.TableName} " +
            "WHERE ${HourlyForecastEntity.Columns.Latitude} = :latitude " +
            "AND ${HourlyForecastEntity.Columns.Longitude} = :longitude"
    )
    abstract override suspend fun deleteAll(latitude: Double, longitude: Double)
}
