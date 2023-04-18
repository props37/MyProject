package ru.zarina.zarina.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import ru.zarina.zarina.ui.common.system.TransparentSystemBars
import ru.zarina.zarina.ui.theme.ZarinaTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ZarinaTheme {
                TransparentSystemBars()
                ZarinaApp(
                    splashScreen = splashScreen,
                )
            }
        }
    }
}
