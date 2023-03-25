package dev.arli.sunnyday.data.api.dto.weather

import dev.arli.sunnyday.data.common.serializer.SerializableLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyForecastDto(
    @SerialName("time") val time: List<SerializableLocalDateTime>,
    @SerialName("temperature_2m") val temperature2m: List<Double>,
    @SerialName("relativehumidity_2m") val relativeHumidity2m: List<Int>,
    @SerialName("dewpoint_2m") val dewPoint2m: List<Double>,
    @SerialName("apparent_temperature") val apparentTemperature: List<Double>,
    @SerialName("precipitation_probability") val precipitationProbability: List<Int?>, // sometimes API returns null items
    @SerialName("precipitation") val precipitation: List<Double>,
    @SerialName("weathercode") val weatherCode: List<Int>,
    @SerialName("pressure_msl") val pressureMsl: List<Double>,
    @SerialName("windspeed_10m") val windSpeed10m: List<Double>,
    @SerialName("winddirection_10m") val windDirection10m: List<Int>,
    @SerialName("uv_index") val uvIndex: List<Double>
)
