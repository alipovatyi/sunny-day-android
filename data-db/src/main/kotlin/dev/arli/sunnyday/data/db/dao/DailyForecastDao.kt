package dev.arli.sunnyday.data.db.dao

import dev.arli.sunnyday.data.db.entity.DailyForecastEntity
import kotlinx.coroutines.flow.Flow

interface DailyForecastDao {

    fun observeAll(): Flow<List<DailyForecastEntity>>

    fun observeAll(latitude: Double, longitude: Double): Flow<List<DailyForecastEntity>>

    suspend fun insertOrUpdateAll(vararg dailyForecasts: DailyForecastEntity)

    suspend fun deleteAll()

    suspend fun deleteAll(latitude: Double, longitude: Double)
}
