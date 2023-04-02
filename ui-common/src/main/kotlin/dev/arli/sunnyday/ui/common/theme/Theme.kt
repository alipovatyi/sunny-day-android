package dev.arli.sunnyday.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

private val DarkColorScheme = darkColorScheme()
private val LightColorScheme = lightColorScheme()

@Composable
fun SunnyDayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

object SunnyDayTheme {

    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) {
            DarkColorScheme
        } else {
            LightColorScheme
        }

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = Typography

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = Shapes
}
