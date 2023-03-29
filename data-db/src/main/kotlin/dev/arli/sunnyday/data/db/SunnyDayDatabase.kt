package dev.arli.sunnyday.data.db

import dev.arli.sunnyday.data.db.dao.CurrentWeatherDao
import dev.arli.sunnyday.data.db.dao.DailyForecastDao
import dev.arli.sunnyday.data.db.dao.HourlyForecastDao
import dev.arli.sunnyday.data.db.dao.LocationDao

interface SunnyDayDatabase {
    fun locationDao(): LocationDao
    fun currentWeatherDao(): CurrentWeatherDao
    fun dailyForecastDao(): DailyForecastDao
    fun hourlyForecastDao(): HourlyForecastDao
}
