package dev.arli.sunnyday.data.db.room.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import dev.arli.sunnyday.data.db.entity.LocationEntity
import dev.arli.sunnyday.data.db.room.SunnyDayDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class RoomLocationDaoTest {

    private lateinit var locationDao: RoomLocationDao
    private lateinit var db: SunnyDayDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, SunnyDayDatabase::class.java).build()
        locationDao = db.locationDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun shouldReturnFlowWithAllLocations() = runTest {
        val givenLocationEntity1 = LocationEntity(
            id = 1,
            latitude = 52.23,
            longitude = 21.01,
            name = "Warsaw",
            isCurrent = true
        )
        val givenLocationEntity2 = LocationEntity(
            id = 2,
            latitude = 50.45,
            longitude = 30.52,
            name = "Kyiv",
            isCurrent = false
        )

        locationDao.insert(givenLocationEntity1)
        locationDao.insert(givenLocationEntity2)

        locationDao.observeAll().test {
            assertEquals(listOf(givenLocationEntity1, givenLocationEntity2), awaitItem())

            expectNoEvents()
        }
    }

    @Test
    fun shouldReturnCurrentLocationIfExists() = runTest {
        val givenLocationEntity1 = LocationEntity(
            id = 1,
            latitude = 52.23,
            longitude = 21.01,
            name = "Warsaw",
            isCurrent = true
        )
        val givenLocationEntity2 = LocationEntity(
            id = 2,
            latitude = 50.45,
            longitude = 30.52,
            name = "Kyiv",
            isCurrent = false
        )

        locationDao.insert(givenLocationEntity1)
        locationDao.insert(givenLocationEntity2)

        assertEquals(givenLocationEntity1, locationDao.selectCurrent())
    }

    @Test
    fun shouldReturnNullIfCurrentLocationDoesNotExists() = runTest {
        val givenLocationEntity = LocationEntity(
            id = 2,
            latitude = 50.45,
            longitude = 30.52,
            name = "Kyiv",
            isCurrent = false
        )

        locationDao.insert(givenLocationEntity)

        assertNull(locationDao.selectCurrent())
    }

    @Test
    fun shouldInsertLocationIfDoesNotExist() = runTest {
        val givenLocationEntity = LocationEntity(
            id = 1,
            latitude = 52.23,
            longitude = 21.01,
            name = "Warsaw",
            isCurrent = true
        )

        locationDao.insert(givenLocationEntity)

        locationDao.observeAll().test {
            assertEquals(listOf(givenLocationEntity), awaitItem())

            expectNoEvents()
        }
    }

    @Test
    fun shouldDeleteCurrentLocation() = runTest {
        val givenLocationEntity1 = LocationEntity(
            id = 1,
            latitude = 52.23,
            longitude = 21.01,
            name = "Warsaw",
            isCurrent = true
        )
        val givenLocationEntity2 = LocationEntity(
            id = 2,
            latitude = 50.45,
            longitude = 30.52,
            name = "Kyiv",
            isCurrent = false
        )

        locationDao.insert(givenLocationEntity1)
        locationDao.insert(givenLocationEntity2)

        locationDao.deleteCurrent()

        locationDao.observeAll().test {
            assertEquals(listOf(givenLocationEntity2), awaitItem())

            expectNoEvents()
        }
    }

    @Test
    fun shouldDeleteLocationWithId() = runTest {
        val givenLocationEntity1 = LocationEntity(
            id = 1,
            latitude = 52.23,
            longitude = 21.01,
            name = "Warsaw",
            isCurrent = true
        )
        val givenLocationEntity2 = LocationEntity(
            id = 2,
            latitude = 50.45,
            longitude = 30.52,
            name = "Kyiv",
            isCurrent = false
        )

        locationDao.insert(givenLocationEntity1)
        locationDao.insert(givenLocationEntity2)

        locationDao.delete(2)

        locationDao.observeAll().test {
            assertEquals(listOf(givenLocationEntity1), awaitItem())

            expectNoEvents()
        }
    }

    @Test
    fun shouldDeleteAllLocations() = runTest {
        val givenLocationEntity1 = LocationEntity(
            id = 1,
            latitude = 52.23,
            longitude = 21.01,
            name = "Warsaw",
            isCurrent = true
        )
        val givenLocationEntity2 = LocationEntity(
            id = 2,
            latitude = 50.45,
            longitude = 30.52,
            name = "Kyiv",
            isCurrent = false
        )

        locationDao.insert(givenLocationEntity1)
        locationDao.insert(givenLocationEntity2)

        locationDao.deleteAll()

        locationDao.observeAll().test {
            assertEquals(emptyList<LocationEntity>(), awaitItem())

            expectNoEvents()
        }
    }
}
