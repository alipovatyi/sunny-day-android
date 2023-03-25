package dev.arli.sunnyday.data.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.arli.sunnyday.data.db.SunnyDayDatabase
import dev.arli.sunnyday.data.db.entity.CurrentWeatherEntity
import dev.arli.sunnyday.data.db.entity.LocationEntity
import dev.arli.sunnyday.data.db.room.dao.RoomCurrentWeatherDao
import dev.arli.sunnyday.data.db.room.dao.RoomLocationDao
import java.time.LocalDate

@Database(
    entities = [
        LocationEntity::class,
        CurrentWeatherEntity::class
    ],
    version = 1
)
@TypeConverters(LocalDate::class)
abstract class RoomSunnyDayDatabase : RoomDatabase(), SunnyDayDatabase {
    abstract override fun locationDao(): RoomLocationDao
    abstract override fun currentWeatherDao(): RoomCurrentWeatherDao
}
