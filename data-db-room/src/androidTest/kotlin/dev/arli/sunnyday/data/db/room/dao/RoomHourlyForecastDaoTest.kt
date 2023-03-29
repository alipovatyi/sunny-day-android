package dev.arli.sunnyday.data.db.room.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.arli.sunnyday.data.db.entity.HourlyForecastEntity
import dev.arli.sunnyday.data.db.entity.LocationEntity
import dev.arli.sunnyday.data.db.room.RoomSunnyDayDatabase
import java.time.LocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class RoomHourlyForecastDaoTest {

    private lateinit var locationDao: RoomLocationDao
    private lateinit var hourlyForecastDao: RoomHourlyForecastDao
    private lateinit var db: RoomSunnyDayDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, RoomSunnyDayDatabase::class.java).build()
        locationDao = db.locationDao()
        hourlyForecastDao = db.hourlyForecastDao()
    }

    @Test
    fun shouldReturnFlowWithAllHourlyForecastEntities() = runTest {
        val givenHourlyForecastEntity1 = HourlyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDateTime.parse("2023-03-25T00:00"),
            temperature2m = 11.8,
            relativeHumidity2m = 92,
            dewPoint2m = 10.5,
            apparentTemperature = 10.9,
            precipitationProbability = 30,
            precipitation = 0.10,
            weatherCode = 3,
            pressureMsl = 1003.6,
            windSpeed10m = 6.8,
            windDirection10m = 267,
            uvIndex = 0.05
        )
        val givenHourlyForecastEntity2 = HourlyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDateTime.parse("2023-03-25T01:00"),
            temperature2m = 11.0,
            relativeHumidity2m = 91,
            dewPoint2m = 9.5,
            apparentTemperature = 9.6,
            precipitationProbability = 50,
            precipitation = 0.20,
            weatherCode = 2,
            pressureMsl = 1004.2,
            windSpeed10m = 8.7,
            windDirection10m = 265,
            uvIndex = 1.00
        )

        val expectedHourlyForecastWeatherEntities = listOf(givenHourlyForecastEntity1, givenHourlyForecastEntity2)

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        hourlyForecastDao.insertOrUpdateAll(listOf(givenHourlyForecastEntity1, givenHourlyForecastEntity2))

        assertEquals(expectedHourlyForecastWeatherEntities, hourlyForecastDao.observeAll().first())
    }

    @Test
    fun shouldReturnFlowWithAllHourlyForecastEntitiesForCoordinates() = runTest {
        val givenHourlyForecastEntity1 = HourlyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDateTime.parse("2023-03-25T00:00"),
            temperature2m = 11.8,
            relativeHumidity2m = 92,
            dewPoint2m = 10.5,
            apparentTemperature = 10.9,
            precipitationProbability = 30,
            precipitation = 0.10,
            weatherCode = 3,
            pressureMsl = 1003.6,
            windSpeed10m = 6.8,
            windDirection10m = 267,
            uvIndex = 0.05
        )
        val givenHourlyForecastEntity2 = HourlyForecastEntity(
            latitude = 50.45,
            longitude = 30.52,
            time = LocalDateTime.parse("2023-03-25T00:00"),
            temperature2m = 11.0,
            relativeHumidity2m = 91,
            dewPoint2m = 9.5,
            apparentTemperature = 9.6,
            precipitationProbability = 50,
            precipitation = 0.20,
            weatherCode = 2,
            pressureMsl = 1004.2,
            windSpeed10m = 8.7,
            windDirection10m = 265,
            uvIndex = 1.00
        )

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        hourlyForecastDao.insertOrUpdateAll(listOf(givenHourlyForecastEntity1, givenHourlyForecastEntity2))

        val actualHourlyForecastEntity = hourlyForecastDao.observeAll(
            latitude = givenHourlyForecastEntity1.latitude,
            longitude = givenHourlyForecastEntity1.longitude
        ).first()

        assertEquals(listOf(givenHourlyForecastEntity1), actualHourlyForecastEntity)
    }

    @Test
    fun shouldInsertAllHourlyForecastEntitiesIfDoNotExist() = runTest {
        val givenHourlyForecastEntity1 = HourlyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDateTime.parse("2023-03-25T00:00"),
            temperature2m = 11.8,
            relativeHumidity2m = 92,
            dewPoint2m = 10.5,
            apparentTemperature = 10.9,
            precipitationProbability = 30,
            precipitation = 0.10,
            weatherCode = 3,
            pressureMsl = 1003.6,
            windSpeed10m = 6.8,
            windDirection10m = 267,
            uvIndex = 0.05
        )
        val givenHourlyForecastEntity2 = HourlyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDateTime.parse("2023-03-25T01:00"),
            temperature2m = 11.0,
            relativeHumidity2m = 91,
            dewPoint2m = 9.5,
            apparentTemperature = 9.6,
            precipitationProbability = 50,
            precipitation = 0.20,
            weatherCode = 2,
            pressureMsl = 1004.2,
            windSpeed10m = 8.7,
            windDirection10m = 265,
            uvIndex = 1.00
        )

        val expectedHourlyWeatherEntities = listOf(givenHourlyForecastEntity1, givenHourlyForecastEntity2)

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        hourlyForecastDao.insertOrUpdateAll(listOf(givenHourlyForecastEntity1, givenHourlyForecastEntity2))

        assertEquals(expectedHourlyWeatherEntities, hourlyForecastDao.observeAll().first())
    }

    @Test
    fun shouldUpdateAllHourlyForecastEntitiesIfExist() = runTest {
        val givenHourlyForecastEntity1 = HourlyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDateTime.parse("2023-03-25T00:00"),
            temperature2m = 11.8,
            relativeHumidity2m = 92,
            dewPoint2m = 10.5,
            apparentTemperature = 10.9,
            precipitationProbability = 30,
            precipitation = 0.10,
            weatherCode = 3,
            pressureMsl = 1003.6,
            windSpeed10m = 6.8,
            windDirection10m = 267,
            uvIndex = 0.05
        )
        val givenHourlyForecastEntity2 = HourlyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDateTime.parse("2023-03-25T01:00"),
            temperature2m = 11.0,
            relativeHumidity2m = 91,
            dewPoint2m = 9.5,
            apparentTemperature = 9.6,
            precipitationProbability = 50,
            precipitation = 0.20,
            weatherCode = 2,
            pressureMsl = 1004.2,
            windSpeed10m = 8.7,
            windDirection10m = 265,
            uvIndex = 1.00
        )
        val givenHourlyForecastEntity3 = HourlyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDateTime.parse("2023-03-25T00:00"),
            temperature2m = 20.8,
            relativeHumidity2m = 50,
            dewPoint2m = 10.5,
            apparentTemperature = 10.9,
            precipitationProbability = 30,
            precipitation = 0.10,
            weatherCode = 81,
            pressureMsl = 1003.6,
            windSpeed10m = 6.8,
            windDirection10m = 267,
            uvIndex = 0.20
        )
        val givenHourlyForecastEntity4 = HourlyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDateTime.parse("2023-03-25T01:00"),
            temperature2m = 10.0,
            relativeHumidity2m = 45,
            dewPoint2m = 9.5,
            apparentTemperature = 9.6,
            precipitationProbability = 50,
            precipitation = 0.20,
            weatherCode = 2,
            pressureMsl = 1004.2,
            windSpeed10m = 6.9,
            windDirection10m = 300,
            uvIndex = 0.25
        )

        val expectedHourlyWeatherEntities = listOf(givenHourlyForecastEntity3, givenHourlyForecastEntity4)

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        hourlyForecastDao.insertOrUpdateAll(listOf(givenHourlyForecastEntity1, givenHourlyForecastEntity2))
        hourlyForecastDao.insertOrUpdateAll(listOf(givenHourlyForecastEntity3, givenHourlyForecastEntity4))

        assertEquals(expectedHourlyWeatherEntities, hourlyForecastDao.observeAll().first())
    }

    @Test
    fun shouldDeleteAllHourlyForecastEntities() = runTest {
        val givenHourlyForecastEntity1 = HourlyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDateTime.parse("2023-03-25T00:00"),
            temperature2m = 11.8,
            relativeHumidity2m = 92,
            dewPoint2m = 10.5,
            apparentTemperature = 10.9,
            precipitationProbability = 30,
            precipitation = 0.10,
            weatherCode = 3,
            pressureMsl = 1003.6,
            windSpeed10m = 6.8,
            windDirection10m = 267,
            uvIndex = 0.05
        )
        val givenHourlyForecastEntity2 = HourlyForecastEntity(
            latitude = 50.45,
            longitude = 30.52,
            time = LocalDateTime.parse("2023-03-25T00:00"),
            temperature2m = 11.0,
            relativeHumidity2m = 91,
            dewPoint2m = 9.5,
            apparentTemperature = 9.6,
            precipitationProbability = 50,
            precipitation = 0.20,
            weatherCode = 2,
            pressureMsl = 1004.2,
            windSpeed10m = 8.7,
            windDirection10m = 265,
            uvIndex = 1.00
        )

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)
        hourlyForecastDao.insertOrUpdateAll(listOf(givenHourlyForecastEntity1, givenHourlyForecastEntity2))

        hourlyForecastDao.deleteAll()

        assertEquals(emptyList<HourlyForecastEntity>(), hourlyForecastDao.observeAll().first())
    }

    @Test
    fun shouldDeleteAllHourlyForecastEntitiesForCoordinates() = runTest {
        val givenHourlyForecastEntity1 = HourlyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDateTime.parse("2023-03-25T00:00"),
            temperature2m = 11.8,
            relativeHumidity2m = 92,
            dewPoint2m = 10.5,
            apparentTemperature = 10.9,
            precipitationProbability = 30,
            precipitation = 0.10,
            weatherCode = 3,
            pressureMsl = 1003.6,
            windSpeed10m = 6.8,
            windDirection10m = 267,
            uvIndex = 0.05
        )
        val givenHourlyForecastEntity2 = HourlyForecastEntity(
            latitude = 50.45,
            longitude = 30.52,
            time = LocalDateTime.parse("2023-03-25T00:00"),
            temperature2m = 11.0,
            relativeHumidity2m = 91,
            dewPoint2m = 9.5,
            apparentTemperature = 9.6,
            precipitationProbability = 50,
            precipitation = 0.20,
            weatherCode = 2,
            pressureMsl = 1004.2,
            windSpeed10m = 8.7,
            windDirection10m = 265,
            uvIndex = 1.00
        )

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)
        hourlyForecastDao.insertOrUpdateAll(listOf(givenHourlyForecastEntity1, givenHourlyForecastEntity2))

        hourlyForecastDao.deleteAll(
            latitude = givenHourlyForecastEntity1.latitude,
            longitude = givenHourlyForecastEntity1.longitude,
        )

        assertEquals(listOf(givenHourlyForecastEntity2), hourlyForecastDao.observeAll().first())
    }

    private companion object {
        val givenLocationEntity1 = LocationEntity(
            latitude = 52.23,
            longitude = 21.01,
            name = "Warsaw",
            isCurrent = true
        )
        val givenLocationEntity2 = LocationEntity(
            latitude = 50.45,
            longitude = 30.52,
            name = "Kyiv",
            isCurrent = false
        )
    }
}
