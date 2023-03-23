package dev.arli.sunnyday.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = CurrentWeatherEntity.TableName,
    primaryKeys = [
        CurrentWeatherEntity.Columns.Latitude,
        CurrentWeatherEntity.Columns.Longitude
    ],
    foreignKeys = [
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = [
                LocationEntity.Columns.Latitude,
                LocationEntity.Columns.Longitude
            ],
            childColumns = [
                CurrentWeatherEntity.Columns.Latitude,
                CurrentWeatherEntity.Columns.Longitude
            ],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CurrentWeatherEntity(
    @ColumnInfo(name = Columns.Latitude) val latitude: Double,
    @ColumnInfo(name = Columns.Longitude) val longitude: Double,
    @ColumnInfo(name = Columns.Temperature) val temperature: Double,
    @ColumnInfo(name = Columns.WindSpeed) val windSpeed: Double,
    @ColumnInfo(name = Columns.WindDirection) val windDirection: Double,
    @ColumnInfo(name = Columns.WeatherCode) val weatherCode: Int,
    @ColumnInfo(name = Columns.Time) val time: String
) {

    companion object {
        const val TableName = "current_weather"
    }

    object Columns {
        const val Latitude = "latitude"
        const val Longitude = "longitude"
        const val Temperature = "temperature"
        const val WindSpeed = "wind_speed"
        const val WindDirection = "wind_direction"
        const val WeatherCode = "weather_code"
        const val Time = "time"
    }
}
