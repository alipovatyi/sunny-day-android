package dev.arli.sunnyday.datasource

import dev.arli.sunnyday.BuildConfig
import dev.arli.sunnyday.data.config.datasource.ConfigDataSource
import java.util.TimeZone
import javax.inject.Inject

internal class ApplicationConfigDataSource @Inject constructor() : ConfigDataSource {

    override val apiUrl: String
        get() = BuildConfig.API_URL

    override val currentTimeZone: TimeZone
        get() = TimeZone.getDefault()

    override val forecastDaysCount: Int
        get() = ForecastDaysCount

    override val googleMapsApiKey: String
        get() = BuildConfig.GOOGLE_MAPS_API_KEY

    private companion object {
        const val ForecastDaysCount = 10
    }
}
