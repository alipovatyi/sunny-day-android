package dev.arli.sunnyday.model.weather

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

internal class WeatherCodeTest : BehaviorSpec({
    given("weather code") {
        `when`("get key") {
            then("return weather code key") {
                listOf(
                    WeatherCode.ClearSky to 0,
                    WeatherCode.MainlyClear to 1,
                    WeatherCode.PartlyCloudy to 2,
                    WeatherCode.Overcast to 3,
                    WeatherCode.Fog to 45,
                    WeatherCode.DepositingRimeFog to 48,
                    WeatherCode.DrizzleLight to 51,
                    WeatherCode.DrizzleModerate to 53,
                    WeatherCode.DrizzleDense to 55,
                    WeatherCode.FreezingDrizzleLight to 56,
                    WeatherCode.FreezingDrizzleDense to 57,
                    WeatherCode.RainSlight to 61,
                    WeatherCode.RainModerate to 63,
                    WeatherCode.RainHeavy to 65,
                    WeatherCode.FreezingRainLight to 66,
                    WeatherCode.FreezingRainHeavy to 67,
                    WeatherCode.SnowFallSlight to 71,
                    WeatherCode.SnowFallModerate to 73,
                    WeatherCode.SnowFallHeavy to 75,
                    WeatherCode.SnowGrains to 77,
                    WeatherCode.RainShowersSlight to 80,
                    WeatherCode.RainShowersModerate to 81,
                    WeatherCode.RainShowersViolent to 82,
                    WeatherCode.SnowShowersSlight to 85,
                    WeatherCode.SnowShowersHeavy to 86,
                    WeatherCode.ThunderstormSlightOrModerate to 95,
                    WeatherCode.ThunderstormWithSlightHail to 96,
                    WeatherCode.ThunderstormWithHeavyHail to 99,
                    WeatherCode.Unknown to -1
                ).forAll { (givenWeatherCode, expectedKey) ->
                    givenWeatherCode.key shouldBe expectedKey
                }
            }
        }
    }

    given("weather code key") {
        `when`("called fromKey") {
            and("key is supported") {
                then("return weather code") {
                    listOf(
                        0 to WeatherCode.ClearSky,
                        1 to WeatherCode.MainlyClear,
                        2 to WeatherCode.PartlyCloudy,
                        3 to WeatherCode.Overcast,
                        45 to WeatherCode.Fog,
                        48 to WeatherCode.DepositingRimeFog,
                        51 to WeatherCode.DrizzleLight,
                        53 to WeatherCode.DrizzleModerate,
                        55 to WeatherCode.DrizzleDense,
                        56 to WeatherCode.FreezingDrizzleLight,
                        57 to WeatherCode.FreezingDrizzleDense,
                        61 to WeatherCode.RainSlight,
                        63 to WeatherCode.RainModerate,
                        65 to WeatherCode.RainHeavy,
                        66 to WeatherCode.FreezingRainLight,
                        67 to WeatherCode.FreezingRainHeavy,
                        71 to WeatherCode.SnowFallSlight,
                        73 to WeatherCode.SnowFallModerate,
                        75 to WeatherCode.SnowFallHeavy,
                        77 to WeatherCode.SnowGrains,
                        80 to WeatherCode.RainShowersSlight,
                        81 to WeatherCode.RainShowersModerate,
                        82 to WeatherCode.RainShowersViolent,
                        85 to WeatherCode.SnowShowersSlight,
                        86 to WeatherCode.SnowShowersHeavy,
                        95 to WeatherCode.ThunderstormSlightOrModerate,
                        96 to WeatherCode.ThunderstormWithSlightHail,
                        99 to WeatherCode.ThunderstormWithHeavyHail
                    ).forAll { (givenKey, expectedWeatherCode) ->
                        WeatherCode.fromKey(givenKey) shouldBe expectedWeatherCode
                    }
                }
            }

            and("key is not supported") {
                then("return Unknown weather code") {
                    listOf(-1, 10, 100).forAll { givenKey ->
                        WeatherCode.fromKey(givenKey) shouldBe WeatherCode.Unknown
                    }
                }
            }
        }
    }
})
