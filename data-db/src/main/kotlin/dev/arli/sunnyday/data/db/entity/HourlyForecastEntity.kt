package dev.arli.sunnyday.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.LocalDateTime

@Entity(
    tableName = HourlyForecastEntity.TableName,
    primaryKeys = [
        HourlyForecastEntity.Columns.Latitude,
        HourlyForecastEntity.Columns.Longitude,
        HourlyForecastEntity.Columns.Time
    ],
    foreignKeys = [
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = [
                LocationEntity.Columns.Latitude,
                LocationEntity.Columns.Longitude
            ],
            childColumns = [
                HourlyForecastEntity.Columns.Latitude,
                HourlyForecastEntity.Columns.Longitude
            ],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HourlyForecastEntity(
    @ColumnInfo(name = Columns.Latitude) val latitude: Double,
    @ColumnInfo(name = Columns.Longitude) val longitude: Double,
    @ColumnInfo(name = Columns.Time) val time: LocalDateTime,
    @ColumnInfo(name = Columns.Temperature2m) val temperature2m: Double,
    @ColumnInfo(name = Columns.RelativeHumidity2m) val relativeHumidity2m: Int,
    @ColumnInfo(name = Columns.DewPoint2m) val dewPoint2m: Double,
    @ColumnInfo(name = Columns.ApparentTemperature) val apparentTemperature: Double,
    @ColumnInfo(name = Columns.PrecipitationProbability) val precipitationProbability: Int?,
    @ColumnInfo(name = Columns.Precipitation) val precipitation: Double,
    @ColumnInfo(name = Columns.WeatherCode) val weatherCode: Int,
    @ColumnInfo(name = Columns.PressureMsl) val pressureMsl: Double,
    @ColumnInfo(name = Columns.WindSpeed10m) val windSpeed10m: Double,
    @ColumnInfo(name = Columns.WindDirection10m) val windDirection10m: Int,
    @ColumnInfo(name = Columns.UVIndex) val uvIndex: Double
) {

    companion object {
        const val TableName = "hourly_forecast"
    }

    object Columns {
        const val Latitude = "latitude"
        const val Longitude = "longitude"
        const val Time = "time"
        const val Temperature2m = "temperature_2m"
        const val RelativeHumidity2m = "relative_humidity_2m"
        const val DewPoint2m = "dew_point_2m"
        const val ApparentTemperature = "apparent_temperature"
        const val PrecipitationProbability = "precipitation_probability"
        const val Precipitation = "precipitation"
        const val WeatherCode = "weather_code"
        const val PressureMsl = "pressure_msl"
        const val WindSpeed10m = "wind_speed_10m"
        const val WindDirection10m = "wind_direction_10m"
        const val UVIndex = "uv_index"
    }
}
