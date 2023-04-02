package dev.arli.sunnyday.data.common.serializer

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime

internal class LocalDateTimeSerializerTest : ShouldSpec({
    context("serializing") {
        should("serialize local datetime to string") {
            val givenLocalDateTime = LocalDateTime.parse("2023-03-25T12:34:00")
            val givenTestClass = TestClass(localDateTime = givenLocalDateTime)

            val expectedString = "{\"localDateTime\":\"2023-03-25T12:34:00\"}"

            Json.encodeToString(givenTestClass) shouldBe expectedString
        }

        should("serialize local datetime to null string") {
            val givenTestClass = TestClass(localDateTime = null)

            val expectedString = "{\"localDateTime\":null}"

            Json.encodeToString(givenTestClass) shouldBe expectedString
        }
    }

    context("deserializing") {
        should("deserialize string to local datetime") {
            val givenString = "{\"localDateTime\":\"2023-02-19T12:34:00\"}"

            val expectedLocalDateTime = LocalDateTime.parse("2023-02-19T12:34:00")
            val expectedTestClass = TestClass(localDateTime = expectedLocalDateTime)

            Json.decodeFromString<TestClass>(givenString) shouldBe expectedTestClass
        }

        should("deserialize null string to null") {
            val givenString = "{\"localDateTime\":null}"

            val expectedTestClass = TestClass(localDateTime = null)

            Json.decodeFromString<TestClass>(givenString) shouldBe expectedTestClass
        }
    }
}) {

    @Serializable
    private data class TestClass(val localDateTime: SerializableLocalDateTime?)
}
