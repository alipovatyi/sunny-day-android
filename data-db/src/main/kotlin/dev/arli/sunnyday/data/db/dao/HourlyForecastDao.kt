package dev.arli.sunnyday.data.db.dao

import dev.arli.sunnyday.data.db.entity.HourlyForecastEntity
import kotlinx.coroutines.flow.Flow

interface HourlyForecastDao {

    fun observeAll(): Flow<List<HourlyForecastEntity>>

    fun observeAll(latitude: Double, longitude: Double): Flow<List<HourlyForecastEntity>>

    suspend fun insertOrUpdateAll(vararg hourlyForecasts: HourlyForecastEntity)

    suspend fun deleteAll()

    suspend fun deleteAll(latitude: Double, longitude: Double)
}
