package dev.arli.sunnyday.datasource

import dev.arli.sunnyday.data.config.datasource.ConfigDataSource
import dev.arli.sunnyday.BuildConfig

internal class ApplicationConfigDataSource : ConfigDataSource {

    override val apiUrl: String
        get() = BuildConfig.API_URL
}
