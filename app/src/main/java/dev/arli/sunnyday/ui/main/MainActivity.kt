package dev.arli.sunnyday.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.arli.sunnyday.ui.common.theme.SunnyDayTheme
import dev.arli.sunnyday.ui.navigation.SunnyDayNavHost
import dev.arli.sunnyday.ui.navigation.navigator.LocalNavigator
import dev.arli.sunnyday.ui.navigation.navigator.Navigator
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()

            CompositionLocalProvider(
                LocalNavigator provides navigator
            ) {
                SunnyDayTheme {
                    SunnyDayNavHost(
                        navHostController = navController,
                        navigator = navigator,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
