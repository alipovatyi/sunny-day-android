package dev.arli.sunnyday.data.common

import dev.arli.sunnyday.data.common.datasource.DateTimeDataSource
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalDateTime

internal class DateTimeRepositoryTest : ShouldSpec({

    val mockDateTimeDataSource: DateTimeDataSource = mockk()
    val repository = DateTimeRepository(
        dateTimeDataSource = mockDateTimeDataSource
    )

    should("return current local date") {
        val givenLocalDate = LocalDate.parse("2023-03-25")

        every { mockDateTimeDataSource.currentLocalDate() } returns givenLocalDate

        repository.currentLocalDate() shouldBe givenLocalDate

        verify { mockDateTimeDataSource.currentLocalDate() }
    }

    should("return current local datetime") {
        val givenLocalDateTime = LocalDateTime.parse("2023-03-25T12:34:00")

        every { mockDateTimeDataSource.currentLocalDateTime() } returns givenLocalDateTime

        repository.currentLocalDateTime() shouldBe givenLocalDateTime

        verify { mockDateTimeDataSource.currentLocalDateTime() }
    }

    afterEach { confirmVerified(mockDateTimeDataSource) }
})
