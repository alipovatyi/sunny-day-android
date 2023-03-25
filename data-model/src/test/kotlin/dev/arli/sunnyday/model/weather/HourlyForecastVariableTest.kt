package dev.arli.sunnyday.model.weather

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

internal class HourlyForecastVariableTest : ShouldSpec({
    should("return correct key") {
        listOf(
            HourlyForecastVariable.Temperature2m to "temperature_2m",
            HourlyForecastVariable.RelativeHumidity2m to "relativehumidity_2m",
            HourlyForecastVariable.DewPoint2m to "dewpoint_2m",
            HourlyForecastVariable.ApparentTemperature to "apparent_temperature",
            HourlyForecastVariable.PrecipitationProbability to "precipitation_probability",
            HourlyForecastVariable.Precipitation to "precipitation",
            HourlyForecastVariable.WeatherCode to "weathercode",
            HourlyForecastVariable.PressureMsl to "pressure_msl",
            HourlyForecastVariable.WindSpeed10m to "windspeed_10m",
            HourlyForecastVariable.WindDirection10m to "winddirection_10m",
            HourlyForecastVariable.UVIndex to "uv_index",
        ).forAll { (givenHourlyForecastVariable, expectedKey) ->
            givenHourlyForecastVariable.key shouldBe expectedKey
        }
    }
})
