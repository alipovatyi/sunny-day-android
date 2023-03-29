package dev.arli.sunnyday.data.db.room.converter

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter {

    @TypeConverter
    fun localDateTimeToString(localDateTime: LocalDateTime?): String? {
        return localDateTime?.let { DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(it) }
    }

    @TypeConverter
    fun stringToLocalDateTime(localDateTimeString: String?): LocalDateTime? {
        return localDateTimeString?.let(LocalDateTime::parse)
    }
}
