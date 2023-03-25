package dev.arli.sunnyday.data.db.room.converter

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

internal class LocalDateConverterTest : ShouldSpec({

    val converter = LocalDateConverter()

    should("convert local date to string") {
        val givenLocalDate = LocalDate.parse("2023-03-25")

        val expectedLocalDateString = "2023-03-25"

        converter.localDateToString(givenLocalDate) shouldBe expectedLocalDateString
    }

    should("convert null local date to null") {
        converter.localDateToString(null) shouldBe null
    }

    should("convert string to local date") {
        val givenLocalDateString = "2023-02-19"

        val expectedLocalDate = LocalDate.parse("2023-03-25")

        converter.stringToLocalDate(givenLocalDateString) shouldBe expectedLocalDate
    }

    should("convert null string to null") {
        converter.stringToLocalDate(null) shouldBe null
    }
})
