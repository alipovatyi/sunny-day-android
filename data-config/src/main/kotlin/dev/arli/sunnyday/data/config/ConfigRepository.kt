package dev.arli.sunnyday.data.config

import dev.arli.sunnyday.data.config.datasource.ConfigDataSource
import java.net.URL
import javax.inject.Inject

class ConfigRepository @Inject constructor(
    private val configDataSource: ConfigDataSource
) {

    fun getDataSourceUrl(): URL {
        return configDataSource.dataSourceUrl
    }
}
