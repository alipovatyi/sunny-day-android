package dev.arli.sunnyday.ui.common.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import dev.arli.sunnyday.model.weather.WeatherCode
import dev.arli.sunnyday.resources.R
import dev.arli.sunnyday.ui.common.preview.SunnyDayThemePreview

@Suppress("ForbiddenComment")
@Composable
fun WeatherIcon(
    weatherCode: WeatherCode,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = weatherCode.drawableRes),
        contentDescription = null, // TODO: add content description
        modifier = modifier
    )
}

@Preview
@Composable
private fun WeatherIconPreview() {
    SunnyDayThemePreview {
        WeatherIcon(
            weatherCode = WeatherCode.ClearSky
        )
    }
}

private val WeatherCode.drawableRes: Int
    get() = when (this) {
        WeatherCode.ClearSky,
        WeatherCode.MainlyClear -> R.drawable.ic_sunny
        WeatherCode.PartlyCloudy -> R.drawable.ic_partly_cloudy
        WeatherCode.Overcast -> R.drawable.ic_cloudy
        WeatherCode.Fog,
        WeatherCode.DepositingRimeFog -> R.drawable.ic_foggy
        WeatherCode.DrizzleLight,
        WeatherCode.DrizzleModerate,
        WeatherCode.DrizzleDense,
        WeatherCode.FreezingDrizzleLight,
        WeatherCode.FreezingDrizzleDense -> R.drawable.ic_rainy
        WeatherCode.RainSlight,
        WeatherCode.RainModerate,
        WeatherCode.RainHeavy -> R.drawable.ic_rainy
        WeatherCode.FreezingRainLight,
        WeatherCode.FreezingRainHeavy -> R.drawable.ic_hail
        WeatherCode.SnowFallSlight,
        WeatherCode.SnowFallModerate,
        WeatherCode.SnowFallHeavy,
        WeatherCode.SnowGrains -> R.drawable.ic_snowy
        WeatherCode.RainShowersSlight,
        WeatherCode.RainShowersModerate,
        WeatherCode.RainShowersViolent,
        WeatherCode.SnowShowersSlight,
        WeatherCode.SnowShowersHeavy -> R.drawable.ic_rainy
        WeatherCode.ThunderstormSlightOrModerate,
        WeatherCode.ThunderstormWithSlightHail,
        WeatherCode.ThunderstormWithHeavyHail -> R.drawable.ic_thunderstorm
        WeatherCode.Unknown -> R.drawable.ic_unknown
    }
