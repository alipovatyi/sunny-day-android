package dev.arli.sunnyday.ui.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.weather.DailyForecast
import dev.arli.sunnyday.model.weather.WeatherCode
import dev.arli.sunnyday.resources.R
import dev.arli.sunnyday.ui.common.components.WeatherIcon
import dev.arli.sunnyday.ui.common.preview.SunnyDayThemePreview
import dev.arli.sunnyday.ui.common.theme.SunnyDayTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

private val DayOfWeekFormatter = DateTimeFormatter.ofPattern("EEE")

@Suppress("UnstableCollections", "LongMethod")
@Composable
internal fun DailyForecastSection(
    dailyForecasts: List<DailyForecast>,
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
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.daily_forecast).uppercase(),
                style = SunnyDayTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            dailyForecasts.forEachIndexed { index, dailyForecast ->
                if (index > 0) {
                    Spacer(Modifier.height(8.dp))
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.weight(1 / 3f)
                    ) {
                        val formattedTime = remember(dailyForecast.time) {
                            DayOfWeekFormatter.format(dailyForecast.time)
                        }
                        Text(text = formattedTime, style = SunnyDayTheme.typography.bodyLarge)
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.weight(1 / 3f)
                    ) {
                        WeatherIcon(
                            weatherCode = dailyForecast.weatherCode,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(1 / 3f)
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                with(SunnyDayTheme.typography.bodyLarge.toSpanStyle()) {
                                    withStyle(copy(fontWeight = FontWeight.Bold)) {
                                        append(dailyForecast.temperature2mMax.roundToInt().toString())
                                    }
                                    append(" / ")
                                    append(dailyForecast.temperature2mMin.roundToInt().toString())
                                }
                            }
                        )
                    }
                }

                if (index < dailyForecasts.lastIndex) {
                    Spacer(Modifier.height(8.dp))
                    Divider()
                }
            }
        }
    }
}

@Composable
@Preview
private fun DailyForecastSectionPreview() {
    SunnyDayThemePreview {
        DailyForecastSection(
            dailyForecasts = listOf(
                DailyForecast(
                    latitude = Latitude(52.23),
                    longitude = Longitude(21.01),
                    time = LocalDate.parse("2023-03-25"),
                    weatherCode = WeatherCode.ThunderstormSlightOrModerate,
                    temperature2mMax = 13.8,
                    temperature2mMin = 7.4,
                    apparentTemperatureMax = 11.1,
                    apparentTemperatureMin = 4.9,
                    sunrise = LocalDateTime.parse("2023-03-25T05:25"),
                    sunset = LocalDateTime.parse("2023-03-25T17:58"),
                    uvIndexMax = 4.30
                ),
                DailyForecast(
                    latitude = Latitude(52.23),
                    longitude = Longitude(21.01),
                    time = LocalDate.parse("2023-03-26"),
                    weatherCode = WeatherCode.ClearSky,
                    temperature2mMax = 18.0,
                    temperature2mMin = 5.5,
                    apparentTemperatureMax = 15.0,
                    apparentTemperatureMin = 5.0,
                    sunrise = LocalDateTime.parse("2023-03-26T05:23"),
                    sunset = LocalDateTime.parse("2023-03-26T18:00"),
                    uvIndexMax = 3.20
                ),
                DailyForecast(
                    latitude = Latitude(52.23),
                    longitude = Longitude(21.01),
                    time = LocalDate.parse("2023-03-27"),
                    weatherCode = WeatherCode.PartlyCloudy,
                    temperature2mMax = 10.7,
                    temperature2mMin = 2.6,
                    apparentTemperatureMax = 10.5,
                    apparentTemperatureMin = 3.2,
                    sunrise = LocalDateTime.parse("2023-03-27T05:20"),
                    sunset = LocalDateTime.parse("2023-03-27T18:02"),
                    uvIndexMax = 5.15
                )
            )
        )
    }
}
