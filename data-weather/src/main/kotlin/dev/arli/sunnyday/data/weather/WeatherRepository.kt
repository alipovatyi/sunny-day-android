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
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.weather.DailyForecast
import dev.arli.sunnyday.model.weather.DailyForecastVariable
import dev.arli.sunnyday.model.weather.HourlyForecast
import dev.arli.sunnyday.model.weather.HourlyForecastVariable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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

    fun observeCurrentWeather(coordinates: Coordinates): Flow<CurrentWeather> {
        return currentWeatherDao.observe(
            latitude = coordinates.latitude.value,
            longitude = coordinates.longitude.value
        ).map { it.toModel() }
    }

    fun observeDailyForecast(coordinates: Coordinates): Flow<List<DailyForecast>> {
        return dailyForecastDao.observeAll(
            latitude = coordinates.latitude.value,
            longitude = coordinates.longitude.value
        ).map { dailyForecastEntities ->
            dailyForecastEntities.map { it.toModel() }
        }
    }

    fun observeHourlyForecast(coordinates: Coordinates): Flow<List<HourlyForecast>> {
        return hourlyForecastDao.observeAll(
            latitude = coordinates.latitude.value,
            longitude = coordinates.longitude.value
        ).map { hourlyForecastEntities ->
            hourlyForecastEntities.map { it.toModel() }
        }
    }

    @Suppress("ForbiddenComment")
    // TODO: consider using Coordinates class instead of latitude/longitude
    // TODO: remove old weather items
    suspend fun refreshWeather(latitude: Latitude, longitude: Longitude): Either<Throwable, Unit> {
        return weatherApi.getWeather(
            latitude = latitude.value,
            longitude = longitude.value,
            forecastDaysCount = configDataSource.forecastDaysCount,
            includeCurrentWeather = true,
            hourlyVariables = HourlyForecastVariable.values().map { it.key },
            dailyVariables = DailyForecastVariable.values().map { it.key },
            timezone = "auto"
        ).flatMap { weatherResponseDto ->
            Either.catch {
                val currentWeatherEntity = weatherResponseDto.currentWeather.toEntity(latitude, longitude)
                val dailyForecastEntities = weatherResponseDto.daily.toEntity(latitude, longitude)
                val hourlyForecastEntities = weatherResponseDto.hourly.toEntity(latitude, longitude)

                databaseTransactionRunner {
                    currentWeatherDao.insertOrUpdate(currentWeatherEntity)
                    dailyForecastDao.insertOrUpdateAll(dailyForecastEntities)
                    hourlyForecastDao.insertOrUpdateAll(hourlyForecastEntities)
                }
            }
        }
    }
}
