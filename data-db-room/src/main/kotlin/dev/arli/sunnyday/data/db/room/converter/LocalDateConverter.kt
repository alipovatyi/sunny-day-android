package dev.arli.sunnyday.data.db.room.converter

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateConverter {

    @TypeConverter
    fun localDateToString(localDate: LocalDate?): String? {
        return localDate?.let { DateTimeFormatter.ISO_LOCAL_DATE.format(it) }
    }

    @TypeConverter
    fun stringToLocalDate(localDateString: String?): LocalDate? {
        return localDateString?.let(LocalDate::parse)
    }
}
