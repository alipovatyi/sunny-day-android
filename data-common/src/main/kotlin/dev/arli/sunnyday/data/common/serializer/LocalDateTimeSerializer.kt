package dev.arli.sunnyday.data.common.serializer

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

typealias SerializableLocalDateTime = @Serializable(LocalDateTimeSerializer::class) LocalDateTime

object LocalDateTimeSerializer : KSerializer<LocalDateTime?> {

    override val descriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime?) {
        if (value != null) {
            encoder.encodeString(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(value))
        }
    }

    override fun deserialize(decoder: Decoder): LocalDateTime? {
        return LocalDateTime.parse(decoder.decodeString(), DateTimeFormatter.ISO_DATE_TIME)
    }
}
