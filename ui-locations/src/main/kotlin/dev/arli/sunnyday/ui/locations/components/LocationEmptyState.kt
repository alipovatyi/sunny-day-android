package dev.arli.sunnyday.ui.locations.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.arli.sunnyday.ui.common.preview.SunnyDayThemePreview
import dev.arli.sunnyday.resources.R
import dev.arli.sunnyday.ui.common.theme.SunnyDayTheme

@Composable
internal fun LocationEmptyState(
    onAddLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.no_locations_added),
            style = SunnyDayTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.add_first_location),
            style = SunnyDayTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onAddLocationClick) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = stringResource(id = R.string.add_location))
        }
    }
}

@Preview
@Composable
private fun LocationEmptyStatePreview() {
    SunnyDayThemePreview {
        LocationEmptyState(
            onAddLocationClick = {}
        )
    }
}
