package dev.arli.sunnyday.datasource

import dev.arli.sunnyday.BuildConfig
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import java.util.TimeZone

internal class ApplicationConfigDataSourceTest : ShouldSpec({

    val dataSource = ApplicationConfigDataSource()

    should("return API url") {
        dataSource.apiUrl shouldBe BuildConfig.API_URL
    }

    should("return current time zone") {
        dataSource.currentTimeZone shouldBe TimeZone.getDefault()
    }

    should("return forecast days count") {
        dataSource.forecastDaysCount shouldBe 10
    }

    should("return Google Maps API key") {
        dataSource.googleMapsApiKey shouldBe BuildConfig.GOOGLE_MAPS_API_KEY
    }
})
