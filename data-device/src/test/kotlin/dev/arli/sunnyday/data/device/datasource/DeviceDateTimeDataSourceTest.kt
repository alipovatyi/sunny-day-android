package dev.arli.sunnyday.data.device.datasource

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import java.time.LocalDate
import java.time.LocalDateTime

internal class DeviceDateTimeDataSourceTest : ShouldSpec({

    val dataSource = DeviceDateTimeDataSource()

    beforeSpec { mockkStatic(LocalDate::class, LocalDateTime::class) }

    should("return current local date") {
        val givenLocalDate = LocalDate.parse("2023-03-25")

        every { LocalDate.now() } returns givenLocalDate

        dataSource.currentLocalDate() shouldBe givenLocalDate
    }

    should("return current local datetime") {
        val givenLocalDateTime = LocalDateTime.parse("2023-03-25T12:34:00")

        every { LocalDateTime.now() } returns givenLocalDateTime

        dataSource.currentLocalDateTime() shouldBe givenLocalDateTime
    }

    afterSpec { unmockkStatic(LocalDate::class, LocalDateTime::class) }
})
