package dev.arli.sunnyday.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.arli.sunnyday.ui.common.theme.SunnyDayTheme
import dev.arli.sunnyday.ui.navigation.SunnyDayNavHost

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()

            SunnyDayTheme {
                SunnyDayNavHost(
                    navHostController = navController,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
