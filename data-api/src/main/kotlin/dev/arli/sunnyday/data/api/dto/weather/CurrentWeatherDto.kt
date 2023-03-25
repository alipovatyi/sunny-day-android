package dev.arli.sunnyday.data.api.dto.weather

import dev.arli.sunnyday.data.common.serializer.SerializableLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherDto(
    @SerialName("temperature") val temperature: Double,
    @SerialName("windspeed") val windSpeed: Double,
    @SerialName("winddirection") val windDirection: Double,
    @SerialName("weathercode") val weatherCode: Int,
    @SerialName("time") val time: SerializableLocalDateTime
)
