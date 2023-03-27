package dev.arli.sunnyday.ui.locations

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.arli.sunnyday.resources.R
import dev.arli.sunnyday.ui.common.preview.SunnyDayThemePreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.my_locations))
                }
            )
        }
    ) { contentPadding ->
        // TODO
    }
}

@Preview
@Composable
private fun LocationsScreenPreview() {
    SunnyDayThemePreview {
        LocationsScreen()
    }
}
