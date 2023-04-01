package dev.arli.sunnyday.ui.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.arli.sunnyday.model.CurrentWeather
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.weather.WeatherCode
import dev.arli.sunnyday.ui.common.components.WeatherIcon
import dev.arli.sunnyday.ui.common.preview.SunnyDayThemePreview
import dev.arli.sunnyday.ui.common.theme.SunnyDayTheme
import java.time.LocalDateTime
import kotlin.math.roundToInt

@Composable
internal fun CurrentWeatherSection(
    currentWeather: CurrentWeather,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        WeatherIcon(
            weatherCode = currentWeather.weatherCode,
            modifier = Modifier.size(64.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "${currentWeather.temperature.roundToInt()}°",
            style = SunnyDayTheme.typography.displayLarge
        )
    }
}

@Preview
@Composable
private fun CurrentWeatherSectionPreview() {
    SunnyDayThemePreview {
        CurrentWeatherSection(
            currentWeather = CurrentWeather(
                latitude = Latitude(52.23),
                longitude = Longitude(21.01),
                temperature = 12.6,
                windSpeed = 13.2,
                windDirection = 244,
                weatherCode = WeatherCode.RainShowersSlight,
                time = LocalDateTime.parse("2023-03-25T15:00")
            )
        )
    }
}
