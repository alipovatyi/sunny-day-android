package dev.arli.sunnyday.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = DailyForecastEntity.TableName,
    primaryKeys = [
        DailyForecastEntity.Columns.Latitude,
        DailyForecastEntity.Columns.Longitude,
        DailyForecastEntity.Columns.Time
    ],
    foreignKeys = [
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = [
                LocationEntity.Columns.Latitude,
                LocationEntity.Columns.Longitude
            ],
            childColumns = [
                DailyForecastEntity.Columns.Latitude,
                DailyForecastEntity.Columns.Longitude
            ],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DailyForecastEntity(
    @ColumnInfo(name = Columns.Latitude) val latitude: Double,
    @ColumnInfo(name = Columns.Longitude) val longitude: Double,
    @ColumnInfo(name = Columns.Time) val time: LocalDate,
    @ColumnInfo(name = Columns.WeatherCode) val weatherCode: Int,
    @ColumnInfo(name = Columns.Temperature2mMax) val temperature2mMax: Double,
    @ColumnInfo(name = Columns.Temperature2mMin) val temperature2mMin: Double,
    @ColumnInfo(name = Columns.ApparentTemperatureMax) val apparentTemperatureMax: Double,
    @ColumnInfo(name = Columns.ApparentTemperatureMin) val apparentTemperatureMin: Double,
    @ColumnInfo(name = Columns.Sunrise) val sunrise: LocalDateTime,
    @ColumnInfo(name = Columns.Sunset) val sunset: LocalDateTime,
    @ColumnInfo(name = Columns.UVIndexMax) val uvIndexMax: Double
) {

    companion object {
        const val TableName = "daily_forecast"
    }

    object Columns {
        const val Latitude = "latitude"
        const val Longitude = "longitude"
        const val Time = "time"
        const val WeatherCode = "weather_code"
        const val Temperature2mMax = "temperature_2m_max"
        const val Temperature2mMin = "temperature_2m_min"
        const val ApparentTemperatureMax = "apparent_temperature_max"
        const val ApparentTemperatureMin = "apparent_temperature_min"
        const val Sunrise = "sunrise"
        const val Sunset = "sunset"
        const val UVIndexMax = "uv_index_max"
    }
}
