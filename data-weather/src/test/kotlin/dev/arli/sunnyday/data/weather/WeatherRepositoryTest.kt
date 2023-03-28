package dev.arli.sunnyday.data.weather

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.arli.sunnyday.data.api.WeatherApi
import dev.arli.sunnyday.data.api.dto.weather.CurrentWeatherDto
import dev.arli.sunnyday.data.api.dto.weather.DailyForecastDto
import dev.arli.sunnyday.data.api.dto.weather.HourlyForecastDto
import dev.arli.sunnyday.data.api.dto.weather.WeatherResponseDto
import dev.arli.sunnyday.data.common.serializer.SerializableLocalDate
import dev.arli.sunnyday.data.common.serializer.SerializableLocalDateTime
import dev.arli.sunnyday.data.config.datasource.ConfigDataSource
import dev.arli.sunnyday.data.db.DatabaseTransactionRunner
import dev.arli.sunnyday.data.db.dao.CurrentWeatherDao
import dev.arli.sunnyday.data.db.dao.DailyForecastDao
import dev.arli.sunnyday.data.db.dao.HourlyForecastDao
import dev.arli.sunnyday.data.db.entity.CurrentWeatherEntity
import dev.arli.sunnyday.data.weather.mapper.toEntity
import dev.arli.sunnyday.model.CurrentWeather
import dev.arli.sunnyday.model.error.ApiError
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.weather.DailyForecastVariable
import dev.arli.sunnyday.model.weather.HourlyForecastVariable
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import java.time.LocalDateTime
import java.util.TimeZone
import kotlinx.coroutines.flow.flowOf

internal class WeatherRepositoryTest : BehaviorSpec({

    val mockConfigDataSource: ConfigDataSource = mockk()
    val mockWeatherApi: WeatherApi = mockk()
    val mockCurrentWeatherDao: CurrentWeatherDao = mockk()
    val mockDailyForecastDao: DailyForecastDao = mockk()
    val mockHourlyForecastDao: HourlyForecastDao = mockk()
    val repository = WeatherRepository(
        configDataSource = mockConfigDataSource,
        weatherApi = mockWeatherApi,
        databaseTransactionRunner = object : DatabaseTransactionRunner {
            override suspend fun <T> invoke(block: suspend () -> T): T = block()
        },
        currentWeatherDao = mockCurrentWeatherDao,
        dailyForecastDao = mockDailyForecastDao,
        hourlyForecastDao = mockHourlyForecastDao
    )

    given("observe current weather") {
        `when`("weather entities present") {
            val givenCurrentWeatherEntities = listOf(
                CurrentWeatherEntity(
                    latitude = 52.23,
                    longitude = 21.01,
                    temperature = 12.6,
                    windSpeed = 13.2,
                    windDirection = 244,
                    weatherCode = 80,
                    time = LocalDateTime.parse("2023-03-25T15:00")
                ),
                CurrentWeatherEntity(
                    latitude = 50.45,
                    longitude = 30.52,
                    temperature = 10.0,
                    windSpeed = 25.0,
                    windDirection = 90,
                    weatherCode = 1,
                    time = LocalDateTime.parse("2023-03-25T15:00")
                )
            )

            val expectedCurrentWeatherModels = listOf(
                CurrentWeather(
                    latitude = Latitude(52.23),
                    longitude = Longitude(21.01),
                    temperature = 12.6,
                    windSpeed = 13.2,
                    windDirection = 244,
                    weatherCode = 80,
                    time = LocalDateTime.parse("2023-03-25T15:00")
                ),
                CurrentWeather(
                    latitude = Latitude(50.45),
                    longitude = Longitude(30.52),
                    temperature = 10.0,
                    windSpeed = 25.0,
                    windDirection = 90,
                    weatherCode = 1,
                    time = LocalDateTime.parse("2023-03-25T15:00")
                )
            )

            every { mockCurrentWeatherDao.observeAll() } returns flowOf(givenCurrentWeatherEntities)

            then("return flow of list of current weather models") {
                repository.observeAllCurrentWeather().test {
                    awaitItem() shouldBe expectedCurrentWeatherModels

                    expectNoEvents()
                }

                verify { mockCurrentWeatherDao.observeAll() }
            }
        }
    }

    given("refresh weather") {
        val givenTimeZone = TimeZone.getTimeZone("Europe/Warsaw")
        val givenLatitude = Latitude(52.23)
        val givenLongitude = Longitude(21.01)
        val givenForecastDaysCount = 10

        afterTest {
            verify {
                mockConfigDataSource.currentTimeZone
                mockConfigDataSource.forecastDaysCount
            }
            coVerify {
                mockWeatherApi.getWeather(
                    latitude = givenLatitude.value,
                    longitude = givenLongitude.value,
                    forecastDaysCount = givenForecastDaysCount,
                    includeCurrentWeather = true,
                    hourlyVariables = HourlyForecastVariable.values().map { it.key },
                    dailyVariables = DailyForecastVariable.values().map { it.key },
                    timezone = givenTimeZone.id
                )
            }
        }

        `when`("fetching failed") {
            val givenError = ApiError.HttpError(code = 500, reason = null)

            every { mockConfigDataSource.currentTimeZone } returns givenTimeZone
            every { mockConfigDataSource.forecastDaysCount } returns givenForecastDaysCount
            coEvery {
                mockWeatherApi.getWeather(
                    latitude = givenLatitude.value,
                    longitude = givenLongitude.value,
                    forecastDaysCount = givenForecastDaysCount,
                    includeCurrentWeather = true,
                    hourlyVariables = HourlyForecastVariable.values().map { it.key },
                    dailyVariables = DailyForecastVariable.values().map { it.key },
                    timezone = givenTimeZone.id
                )
            } returns givenError.left()

            then("return either left with error") {
                repository.refreshWeather(givenLatitude, givenLongitude) shouldBeLeft givenError
            }
        }

        `when`("fetching succeeded") {
            val givenWeatherResponseDto = WeatherResponseDto(
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

            every { mockConfigDataSource.currentTimeZone } returns givenTimeZone
            every { mockConfigDataSource.forecastDaysCount } returns givenForecastDaysCount
            coEvery {
                mockWeatherApi.getWeather(
                    latitude = givenLatitude.value,
                    longitude = givenLongitude.value,
                    forecastDaysCount = givenForecastDaysCount,
                    includeCurrentWeather = true,
                    hourlyVariables = HourlyForecastVariable.values().map { it.key },
                    dailyVariables = DailyForecastVariable.values().map { it.key },
                    timezone = givenTimeZone.id
                )
            } returns givenWeatherResponseDto.right()

            and("updating current weather failed") {
                val givenError = Throwable()

                val expectedCurrentWeatherEntity = givenWeatherResponseDto.currentWeather.toEntity(
                    latitude = givenLatitude,
                    longitude = givenLongitude
                )

                coEvery { mockCurrentWeatherDao.insertOrUpdate(expectedCurrentWeatherEntity) } throws givenError

                then("return either left with error") {
                    repository.refreshWeather(givenLatitude, givenLongitude) shouldBeLeft givenError

                    coVerify { mockCurrentWeatherDao.insertOrUpdate(expectedCurrentWeatherEntity) }
                }
            }

            and("updating daily forecast failed") {
                val givenError = Throwable()

                val expectedCurrentWeatherEntity = givenWeatherResponseDto.currentWeather.toEntity(
                    latitude = givenLatitude,
                    longitude = givenLongitude
                )
                val expectedDailyForecastEntities = givenWeatherResponseDto.daily.toEntity(
                    latitude = givenLatitude,
                    longitude = givenLongitude
                ).toTypedArray()

                coEvery { mockCurrentWeatherDao.insertOrUpdate(expectedCurrentWeatherEntity) } just runs
                coEvery { mockDailyForecastDao.insertOrUpdateAll(*expectedDailyForecastEntities) } throws givenError

                then("return either left with error") {
                    repository.refreshWeather(givenLatitude, givenLongitude) shouldBeLeft givenError

                    coVerify {
                        mockCurrentWeatherDao.insertOrUpdate(expectedCurrentWeatherEntity)
                        mockDailyForecastDao.insertOrUpdateAll(*expectedDailyForecastEntities)
                    }
                }
            }

            and("updating hourly forecast failed") {
                val givenError = Throwable()

                val expectedCurrentWeatherEntity = givenWeatherResponseDto.currentWeather.toEntity(
                    latitude = givenLatitude,
                    longitude = givenLongitude
                )
                val expectedDailyForecastEntities = givenWeatherResponseDto.daily.toEntity(
                    latitude = givenLatitude,
                    longitude = givenLongitude
                ).toTypedArray()
                val expectedHourlyForecastEntities = givenWeatherResponseDto.hourly.toEntity(
                    latitude = givenLatitude,
                    longitude = givenLongitude
                ).toTypedArray()

                coEvery { mockCurrentWeatherDao.insertOrUpdate(expectedCurrentWeatherEntity) } just runs
                coEvery { mockDailyForecastDao.insertOrUpdateAll(*expectedDailyForecastEntities) } just runs
                coEvery { mockHourlyForecastDao.insertOrUpdateAll(*expectedHourlyForecastEntities) } throws givenError

                then("return either left with error") {
                    repository.refreshWeather(givenLatitude, givenLongitude) shouldBeLeft givenError

                    coVerify {
                        mockCurrentWeatherDao.insertOrUpdate(expectedCurrentWeatherEntity)
                        mockDailyForecastDao.insertOrUpdateAll(*expectedDailyForecastEntities)
                        mockHourlyForecastDao.insertOrUpdateAll(*expectedHourlyForecastEntities)
                    }
                }
            }

            and("updating succeeded") {
                val expectedCurrentWeatherEntity = givenWeatherResponseDto.currentWeather.toEntity(
                    latitude = givenLatitude,
                    longitude = givenLongitude
                )
                val expectedDailyForecastEntities = givenWeatherResponseDto.daily.toEntity(
                    latitude = givenLatitude,
                    longitude = givenLongitude
                ).toTypedArray()
                val expectedHourlyForecastEntities = givenWeatherResponseDto.hourly.toEntity(
                    latitude = givenLatitude,
                    longitude = givenLongitude
                ).toTypedArray()

                coEvery { mockCurrentWeatherDao.insertOrUpdate(expectedCurrentWeatherEntity) } just runs
                coEvery { mockDailyForecastDao.insertOrUpdateAll(*expectedDailyForecastEntities) } just runs
                coEvery { mockHourlyForecastDao.insertOrUpdateAll(*expectedHourlyForecastEntities) } just runs

                then("return either right with Unit") {
                    repository.refreshWeather(givenLatitude, givenLongitude) shouldBeRight Unit

                    coVerify {
                        mockCurrentWeatherDao.insertOrUpdate(expectedCurrentWeatherEntity)
                        mockDailyForecastDao.insertOrUpdateAll(*expectedDailyForecastEntities)
                        mockHourlyForecastDao.insertOrUpdateAll(*expectedHourlyForecastEntities)
                    }
                }
            }
        }
    }

    afterEach {
        confirmVerified(
            mockConfigDataSource,
            mockWeatherApi,
            mockCurrentWeatherDao,
            mockDailyForecastDao,
            mockHourlyForecastDao
        )
    }
})
