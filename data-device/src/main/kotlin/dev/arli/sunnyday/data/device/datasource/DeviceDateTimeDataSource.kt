package dev.arli.sunnyday.data.device.datasource

import dev.arli.sunnyday.data.common.datasource.DateTimeDataSource
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

internal class DeviceDateTimeDataSource @Inject constructor() : DateTimeDataSource {

    override fun currentLocalDate(): LocalDate {
        return LocalDate.now()
    }

    override fun currentLocalDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }
}
