package dev.arli.sunnyday.ui.common.preview

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import dev.arli.sunnyday.ui.common.theme.SunnyDayTheme

@Composable
fun SunnyDayThemePreview(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    SunnyDayTheme(
        darkTheme = darkTheme,
        content = content
    )
}
