package dev.arli.sunnyday.model.weather

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

internal class DailyForecastVariableTest : ShouldSpec({
    should("return correct key") {
        listOf(
            DailyForecastVariable.WeatherCode to "weathercode",
            DailyForecastVariable.Temperature2mMax to "temperature_2m_max",
            DailyForecastVariable.Temperature2mMin to "temperature_2m_min",
            DailyForecastVariable.ApparentTemperature2mMax to "apparent_temperature_max",
            DailyForecastVariable.ApparentTemperature2mMin to "apparent_temperature_min",
            DailyForecastVariable.Sunrise to "sunrise",
            DailyForecastVariable.Sunset to "sunset",
            DailyForecastVariable.UVIndexMax to "uv_index_max"
        ).forAll { (givenDailyForecastVariable, expectedKey) ->
            givenDailyForecastVariable.key shouldBe expectedKey
        }
    }
})
