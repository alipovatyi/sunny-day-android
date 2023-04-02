package dev.arli.sunnyday.ui.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.weather.DailyForecast
import dev.arli.sunnyday.model.weather.HourlyForecast
import dev.arli.sunnyday.model.weather.WeatherCode
import dev.arli.sunnyday.resources.R
import dev.arli.sunnyday.ui.common.preview.SunnyDayThemePreview
import dev.arli.sunnyday.ui.common.theme.SunnyDayTheme
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.roundToInt

@Suppress("LongMethod")
@Composable
internal fun AirConditionsSection(
    currentDailyForecast: DailyForecast,
    currentHourlyForecast: HourlyForecast,
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
                text = stringResource(id = R.string.air_conditions).uppercase(),
                style = SunnyDayTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.weight(0.5f)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_temperature),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    Column(modifier = Modifier.padding(top = 4.dp)) {
                        Text(
                            text = stringResource(id = R.string.apparent_temperature),
                            style = SunnyDayTheme.typography.labelLarge
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = buildString {
                                append("${currentDailyForecast.apparentTemperatureMax.roundToInt()}°")
                                append(" / ")
                                append("${currentDailyForecast.apparentTemperatureMin.roundToInt()}°")
                            },
                            style = SunnyDayTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                Row(modifier = Modifier.weight(0.5f)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_wind),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    Column(modifier = Modifier.padding(top = 4.dp)) {
                        Text(
                            text = stringResource(id = R.string.wind),
                            style = SunnyDayTheme.typography.labelLarge
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = stringResource(
                                id = R.string.speed_kph,
                                currentHourlyForecast.windSpeed10m
                            ),
                            style = SunnyDayTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.weight(0.5f)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_humidity),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    Column(modifier = Modifier.padding(top = 4.dp)) {
                        Text(
                            text = stringResource(id = R.string.precipitation_probability),
                            style = SunnyDayTheme.typography.labelLarge
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "${currentHourlyForecast.precipitationProbability ?: 0}%",
                            style = SunnyDayTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                Row(modifier = Modifier.weight(0.5f)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_precipitation),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    Column(modifier = Modifier.padding(top = 4.dp)) {
                        Text(
                            text = stringResource(id = R.string.precipitation),
                            style = SunnyDayTheme.typography.labelLarge
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = stringResource(
                                id = R.string.precipitation_mm,
                                currentHourlyForecast.precipitation
                            ),
                            style = SunnyDayTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.weight(0.5f)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_humidity),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    Column(modifier = Modifier.padding(top = 4.dp)) {
                        Text(
                            text = stringResource(id = R.string.humidity),
                            style = SunnyDayTheme.typography.labelLarge
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "${currentHourlyForecast.relativeHumidity2m}%",
                            style = SunnyDayTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                Row(modifier = Modifier.weight(0.5f)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_dew_point),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    Column(modifier = Modifier.padding(top = 4.dp)) {
                        Text(
                            text = stringResource(id = R.string.dew_point),
                            style = SunnyDayTheme.typography.labelLarge,
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "${currentHourlyForecast.dewPoint2m.roundToInt()}°",
                            style = SunnyDayTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.weight(0.5f)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pressure),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    Column(modifier = Modifier.padding(top = 4.dp)) {
                        Text(
                            text = stringResource(id = R.string.pressure),
                            style = SunnyDayTheme.typography.labelLarge
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = stringResource(
                                id = R.string.pressure_mm,
                                currentHourlyForecast.pressureMsl.roundToInt()
                            ),
                            style = SunnyDayTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                Row(modifier = Modifier.weight(0.5f)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_uv_index),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    Column(modifier = Modifier.padding(top = 4.dp)) {
                        Text(
                            text = stringResource(id = R.string.uv_index),
                            style = SunnyDayTheme.typography.labelLarge,
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = currentHourlyForecast.uvIndex.roundToInt().toString(),
                            style = SunnyDayTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AirConditionsSectionPreview() {
    SunnyDayThemePreview {
        AirConditionsSection(
            currentDailyForecast = DailyForecast(
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
            currentHourlyForecast = HourlyForecast(
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
            )
        )
    }
}
