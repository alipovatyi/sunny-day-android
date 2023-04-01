package dev.arli.sunnyday.ui.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.weather.HourlyForecast
import dev.arli.sunnyday.model.weather.WeatherCode
import dev.arli.sunnyday.resources.R
import dev.arli.sunnyday.ui.common.components.WeatherIcon
import dev.arli.sunnyday.ui.common.preview.SunnyDayThemePreview
import dev.arli.sunnyday.ui.common.theme.SunnyDayTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

private val TimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
internal fun HourlyForecastSection(
    hourlyForecasts: List<HourlyForecast>,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.hourly_forecast).uppercase(),
                style = SunnyDayTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(16.dp))

            LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
                itemsIndexed(hourlyForecasts) { index, hourlyForecast ->
                    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            val formattedTime = remember(hourlyForecast.time) {
                                TimeFormatter.format(hourlyForecast.time)
                            }

                            Text(text = formattedTime, style = SunnyDayTheme.typography.labelSmall)

                            Spacer(Modifier.height(8.dp))

                            WeatherIcon(
                                weatherCode = hourlyForecast.weatherCode,
                                modifier = Modifier.size(24.dp)
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = "${hourlyForecast.temperature2m.roundToInt()}°",
                                style = SunnyDayTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (index < hourlyForecasts.lastIndex) {
                            Divider(
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HourlyForecastSectionPreview() {
    SunnyDayThemePreview {
        HourlyForecastSection(
            hourlyForecasts = listOf(
                HourlyForecast(
                    latitude = Latitude(52.23),
                    longitude = Longitude(21.01),
                    time = LocalDateTime.parse("2023-03-25T00:00"),
                    temperature2m = 11.8,
                    relativeHumidity2m = 92,
                    dewPoint2m = 10.5,
                    apparentTemperature = 10.9,
                    precipitationProbability = 30,
                    precipitation = 0.10,
                    weatherCode = WeatherCode.Overcast,
                    pressureMsl = 1003.6,
                    windSpeed10m = 6.8,
                    windDirection10m = 267,
                    uvIndex = 0.05
                ),
                HourlyForecast(
                    latitude = Latitude(52.23),
                    longitude = Longitude(21.01),
                    time = LocalDateTime.parse("2023-03-25T01:00"),
                    temperature2m = 11.0,
                    relativeHumidity2m = 91,
                    dewPoint2m = 9.5,
                    apparentTemperature = 9.6,
                    precipitationProbability = 50,
                    precipitation = 0.20,
                    weatherCode = WeatherCode.PartlyCloudy,
                    pressureMsl = 1004.2,
                    windSpeed10m = 8.7,
                    windDirection10m = 265,
                    uvIndex = 1.00
                ),
                HourlyForecast(
                    latitude = Latitude(52.23),
                    longitude = Longitude(21.01),
                    time = LocalDateTime.parse("2023-03-25T02:00"),
                    temperature2m = 20.8,
                    relativeHumidity2m = 50,
                    dewPoint2m = 10.5,
                    apparentTemperature = 10.9,
                    precipitationProbability = 30,
                    precipitation = 0.10,
                    weatherCode = WeatherCode.RainShowersModerate,
                    pressureMsl = 1003.6,
                    windSpeed10m = 6.8,
                    windDirection10m = 267,
                    uvIndex = 0.20
                )
            )
        )
    }
}
