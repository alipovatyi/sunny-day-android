package dev.arli.sunnyday.ui.locations.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.arli.sunnyday.model.CurrentWeather
import dev.arli.sunnyday.model.LocationWithCurrentWeather
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.resources.R
import dev.arli.sunnyday.ui.common.preview.SunnyDayThemePreview
import dev.arli.sunnyday.ui.common.theme.SunnyDayTheme
import java.time.LocalDateTime
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LocationListItem(
    location: LocationWithCurrentWeather,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentWeather = location.currentWeather

    Card(
        enabled = currentWeather != null,
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = location.name ?: stringResource(id = R.string.current_location),
                style = SunnyDayTheme.typography.titleLarge
            )
            if (location.isCurrent) {
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.NearMe,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.weight(1f))
            if (currentWeather == null) {
                CircularProgressIndicator(
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(32.dp)
                )
            } else {
                Text(
                    text = "${currentWeather.temperature.roundToInt()}°",
                    style = SunnyDayTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
private fun LocationListItemPreview() {
    SunnyDayThemePreview {
        LocationListItem(
            location = LocationWithCurrentWeather(
                coordinates = Coordinates(Latitude(0.0), Longitude(0.0)),
                name = "Warsaw",
                isCurrent = true,
                currentWeather = CurrentWeather(
                    latitude = Latitude(0.0),
                    longitude = Longitude(0.0),
                    temperature = 19.0,
                    windSpeed = 5.0,
                    windDirection = 180,
                    weatherCode = 0,
                    time = LocalDateTime.parse("2023-03-24T12:00")
                )
            ),
            onClick = {}
        )
    }
}
