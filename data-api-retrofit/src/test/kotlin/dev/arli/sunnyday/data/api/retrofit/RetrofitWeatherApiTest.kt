package dev.arli.sunnyday.data.api.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.arli.sunnyday.data.api.dto.weather.CurrentWeatherDto
import dev.arli.sunnyday.data.api.dto.weather.DailyForecastDto
import dev.arli.sunnyday.data.api.dto.weather.HourlyForecastDto
import dev.arli.sunnyday.data.api.dto.weather.WeatherResponseDto
import dev.arli.sunnyday.data.api.retrofit.adapter.EitherCallAdapterFactory
import dev.arli.sunnyday.data.common.serializer.SerializableLocalDate
import dev.arli.sunnyday.data.common.serializer.SerializableLocalDateTime
import dev.arli.sunnyday.model.error.ApiError
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.create

@OptIn(ExperimentalSerializationApi::class)
internal class RetrofitWeatherApiTest : BehaviorSpec({

    lateinit var mockWebServer: MockWebServer
    lateinit var api: RetrofitWeatherApi

    beforeEach {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val json = Json { ignoreUnknownKeys = true }
        val retrofit = Retrofit.Builder().apply {
            baseUrl(mockWebServer.url("/").toString())
            addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            addCallAdapterFactory(EitherCallAdapterFactory())
        }.build()

        api = retrofit.create()
    }

    afterEach {
        mockWebServer.shutdown()
    }

    given("get weather request") {
        `when`("success") {
            then("return either right with weather response dto") {
                val givenResponseJson = buildJsonObject {
                    put("latitude", 52.25)
                    put("longitude", 21.01)
                    putJsonObject("current_weather") {
                        put("temperature", 12.6)
                        put("windspeed", 13.2)
                        put("winddirection", 244.0)
                        put("weathercode", 80)
                        put("time", "2023-03-25T15:00")
                    }
                    putJsonObject("hourly") {
                        putJsonArray("time") {
                            add("2023-03-25T00:00")
                            add("2023-03-25T01:00")
                            add("2023-03-25T02:00")
                        }
                        putJsonArray("temperature_2m") {
                            add(11.8)
                            add(11.0)
                            add(10.0)
                        }
                        putJsonArray("relativehumidity_2m") {
                            add(92)
                            add(91)
                            add(90)
                        }
                        putJsonArray("dewpoint_2m") {
                            add(10.5)
                            add(9.5)
                            add(8.5)
                        }
                        putJsonArray("apparent_temperature") {
                            add(10.9)
                            add(9.6)
                            add(8.5)
                        }
                        putJsonArray("precipitation_probability") {
                            add(30)
                            add(50)
                            add(70)
                        }
                        putJsonArray("precipitation") {
                            add(0.10)
                            add(0.20)
                            add(0.50)
                        }
                        putJsonArray("weathercode") {
                            add(3)
                            add(2)
                            add(80)
                        }
                        putJsonArray("pressure_msl") {
                            add(1003.6)
                            add(1004.2)
                            add(1005.5)
                        }
                        putJsonArray("windspeed_10m") {
                            add(6.8)
                            add(8.7)
                            add(7.9)
                        }
                        putJsonArray("winddirection_10m") {
                            add(267)
                            add(265)
                            add(273)
                        }
                        putJsonArray("uv_index") {
                            add(0.05)
                            add(1.00)
                            add(2.95)
                        }
                    }
                    putJsonObject("daily") {
                        putJsonArray("time") {
                            add("2023-03-25")
                            add("2023-03-26")
                            add("2023-03-27")
                        }
                        putJsonArray("weathercode") {
                            add(95)
                            add(61)
                            add(80)
                        }
                        putJsonArray("temperature_2m_max") {
                            add(13.8)
                            add(9.9)
                            add(5.8)
                        }
                        putJsonArray("temperature_2m_min") {
                            add(7.4)
                            add(6.2)
                            add(-0.7)
                        }
                        putJsonArray("apparent_temperature_max") {
                            add(11.1)
                            add(6.9)
                            add(3.8)
                        }
                        putJsonArray("apparent_temperature_min") {
                            add(4.9)
                            add(3.4)
                            add(-6.1)
                        }
                        putJsonArray("sunrise") {
                            add("2023-03-25T05:25")
                            add("2023-03-26T05:23")
                            add("2023-03-27T05:20")
                        }
                        putJsonArray("sunset") {
                            add("2023-03-25T17:58")
                            add("2023-03-26T18:00")
                            add("2023-03-27T18:02")
                        }
                        putJsonArray("uv_index_max") {
                            add(4.30)
                            add(3.95)
                            add(3.10)
                        }
                    }
                }
                val givenResponse = MockResponse().setBody(givenResponseJson.toString())

                val expectedWeatherResponseDto = WeatherResponseDto(
                    latitude = 52.25,
                    longitude = 21.01,
                    currentWeather = CurrentWeatherDto(
                        temperature = 12.6,
                        windSpeed = 13.2,
                        windDirection = 244.0,
                        weatherCode = 80,
                        time = SerializableLocalDateTime.parse("2023-03-25T15:00")
                    ),
                    hourly = HourlyForecastDto(
                        time = listOf(
                            SerializableLocalDateTime.parse("2023-03-25T00:00"),
                            SerializableLocalDateTime.parse("2023-03-25T01:00"),
                            SerializableLocalDateTime.parse("2023-03-25T02:00")
                        ),
                        temperature2m = listOf(11.8, 11.0, 10.0),
                        relativeHumidity2m = listOf(92, 91, 90),
                        dewPoint2m = listOf(10.5, 9.5, 8.5),
                        apparentTemperature = listOf(10.9, 9.6, 8.5),
                        precipitationProbability = listOf(30, 50, 70),
                        precipitation = listOf(0.10, 0.20, 0.50),
                        weatherCode = listOf(3, 2, 80),
                        pressureMsl = listOf(1003.6, 1004.2, 1005.5),
                        windSpeed10m = listOf(6.8, 8.7, 7.9),
                        windDirection10m = listOf(267, 265, 273),
                        uvIndex = listOf(0.05, 1.00, 2.95)
                    ),
                    daily = DailyForecastDto(
                        time = listOf(
                            SerializableLocalDate.parse("2023-03-25"),
                            SerializableLocalDate.parse("2023-03-26"),
                            SerializableLocalDate.parse("2023-03-27")
                        ),
                        weatherCode = listOf(95, 61, 80),
                        temperature2mMax = listOf(13.8, 9.9, 5.8),
                        temperature2mMin = listOf(7.4, 6.2, -0.7),
                        apparentTemperatureMax = listOf(11.1, 6.9, 3.8),
                        apparentTemperatureMin = listOf(4.9, 3.4, -6.1),
                        sunrise = listOf(
                            SerializableLocalDateTime.parse("2023-03-25T05:25"),
                            SerializableLocalDateTime.parse("2023-03-26T05:23"),
                            SerializableLocalDateTime.parse("2023-03-27T05:20")
                        ),
                        sunset = listOf(
                            SerializableLocalDateTime.parse("2023-03-25T17:58"),
                            SerializableLocalDateTime.parse("2023-03-26T18:00"),
                            SerializableLocalDateTime.parse("2023-03-27T18:02")
                        ),
                        uvIndexMax = listOf(4.30, 3.95, 3.10)
                    )
                )

                mockWebServer.enqueue(givenResponse)

                val actualResponse = api.getWeather(
                    latitude = 52.25,
                    longitude = 21.01,
                    forecastDaysCount = 3,
                    includeCurrentWeather = true,
                    hourlyVariables = listOf("temperature_2m", "relativehumidity_2m"),
                    dailyVariables = listOf("temperature_2m_max", "temperature_2m_min"),
                    timezone = "Europe/Warsaw"
                )
                val actualRequest = mockWebServer.takeRequest()

                actualResponse shouldBeRight expectedWeatherResponseDto

                with(requireNotNull(actualRequest.requestUrl)) {
                    queryParameter("latitude") shouldBe "52.25"
                    queryParameter("longitude") shouldBe "21.01"
                    queryParameter("forecast_days") shouldBe "3"
                    queryParameter("current_weather") shouldBe "true"
                    queryParameterValues("hourly") shouldBe listOf("temperature_2m", "relativehumidity_2m")
                    queryParameterValues("daily") shouldBe listOf("temperature_2m_max", "temperature_2m_min")
                    queryParameter("timezone") shouldBe "Europe/Warsaw"
                }
            }
        }

        `when`("failure") {
            then("return either left with error") {
                val givenResponse = MockResponse().setResponseCode(500)

                mockWebServer.enqueue(givenResponse)

                val actualResponse = api.getWeather(
                    latitude = 52.25,
                    longitude = 21.01,
                    forecastDaysCount = 3,
                    includeCurrentWeather = true,
                    hourlyVariables = listOf("temperature_2m", "relativehumidity_2m"),
                    dailyVariables = listOf("temperature_2m_max", "temperature_2m_min"),
                    timezone = "Europe/Warsaw"
                )

                actualResponse shouldBeLeft ApiError.HttpError(code = 500, reason = null)
            }
        }
    }
})
