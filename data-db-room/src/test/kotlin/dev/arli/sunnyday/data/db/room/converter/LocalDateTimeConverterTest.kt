package dev.arli.sunnyday.data.db.room.converter

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

internal class LocalDateTimeConverterTest : ShouldSpec({

    val converter = LocalDateTimeConverter()

    should("convert local datetime to string") {
        val givenLocalDateTime = LocalDateTime.parse("2023-03-25T12:34:00")

        val expectedLocalDateTimeString = "2023-03-25T12:34:00"

        converter.localDateTimeToString(givenLocalDateTime) shouldBe expectedLocalDateTimeString
    }

    should("convert null local datetime to null") {
        converter.localDateTimeToString(null) shouldBe null
    }

    should("convert string to local datetime") {
        val givenLocalDateTimeString = "2023-03-25T12:34:00"

        val expectedLocalDateTime = LocalDateTime.parse("2023-03-25T12:34:00")

        converter.stringToLocalDateTime(givenLocalDateTimeString) shouldBe expectedLocalDateTime
    }

    should("convert null string to null") {
        converter.stringToLocalDateTime(null) shouldBe null
    }
})
