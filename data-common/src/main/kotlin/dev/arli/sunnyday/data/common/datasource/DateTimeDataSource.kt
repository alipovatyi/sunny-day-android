package dev.arli.sunnyday.data.common.datasource

import java.time.LocalDate
import java.time.LocalDateTime

interface DateTimeDataSource {
    fun currentLocalDate(): LocalDate
    fun currentLocalDateTime(): LocalDateTime
}
