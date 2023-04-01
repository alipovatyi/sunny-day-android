package dev.arli.sunnyday.data.device.datasource

import dev.arli.sunnyday.data.common.datasource.DateTimeDataSource
import java.time.LocalDate
import java.time.LocalDateTime

internal class DeviceDateTimeDataSource : DateTimeDataSource {

    override fun currentLocalDate(): LocalDate {
        return LocalDate.now()
    }

    override fun currentLocalDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }
}
