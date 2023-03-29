package dev.arli.sunnyday.data.api.dto.weather

import dev.arli.sunnyday.data.common.serializer.SerializableLocalDate
import dev.arli.sunnyday.data.common.serializer.SerializableLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyForecastDto(
    @SerialName("time") val time: List<SerializableLocalDate>,
    @SerialName("weathercode") val weatherCode: List<Int>,
    @SerialName("temperature_2m_max") val temperature2mMax: List<Double>,
    @SerialName("temperature_2m_min") val temperature2mMin: List<Double>,
    @SerialName("apparent_temperature_max") val apparentTemperatureMax: List<Double>,
    @SerialName("apparent_temperature_min") val apparentTemperatureMin: List<Double>,
    @SerialName("sunrise") val sunrise: List<SerializableLocalDateTime>,
    @SerialName("sunset") val sunset: List<SerializableLocalDateTime>,
    @SerialName("uv_index_max") val uvIndexMax: List<Double>
)
