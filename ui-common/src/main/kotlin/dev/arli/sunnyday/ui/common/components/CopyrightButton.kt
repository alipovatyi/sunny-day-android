package dev.arli.sunnyday.ui.common.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.arli.sunnyday.resources.R
import dev.arli.sunnyday.ui.common.preview.SunnyDayThemePreview

@Composable
fun CopyrightButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = stringResource(id = R.string.open_meteo_copyright))
    }
}

@Preview
@Composable
private fun CopyrightButtonPreview() {
    SunnyDayThemePreview {
        CopyrightButton(
            onClick = {}
        )
    }
}
