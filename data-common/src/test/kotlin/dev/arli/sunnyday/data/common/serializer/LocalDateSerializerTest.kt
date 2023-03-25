package dev.arli.sunnyday.data.common.serializer

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class LocalDateSerializerTest : ShouldSpec({
    context("serializing") {
        should("serialize local date to string") {
            val givenLocalDate = LocalDate.parse("2023-03-25")
            val givenTestClass = TestClass(localDate = givenLocalDate)

            val expectedString = "{\"localDate\":\"2023-03-25\"}"

            Json.encodeToString(givenTestClass) shouldBe expectedString
        }

        should("serialize local date to null string") {
            val givenTestClass = TestClass(localDate = null)

            val expectedString = "{\"localDate\":null}"

            Json.encodeToString(givenTestClass) shouldBe expectedString
        }
    }

    context("deserializing") {
        should("deserialize string to local date") {
            val givenString = "{\"localDate\":\"2023-02-19\"}"

            val expectedLocalDate = LocalDate.parse("2023-02-19")
            val expectedTestClass = TestClass(localDate = expectedLocalDate)

            Json.decodeFromString<TestClass>(givenString) shouldBe expectedTestClass
        }

        should("deserialize null string to null") {
            val givenString = "{\"localDate\":null}"

            val expectedTestClass = TestClass(localDate = null)

            Json.decodeFromString<TestClass>(givenString) shouldBe expectedTestClass
        }
    }
}) {

    @Serializable
    private data class TestClass(val localDate: SerializableLocalDate?)
}
