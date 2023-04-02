package dev.arli.sunnyday.data.common

import dev.arli.sunnyday.data.common.datasource.DateTimeDataSource
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class DateTimeRepository @Inject constructor(
    private val dateTimeDataSource: DateTimeDataSource
) {

    fun currentLocalDate(): LocalDate {
        return dateTimeDataSource.currentLocalDate()
    }

    fun currentLocalDateTime(): LocalDateTime {
        return dateTimeDataSource.currentLocalDateTime()
    }
}
