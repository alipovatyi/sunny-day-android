package dev.arli.sunnyday.data.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.arli.sunnyday.data.db.SunnyDayDatabase
import dev.arli.sunnyday.data.db.entity.CurrentWeatherEntity
import dev.arli.sunnyday.data.db.entity.DailyForecastEntity
import dev.arli.sunnyday.data.db.entity.LocationEntity
import dev.arli.sunnyday.data.db.room.converter.LocalDateConverter
import dev.arli.sunnyday.data.db.room.converter.LocalDateTimeConverter
import dev.arli.sunnyday.data.db.room.dao.RoomCurrentWeatherDao
import dev.arli.sunnyday.data.db.room.dao.RoomDailyForecastDao
import dev.arli.sunnyday.data.db.room.dao.RoomLocationDao

@Database(
    entities = [
        LocationEntity::class,
        CurrentWeatherEntity::class,
        DailyForecastEntity::class
    ],
    version = 1
)
@TypeConverters(LocalDateConverter::class, LocalDateTimeConverter::class)
abstract class RoomSunnyDayDatabase : RoomDatabase(), SunnyDayDatabase {
    abstract override fun locationDao(): RoomLocationDao
    abstract override fun currentWeatherDao(): RoomCurrentWeatherDao
    abstract override fun dailyForecastDao(): RoomDailyForecastDao
}
