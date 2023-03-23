package dev.arli.sunnyday.data.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.arli.sunnyday.data.db.entity.CurrentWeatherEntity
import dev.arli.sunnyday.data.db.entity.LocationEntity
import dev.arli.sunnyday.data.db.room.dao.RoomCurrentWeatherDao
import dev.arli.sunnyday.data.db.room.dao.RoomLocationDao

@Database(
    entities = [
        LocationEntity::class,
        CurrentWeatherEntity::class
    ],
    version = 1
)
abstract class SunnyDayDatabase : RoomDatabase() {
    abstract fun locationDao(): RoomLocationDao
    abstract fun currentWeatherDao(): RoomCurrentWeatherDao
}
