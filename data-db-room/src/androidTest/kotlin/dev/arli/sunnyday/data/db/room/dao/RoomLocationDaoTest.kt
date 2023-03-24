package dev.arli.sunnyday.data.db.room.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.arli.sunnyday.data.db.entity.CurrentWeatherEntity
import dev.arli.sunnyday.data.db.entity.LocationEntity
import dev.arli.sunnyday.data.db.room.RoomSunnyDayDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class RoomLocationDaoTest {

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

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun shouldReturnFlowWithAllLocationEntities() = runTest {
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

        val expectedLocationEntities = listOf(givenLocationEntity1, givenLocationEntity2)

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        assertEquals(expectedLocationEntities, locationDao.observeAll().first())
    }

    @Test
    fun shouldReturnFlowWithCurrentLocationEntityIfExists() = runTest {
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

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        assertEquals(givenLocationEntity1, locationDao.observeCurrent().first())
    }

    @Test
    fun shouldReturnCurrentLocationEntityIfExists() = runTest {
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

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        assertEquals(givenLocationEntity1, locationDao.selectCurrent())
    }

    @Test
    fun shouldReturnNullIfCurrentLocationEntityDoesNotExists() = runTest {
        val givenLocationEntity = LocationEntity(
            latitude = 50.45,
            longitude = 30.52,
            name = "Kyiv",
            isCurrent = false
        )

        locationDao.insertOrUpdate(givenLocationEntity)

        assertNull(locationDao.selectCurrent())
    }

    @Test
    fun shouldInsertLocationEntityIfDoesNotExist() = runTest {
        val givenLocationEntity = LocationEntity(
            latitude = 52.23,
            longitude = 21.01,
            name = "Warsaw",
            isCurrent = true
        )

        locationDao.insertOrUpdate(givenLocationEntity)

        assertEquals(listOf(givenLocationEntity), locationDao.observeAll().first())
    }

    @Test
    fun shouldUpdateLocationEntityIfExists() = runTest {
        val givenLocationEntity1 = LocationEntity(
            latitude = 52.23,
            longitude = 21.01,
            name = "Warsaw",
            isCurrent = true
        )
        val givenLocationEntity2 = LocationEntity(
            latitude = 52.23,
            longitude = 21.01,
            name = "Warsaw",
            isCurrent = false
        )

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        assertEquals(listOf(givenLocationEntity2), locationDao.observeAll().first())
    }

    @Test
    fun shouldDeleteCurrentLocationEntity() = runTest {
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

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        locationDao.deleteCurrent()

        assertEquals(listOf(givenLocationEntity2), locationDao.observeAll().first())
    }

    @Test
    fun shouldDeleteLocationEntityWithCoordinates() = runTest {
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

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        locationDao.delete(
            latitude = givenLocationEntity2.latitude,
            longitude = givenLocationEntity2.longitude
        )

        assertEquals(listOf(givenLocationEntity1), locationDao.observeAll().first())
    }

    @Test
    fun shouldDeleteCurrentWeatherOnLocationDelete() = runTest {
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
        val givenCurrentWeatherEntity1 = CurrentWeatherEntity(
            latitude = 52.23,
            longitude = 21.01,
            temperature = 19.0,
            windSpeed = 5.0,
            windDirection = 180.0,
            weatherCode = 0,
            time = "2023-03-24T12:00"
        )
        val givenCurrentWeatherEntity2 = CurrentWeatherEntity(
            latitude = 50.45,
            longitude = 30.52,
            temperature = 10.0,
            windSpeed = 25.0,
            windDirection = 90.0,
            weatherCode = 1,
            time = "2023-03-24T12:00"
        )

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)
        currentWeatherDao.insertOrUpdate(givenCurrentWeatherEntity1)
        currentWeatherDao.insertOrUpdate(givenCurrentWeatherEntity2)

        locationDao.delete(
            latitude = givenLocationEntity1.latitude,
            longitude = givenLocationEntity1.longitude
        )

        assertEquals(listOf(givenLocationEntity2), locationDao.observeAll().first())
        assertEquals(listOf(givenCurrentWeatherEntity2), currentWeatherDao.observeAll().first())
    }

    @Test
    fun shouldDeleteAllLocationEntities() = runTest {
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

        locationDao.insertOrUpdate(givenLocationEntity1)
        locationDao.insertOrUpdate(givenLocationEntity2)

        locationDao.deleteAll()

        assertEquals(emptyList<LocationEntity>(), locationDao.observeAll().first())
    }
}
