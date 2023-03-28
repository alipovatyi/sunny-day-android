package dev.arli.sunnyday.data.db.room.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.arli.sunnyday.data.db.entity.DailyForecastEntity
import dev.arli.sunnyday.data.db.entity.LocationEntity
import dev.arli.sunnyday.data.db.room.RoomSunnyDayDatabase
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class RoomDailyForecastDaoTest {

    private lateinit var locationDao: RoomLocationDao
    private lateinit var dailyForecastDao: RoomDailyForecastDao
    private lateinit var db: RoomSunnyDayDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, RoomSunnyDayDatabase::class.java).build()
        locationDao = db.locationDao()
        dailyForecastDao = db.dailyForecastDao()
    }

    @Test
    fun shouldReturnFlowWithAllDailyForecastEntities() = runTest {
        val givenDailyForecastEntity1 = DailyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDate.parse("2023-03-25"),
            weatherCode = 95,
            temperature2mMax = 13.8,
            temperature2mMin = 7.4,
            apparentTemperatureMax = 11.1,
            apparentTemperatureMin = 4.9,
            sunrise = LocalDateTime.parse("2023-03-25T05:25"),
            sunset = LocalDateTime.parse("2023-03-25T17:58"),
            uvIndexMax = 4.30
        )
        val givenDailyForecastEntity2 = DailyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDate.parse("2023-03-26"),
            weatherCode = 61,
            temperature2mMax = 9.9,
            temperature2mMin = 6.2,
            apparentTemperatureMax = 6.9,
            apparentTemperatureMin = 3.4,
            sunrise = LocalDateTime.parse("2023-03-26T05:23"),
            sunset = LocalDateTime.parse("2023-03-26T18:00"),
            uvIndexMax = 3.95
        )

        val expectedDailyWeatherEntities = listOf(givenDailyForecastEntity1, givenDailyForecastEntity2)

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        dailyForecastDao.insertOrUpdateAll(listOf(givenDailyForecastEntity1, givenDailyForecastEntity2))

        assertEquals(expectedDailyWeatherEntities, dailyForecastDao.observeAll().first())
    }

    @Test
    fun shouldReturnFlowWithAllDailyForecastEntitiesForCoordinates() = runTest {
        val givenDailyForecastEntity1 = DailyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDate.parse("2023-03-25"),
            weatherCode = 95,
            temperature2mMax = 13.8,
            temperature2mMin = 7.4,
            apparentTemperatureMax = 11.1,
            apparentTemperatureMin = 4.9,
            sunrise = LocalDateTime.parse("2023-03-25T05:25"),
            sunset = LocalDateTime.parse("2023-03-25T17:58"),
            uvIndexMax = 4.30
        )
        val givenDailyForecastEntity2 = DailyForecastEntity(
            latitude = 50.45,
            longitude = 30.52,
            time = LocalDate.parse("2023-03-25"),
            weatherCode = 61,
            temperature2mMax = 9.9,
            temperature2mMin = 6.2,
            apparentTemperatureMax = 6.9,
            apparentTemperatureMin = 3.4,
            sunrise = LocalDateTime.parse("2023-03-25T05:23"),
            sunset = LocalDateTime.parse("2023-03-25T18:00"),
            uvIndexMax = 3.95
        )

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        dailyForecastDao.insertOrUpdateAll(listOf(givenDailyForecastEntity1, givenDailyForecastEntity2))

        val actualDailyForecastEntity = dailyForecastDao.observeAll(
            latitude = givenDailyForecastEntity1.latitude,
            longitude = givenDailyForecastEntity1.longitude
        ).first()

        assertEquals(listOf(givenDailyForecastEntity1), actualDailyForecastEntity)
    }

    @Test
    fun shouldInsertAllDailyForecastEntitiesIfDoNotExist() = runTest {
        val givenDailyForecastEntity1 = DailyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDate.parse("2023-03-25"),
            weatherCode = 95,
            temperature2mMax = 13.8,
            temperature2mMin = 7.4,
            apparentTemperatureMax = 11.1,
            apparentTemperatureMin = 4.9,
            sunrise = LocalDateTime.parse("2023-03-25T05:25"),
            sunset = LocalDateTime.parse("2023-03-25T17:58"),
            uvIndexMax = 4.30
        )
        val givenDailyForecastEntity2 = DailyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDate.parse("2023-03-26"),
            weatherCode = 61,
            temperature2mMax = 9.9,
            temperature2mMin = 6.2,
            apparentTemperatureMax = 6.9,
            apparentTemperatureMin = 3.4,
            sunrise = LocalDateTime.parse("2023-03-26T05:23"),
            sunset = LocalDateTime.parse("2023-03-26T18:00"),
            uvIndexMax = 3.95
        )

        val expectedDailyWeatherEntities = listOf(givenDailyForecastEntity1, givenDailyForecastEntity2)

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        dailyForecastDao.insertOrUpdateAll(listOf(givenDailyForecastEntity1, givenDailyForecastEntity2))

        assertEquals(expectedDailyWeatherEntities, dailyForecastDao.observeAll().first())
    }

    @Test
    fun shouldUpdateAllDailyForecastEntitiesIfExist() = runTest {
        val givenDailyForecastEntity1 = DailyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDate.parse("2023-03-25"),
            weatherCode = 95,
            temperature2mMax = 13.8,
            temperature2mMin = 7.4,
            apparentTemperatureMax = 11.1,
            apparentTemperatureMin = 4.9,
            sunrise = LocalDateTime.parse("2023-03-25T05:25"),
            sunset = LocalDateTime.parse("2023-03-25T17:58"),
            uvIndexMax = 4.30
        )
        val givenDailyForecastEntity2 = DailyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDate.parse("2023-03-26"),
            weatherCode = 61,
            temperature2mMax = 9.9,
            temperature2mMin = 6.2,
            apparentTemperatureMax = 6.9,
            apparentTemperatureMin = 3.4,
            sunrise = LocalDateTime.parse("2023-03-26T05:23"),
            sunset = LocalDateTime.parse("2023-03-26T18:00"),
            uvIndexMax = 3.95
        )
        val givenDailyForecastEntity3 = DailyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDate.parse("2023-03-25"),
            weatherCode = 95,
            temperature2mMax = 20.0,
            temperature2mMin = 7.4,
            apparentTemperatureMax = 16.0,
            apparentTemperatureMin = 2.0,
            sunrise = LocalDateTime.parse("2023-03-25T05:25"),
            sunset = LocalDateTime.parse("2023-03-25T17:58"),
            uvIndexMax = 4.30
        )
        val givenDailyForecastEntity4 = DailyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDate.parse("2023-03-26"),
            weatherCode = 1,
            temperature2mMax = 13.0,
            temperature2mMin = 4.5,
            apparentTemperatureMax = 6.9,
            apparentTemperatureMin = 3.4,
            sunrise = LocalDateTime.parse("2023-03-26T05:23"),
            sunset = LocalDateTime.parse("2023-03-26T18:00"),
            uvIndexMax = 4.20
        )

        val expectedDailyWeatherEntities = listOf(givenDailyForecastEntity3, givenDailyForecastEntity4)

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        dailyForecastDao.insertOrUpdateAll(listOf(givenDailyForecastEntity1, givenDailyForecastEntity2))
        dailyForecastDao.insertOrUpdateAll(listOf(givenDailyForecastEntity3, givenDailyForecastEntity4))

        assertEquals(expectedDailyWeatherEntities, dailyForecastDao.observeAll().first())
    }

    @Test
    fun shouldDeleteAllDailyForecastEntities() = runTest {
        val givenDailyForecastEntity1 = DailyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDate.parse("2023-03-25"),
            weatherCode = 95,
            temperature2mMax = 13.8,
            temperature2mMin = 7.4,
            apparentTemperatureMax = 11.1,
            apparentTemperatureMin = 4.9,
            sunrise = LocalDateTime.parse("2023-03-25T05:25"),
            sunset = LocalDateTime.parse("2023-03-25T17:58"),
            uvIndexMax = 4.30
        )
        val givenDailyForecastEntity2 = DailyForecastEntity(
            latitude = 50.45,
            longitude = 30.52,
            time = LocalDate.parse("2023-03-25"),
            weatherCode = 61,
            temperature2mMax = 9.9,
            temperature2mMin = 6.2,
            apparentTemperatureMax = 6.9,
            apparentTemperatureMin = 3.4,
            sunrise = LocalDateTime.parse("2023-03-25T05:23"),
            sunset = LocalDateTime.parse("2023-03-25T18:00"),
            uvIndexMax = 3.95
        )

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)
        dailyForecastDao.insertOrUpdateAll(listOf(givenDailyForecastEntity1, givenDailyForecastEntity2))

        dailyForecastDao.deleteAll()

        assertEquals(emptyList<DailyForecastEntity>(), dailyForecastDao.observeAll().first())
    }

    @Test
    fun shouldDeleteAllDailyForecastEntitiesForCoordinates() = runTest {
        val givenDailyForecastEntity1 = DailyForecastEntity(
            latitude = 52.23,
            longitude = 21.01,
            time = LocalDate.parse("2023-03-25"),
            weatherCode = 95,
            temperature2mMax = 13.8,
            temperature2mMin = 7.4,
            apparentTemperatureMax = 11.1,
            apparentTemperatureMin = 4.9,
            sunrise = LocalDateTime.parse("2023-03-25T05:25"),
            sunset = LocalDateTime.parse("2023-03-25T17:58"),
            uvIndexMax = 4.30
        )
        val givenDailyForecastEntity2 = DailyForecastEntity(
            latitude = 50.45,
            longitude = 30.52,
            time = LocalDate.parse("2023-03-25"),
            weatherCode = 61,
            temperature2mMax = 9.9,
            temperature2mMin = 6.2,
            apparentTemperatureMax = 6.9,
            apparentTemperatureMin = 3.4,
            sunrise = LocalDateTime.parse("2023-03-25T05:23"),
            sunset = LocalDateTime.parse("2023-03-25T18:00"),
            uvIndexMax = 3.95
        )

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)
        dailyForecastDao.insertOrUpdateAll(listOf(givenDailyForecastEntity1, givenDailyForecastEntity2))

        dailyForecastDao.deleteAll(
            latitude = givenDailyForecastEntity1.latitude,
            longitude = givenDailyForecastEntity1.longitude,
        )

        assertEquals(listOf(givenDailyForecastEntity2), dailyForecastDao.observeAll().first())
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
