package dev.arli.sunnyday.data.config.datasource

import java.util.TimeZone

interface ConfigDataSource {
    val apiUrl: String
    val currentTimeZone: TimeZone
    val forecastDaysCount: Int
    val googleMapsApiKey: String
}
