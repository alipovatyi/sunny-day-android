package dev.arli.sunnyday.data.api.retrofit.adapter

import arrow.core.Either
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.arli.sunnyday.model.error.ApiError
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET

@OptIn(ExperimentalSerializationApi::class)
internal class EitherCallAdapterTest : BehaviorSpec({
    lateinit var mockWebServer: MockWebServer
    lateinit var testApi: TestApi

    beforeEach {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val json = Json { ignoreUnknownKeys = true }
        val retrofit = Retrofit.Builder().apply {
            baseUrl(mockWebServer.url("/").toString())
            addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            addCallAdapterFactory(EitherCallAdapterFactory())
        }.build()

        testApi = retrofit.create()
    }

    afterEach {
        mockWebServer.shutdown()
    }

    given("successful response") {
        `when`("body is empty") {
            then("return either left with unknown error") {
                val givenResponse = MockResponse().setResponseCode(200).setBody("")

                mockWebServer.enqueue(givenResponse)

                val actualError = testApi.callWithResponseBody().shouldBeLeft()

                actualError.shouldBeInstanceOf<ApiError.UnknownApiError>()
                actualError.throwable.shouldBeInstanceOf<SerializationException>()
            }
        }

        `when`("body is incorrect") {
            then("return either left with unknown error") {
                val givenResponseJson = """{ "f": "v" }"""
                val givenResponse = MockResponse().setResponseCode(200).setBody(givenResponseJson)

                mockWebServer.enqueue(givenResponse)

                val actualError = testApi.callWithResponseBody().shouldBeLeft()

                actualError.shouldBeInstanceOf<ApiError.UnknownApiError>()
                actualError.throwable.shouldBeInstanceOf<MissingFieldException>()
            }
        }

        `when`("body is correct") {
            then("return either right with data") {
                val givenResponseJson = """{ "field": "value" }"""
                val givenResponse = MockResponse().setResponseCode(200).setBody(givenResponseJson)

                val expectedTestDto = TestDto(field = "value")

                mockWebServer.enqueue(givenResponse)

                testApi.callWithResponseBody() shouldBeRight expectedTestDto
            }
        }

        `when`("return type is Unit") {
            then("return either right with Unit") {
                val givenResponse = MockResponse().setResponseCode(200)

                mockWebServer.enqueue(givenResponse)

                testApi.callWithoutResponseBody() shouldBeRight Unit
            }
        }
    }

    given("failure response") {
        `when`("body is null") {
            then("return either left with http error and null reason") {
                val givenResponse = MockResponse().setResponseCode(404)

                val expectedError = ApiError.HttpError(code = 404, reason = null)

                mockWebServer.enqueue(givenResponse)

                testApi.callWithResponseBody() shouldBeLeft expectedError
            }
        }

        `when`("body is incorrect") {
            then("return either left with http error and null reason") {
                val givenResponseJson = """{ "error": true }"""
                val givenResponse = MockResponse().setResponseCode(404).setBody(givenResponseJson)

                val expectedError = ApiError.HttpError(code = 404, reason = null)

                mockWebServer.enqueue(givenResponse)

                testApi.callWithResponseBody() shouldBeLeft expectedError
            }
        }

        `when`("body is correct") {
            then("return either left with http error and reason") {
                val givenResponseJson = """{ "reason": "Something went wrong" }"""
                val givenResponse = MockResponse().setResponseCode(404).setBody(givenResponseJson)

                val expectedError = ApiError.HttpError(code = 404, reason = "Something went wrong")

                mockWebServer.enqueue(givenResponse)

                testApi.callWithResponseBody() shouldBeLeft expectedError
            }
        }
    }
}) {

    private interface TestApi {
        @GET("/")
        suspend fun callWithResponseBody(): Either<ApiError, TestDto>

        @GET("/")
        suspend fun callWithoutResponseBody(): Either<ApiError, Unit>
    }

    @Serializable
    private data class TestDto(@SerialName("field") val field: String)
}
