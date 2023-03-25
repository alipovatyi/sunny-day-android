package dev.arli.sunnyday.model.forecast

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

internal class HourlyForecastVariableTest : ShouldSpec({
    should("return correct key") {
        listOf(
            HourlyForecastVariable.WeatherCode to "weathercode",
            HourlyForecastVariable.Temperature2mMax to "temperature_2m_max",
            HourlyForecastVariable.Temperature2mMin to "temperature_2m_min",
            HourlyForecastVariable.ApparentTemperature2mMax to "apparent_temperature_max",
            HourlyForecastVariable.ApparentTemperature2mMin to "apparent_temperature_min",
            HourlyForecastVariable.Sunrise to "sunrise",
            HourlyForecastVariable.Sunset to "sunset",
            HourlyForecastVariable.UVIndexMax to "uv_index_max"
        ).forAll { (givenHourlyForecastVariable, expectedKey) ->
            givenHourlyForecastVariable.key shouldBe expectedKey
        }
    }
})
