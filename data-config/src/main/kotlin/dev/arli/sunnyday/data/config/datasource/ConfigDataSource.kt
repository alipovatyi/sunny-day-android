package dev.arli.sunnyday.data.config.datasource

import java.util.TimeZone

interface ConfigDataSource {
    val apiUrl: String
    val currentTimeZone: TimeZone
}
