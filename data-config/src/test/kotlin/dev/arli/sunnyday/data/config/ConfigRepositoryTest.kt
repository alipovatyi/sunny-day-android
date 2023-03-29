package dev.arli.sunnyday.data.config

import dev.arli.sunnyday.data.config.datasource.ConfigDataSource
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.net.URL

internal class ConfigRepositoryTest : ShouldSpec({

    val mockConfigDataSource: ConfigDataSource = mockk()
    val repository = ConfigRepository(
        configDataSource = mockConfigDataSource
    )

    should("return data source url") {
        val givenDataSourceUrl = URL("https://open-meteo.com/")

        every { mockConfigDataSource.dataSourceUrl } returns givenDataSourceUrl

        repository.getDataSourceUrl() shouldBe givenDataSourceUrl

        verify { mockConfigDataSource.dataSourceUrl }
    }

    afterEach {
        confirmVerified(mockConfigDataSource)
    }
})
