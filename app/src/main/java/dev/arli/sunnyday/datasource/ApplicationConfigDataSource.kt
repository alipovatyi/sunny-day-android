package dev.arli.sunnyday.datasource

import dev.arli.sunnyday.data.config.datasource.ConfigDataSource
import dev.arli.sunnyday.BuildConfig
import javax.inject.Inject

internal class ApplicationConfigDataSource @Inject constructor() : ConfigDataSource {

    override val apiUrl: String
        get() = BuildConfig.API_URL
}
