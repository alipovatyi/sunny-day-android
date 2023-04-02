package dev.arli.sunnyday.data.api.dto.error

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    @SerialName("reason") val reason: String
)
