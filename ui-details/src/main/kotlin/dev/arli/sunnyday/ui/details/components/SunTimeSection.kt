package dev.arli.sunnyday.ui.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.weather.DailyForecast
import dev.arli.sunnyday.model.weather.WeatherCode
import dev.arli.sunnyday.resources.R
import dev.arli.sunnyday.ui.common.preview.SunnyDayThemePreview
import dev.arli.sunnyday.ui.common.theme.SunnyDayTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val TimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
internal fun SunTimeSection(
    currentDailyForecast: DailyForecast,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(0.5f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sunrise),
                    contentDescription = stringResource(id = R.string.sunrise)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = remember(currentDailyForecast.sunrise) {
                        TimeFormatter.format(currentDailyForecast.sunrise)
                    },
                    style = SunnyDayTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(0.5f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sunset),
                    contentDescription = stringResource(id = R.string.sunset)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = remember(currentDailyForecast.sunset) {
                        TimeFormatter.format(currentDailyForecast.sunset)
                    },
                    style = SunnyDayTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
private fun SunTimeSectionPreview() {
    SunnyDayThemePreview {
        SunTimeSection(
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
            )
        )
    }
}
