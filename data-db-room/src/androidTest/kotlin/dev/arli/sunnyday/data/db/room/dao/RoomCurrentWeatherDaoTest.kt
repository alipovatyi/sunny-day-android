package dev.arli.sunnyday.data.db.room.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.arli.sunnyday.data.db.entity.CurrentWeatherEntity
import dev.arli.sunnyday.data.db.entity.LocationEntity
import dev.arli.sunnyday.data.db.room.RoomSunnyDayDatabase
import java.time.LocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class RoomCurrentWeatherDaoTest {

    private lateinit var locationDao: RoomLocationDao
    private lateinit var currentWeatherDao: RoomCurrentWeatherDao
    private lateinit var db: RoomSunnyDayDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, RoomSunnyDayDatabase::class.java).build()
        locationDao = db.locationDao()
        currentWeatherDao = db.currentWeatherDao()
    }

    @Test
    fun shouldReturnFlowWithAllCurrentWeatherEntities() = runTest {
        val givenCurrentWeatherEntity1 = CurrentWeatherEntity(
            latitude = 52.23,
            longitude = 21.01,
            temperature = 19.0,
            windSpeed = 5.0,
            windDirection = 180,
            weatherCode = 0,
            time = LocalDateTime.parse("2023-03-24T12:00")
        )
        val givenCurrentWeatherEntity2 = CurrentWeatherEntity(
            latitude = 50.45,
            longitude = 30.52,
            temperature = 10.0,
            windSpeed = 25.0,
            windDirection = 90,
            weatherCode = 1,
            time = LocalDateTime.parse("2023-03-24T12:00")
        )

        val expectedCurrentWeatherEntities = listOf(givenCurrentWeatherEntity1, givenCurrentWeatherEntity2)

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        currentWeatherDao.insertOrUpdate(givenCurrentWeatherEntity1)
        currentWeatherDao.insertOrUpdate(givenCurrentWeatherEntity2)

        assertEquals(expectedCurrentWeatherEntities, currentWeatherDao.observeAll().first())
    }

    @Test
    fun shouldReturnFlowWithCurrentWeatherEntityForCoordinatesIfExists() = runTest {
        val givenCurrentWeatherEntity1 = CurrentWeatherEntity(
            latitude = 52.23,
            longitude = 21.01,
            temperature = 19.0,
            windSpeed = 5.0,
            windDirection = 180,
            weatherCode = 0,
            time = LocalDateTime.parse("2023-03-24T12:00")
        )
        val givenCurrentWeatherEntity2 = CurrentWeatherEntity(
            latitude = 50.45,
            longitude = 30.52,
            temperature = 10.0,
            windSpeed = 25.0,
            windDirection = 90,
            weatherCode = 1,
            time = LocalDateTime.parse("2023-03-24T12:00")
        )

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        currentWeatherDao.insertOrUpdate(givenCurrentWeatherEntity1)
        currentWeatherDao.insertOrUpdate(givenCurrentWeatherEntity2)

        val actualCurrentWeatherEntity = currentWeatherDao.observe(
            latitude = givenCurrentWeatherEntity1.latitude,
            longitude = givenCurrentWeatherEntity1.longitude
        ).first()

        assertEquals(givenCurrentWeatherEntity1, actualCurrentWeatherEntity)
    }

    @Test
    fun shouldReturnFlowWithNullIfCurrentWeatherEntityForCoordinatesDoesNotExist() = runTest {
        val givenCurrentWeatherEntity = CurrentWeatherEntity(
            latitude = 52.23,
            longitude = 21.01,
            temperature = 19.0,
            windSpeed = 5.0,
            windDirection = 180,
            weatherCode = 0,
            time = LocalDateTime.parse("2023-03-24T12:00")
        )

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        currentWeatherDao.insertOrUpdate(givenCurrentWeatherEntity)

        val actualCurrentWeatherEntity = currentWeatherDao.observe(latitude = 50.45, longitude = 30.52).first()

        assertNull(actualCurrentWeatherEntity)
    }

    @Test
    fun shouldInsertCurrentWeatherEntityIfDoesNotExist() = runTest {
        val givenCurrentWeatherEntity = CurrentWeatherEntity(
            latitude = 52.23,
            longitude = 21.01,
            temperature = 19.0,
            windSpeed = 5.0,
            windDirection = 180,
            weatherCode = 0,
            time = LocalDateTime.parse("2023-03-24T12:00")
        )

        locationDao.insertOrUpdate(givenLocationEntity1)

        currentWeatherDao.insertOrUpdate(givenCurrentWeatherEntity)

        assertEquals(listOf(givenCurrentWeatherEntity), currentWeatherDao.observeAll().first())
    }

    @Test
    fun shouldUpdateCurrentWeatherEntityIfExists() = runTest {
        val givenCurrentWeatherEntity1 = CurrentWeatherEntity(
            latitude = 52.23,
            longitude = 21.01,
            temperature = 19.0,
            windSpeed = 5.0,
            windDirection = 180,
            weatherCode = 0,
            time = LocalDateTime.parse("2023-03-24T12:00")
        )
        val givenCurrentWeatherEntity2 = CurrentWeatherEntity(
            latitude = 52.23,
            longitude = 21.01,
            temperature = 25.0,
            windSpeed = 10.0,
            windDirection = 35,
            weatherCode = 3,
            time = LocalDateTime.parse("2023-03-24T12:00")
        )

        locationDao.insertOrUpdate(givenLocationEntity1)

        currentWeatherDao.insertOrUpdate(givenCurrentWeatherEntity1)
        currentWeatherDao.insertOrUpdate(givenCurrentWeatherEntity2)

        assertEquals(listOf(givenCurrentWeatherEntity2), currentWeatherDao.observeAll().first())
    }

    @Test
    fun shouldDeleteAllCurrentWeatherEntities() = runTest {
        val givenCurrentWeatherEntity1 = CurrentWeatherEntity(
            latitude = 52.23,
            longitude = 21.01,
            temperature = 19.0,
            windSpeed = 5.0,
            windDirection = 180,
            weatherCode = 0,
            time = LocalDateTime.parse("2023-03-24T12:00")
        )
        val givenCurrentWeatherEntity2 = CurrentWeatherEntity(
            latitude = 50.45,
            longitude = 30.52,
            temperature = 10.0,
            windSpeed = 25.0,
            windDirection = 90,
            weatherCode = 1,
            time = LocalDateTime.parse("2023-03-24T12:00")
        )

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)
        currentWeatherDao.insertOrUpdate(givenCurrentWeatherEntity1)
        currentWeatherDao.insertOrUpdate(givenCurrentWeatherEntity2)

        currentWeatherDao.deleteAll()

        assertEquals(emptyList<CurrentWeatherEntity>(), currentWeatherDao.observeAll().first())
    }

    @Test
    fun shouldDeleteCurrentWeatherEntityForCoordinates() = runTest {
        val givenCurrentWeatherEntity1 = CurrentWeatherEntity(
            latitude = 52.23,
            longitude = 21.01,
            temperature = 19.0,
            windSpeed = 5.0,
            windDirection = 180,
            weatherCode = 0,
            time = LocalDateTime.parse("2023-03-24T12:00")
        )
        val givenCurrentWeatherEntity2 = CurrentWeatherEntity(
            latitude = 50.45,
            longitude = 30.52,
            temperature = 10.0,
            windSpeed = 25.0,
            windDirection = 90,
            weatherCode = 1,
            time = LocalDateTime.parse("2023-03-24T12:00")
        )

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)
        currentWeatherDao.insertOrUpdate(givenCurrentWeatherEntity1)
        currentWeatherDao.insertOrUpdate(givenCurrentWeatherEntity2)

        currentWeatherDao.delete(
            latitude = givenCurrentWeatherEntity1.latitude,
            longitude = givenCurrentWeatherEntity1.longitude
        )

        assertEquals(listOf(givenCurrentWeatherEntity2), currentWeatherDao.observeAll().first())
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
