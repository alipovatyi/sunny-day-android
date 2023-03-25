package dev.arli.sunnyday.model.forecast

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

internal class DailyForecastVariableTest : ShouldSpec({
    should("return correct key") {
        listOf(
            DailyForecastVariable.Temperature2m to "temperature_2m",
            DailyForecastVariable.RelativeHumidity2m to "relativehumidity_2m",
            DailyForecastVariable.DewPoint2m to "dewpoint_2m",
            DailyForecastVariable.ApparentTemperature to "apparent_temperature",
            DailyForecastVariable.PrecipitationProbability to "precipitation_probability",
            DailyForecastVariable.Precipitation to "precipitation",
            DailyForecastVariable.WeatherCode to "weathercode",
            DailyForecastVariable.PressureMsl to "pressure_msl",
            DailyForecastVariable.WindSpeed10m to "windspeed_10m",
            DailyForecastVariable.WindDirection10m to "winddirection_10m",
            DailyForecastVariable.UVIndex to "uv_index",
        ).forAll { (givenDailyForecastVariable, expectedKey) ->
            givenDailyForecastVariable.key shouldBe expectedKey
        }
    }
})
