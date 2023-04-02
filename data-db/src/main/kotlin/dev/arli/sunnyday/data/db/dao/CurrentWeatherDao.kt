package dev.arli.sunnyday.data.db.dao

import dev.arli.sunnyday.data.db.entity.CurrentWeatherEntity
import kotlinx.coroutines.flow.Flow

interface CurrentWeatherDao {

    fun observeAll(): Flow<List<CurrentWeatherEntity>>

    fun observe(latitude: Double, longitude: Double): Flow<CurrentWeatherEntity?>

    suspend fun insertOrUpdate(currentWeather: CurrentWeatherEntity)

    suspend fun delete(latitude: Double, longitude: Double)

    suspend fun deleteAll()
}
