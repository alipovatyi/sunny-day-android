package dev.arli.sunnyday.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.arli.sunnyday.data.db.dao.DailyForecastDao
import dev.arli.sunnyday.data.db.entity.DailyForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomDailyForecastDao : DailyForecastDao {

    @Query("SELECT * FROM ${DailyForecastEntity.TableName}")
    abstract override fun observeAll(): Flow<List<DailyForecastEntity>>

    @Query(
        "SELECT * FROM ${DailyForecastEntity.TableName} " +
            "WHERE ${DailyForecastEntity.Columns.Latitude} = :latitude " +
            "AND ${DailyForecastEntity.Columns.Longitude} = :longitude"
    )
    abstract override fun observeAll(latitude: Double, longitude: Double): Flow<List<DailyForecastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insertOrUpdateAll(vararg dailyForecasts: DailyForecastEntity)

    @Query("DELETE FROM ${DailyForecastEntity.TableName}")
    abstract override suspend fun deleteAll()

    @Query(
        "DELETE FROM ${DailyForecastEntity.TableName} " +
            "WHERE ${DailyForecastEntity.Columns.Latitude} = :latitude " +
            "AND ${DailyForecastEntity.Columns.Longitude} = :longitude"
    )
    abstract override suspend fun deleteAll(latitude: Double, longitude: Double)
}
