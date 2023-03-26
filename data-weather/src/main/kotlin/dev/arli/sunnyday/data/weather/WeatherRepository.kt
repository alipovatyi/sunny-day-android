package dev.arli.sunnyday.data.weather

import arrow.core.Either
import arrow.core.flatMap
import dev.arli.sunnyday.data.api.WeatherApi
import dev.arli.sunnyday.data.config.datasource.ConfigDataSource
import dev.arli.sunnyday.data.db.DatabaseTransactionRunner
import dev.arli.sunnyday.data.db.dao.CurrentWeatherDao
import dev.arli.sunnyday.data.db.dao.DailyForecastDao
import dev.arli.sunnyday.data.db.dao.HourlyForecastDao
import dev.arli.sunnyday.data.weather.mapper.toEntity
import dev.arli.sunnyday.data.weather.mapper.toModel
import dev.arli.sunnyday.model.CurrentWeather
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.weather.DailyForecastVariable
import dev.arli.sunnyday.model.weather.HourlyForecastVariable
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeatherRepository @Inject internal constructor(
    private val configDataSource: ConfigDataSource,
    private val weatherApi: WeatherApi,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
    private val currentWeatherDao: CurrentWeatherDao,
    private val dailyForecastDao: DailyForecastDao,
    private val hourlyForecastDao: HourlyForecastDao
) {

    fun observeAllCurrentWeather(): Flow<List<CurrentWeather>> {
        return currentWeatherDao.observeAll().map { currentWeatherEntities ->
            currentWeatherEntities.map { it.toModel() }
        }
    }

    suspend fun refreshWeather(
        latitude: Latitude,
        longitude: Longitude,
        forecastDaysCount: Int
    ): Either<Throwable, Unit> {
        return weatherApi.getWeather(
            latitude = latitude.value,
            longitude = longitude.value,
            forecastDaysCount = forecastDaysCount,
            includeCurrentWeather = true,
            hourlyVariables = HourlyForecastVariable.values().map { it.key },
            dailyVariables = DailyForecastVariable.values().map { it.key },
            timezone = configDataSource.currentTimeZone.id
        ).flatMap { weatherResponseDto ->
            Either.catch {
                val currentWeatherEntity = weatherResponseDto.currentWeather.toEntity(latitude, longitude)
                val dailyForecastEntities = weatherResponseDto.daily.toEntity(latitude, longitude)
                val hourlyForecastEntities = weatherResponseDto.hourly.toEntity(latitude, longitude)

                databaseTransactionRunner {
                    currentWeatherDao.insertOrUpdate(currentWeatherEntity)
                    dailyForecastDao.insertOrUpdateAll(*dailyForecastEntities.toTypedArray())
                    hourlyForecastDao.insertOrUpdateAll(*hourlyForecastEntities.toTypedArray())
                }
            }
        }
    }
}
