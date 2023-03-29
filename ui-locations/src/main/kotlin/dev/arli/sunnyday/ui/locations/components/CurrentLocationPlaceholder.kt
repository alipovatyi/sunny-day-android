package dev.arli.sunnyday.ui.locations.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.arli.sunnyday.resources.R
import dev.arli.sunnyday.ui.common.preview.SunnyDayThemePreview
import dev.arli.sunnyday.ui.common.theme.SunnyDayTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CurrentLocationPlaceholder(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.current_location_unavailable),
                style = SunnyDayTheme.typography.titleLarge
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.location_permission_required),
                style = SunnyDayTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
private fun CurrentLocationPlaceholderPreview() {
    SunnyDayThemePreview {
        CurrentLocationPlaceholder(
            onClick = {}
        )
    }
}
