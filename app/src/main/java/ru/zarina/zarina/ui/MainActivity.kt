package ru.zarina.zarina.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import ru.zarina.zarina.ui.theme.ZarinaTheme
import ru.zarina.zarina.utils.library.jetpack.activity.DefaultDarkScrim

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                SystemBarStyle.DefaultDarkScrim,
            ),
        )
        super.onCreate(savedInstanceState)

        setContent {
            ZarinaTheme {
                ZarinaAppReworked()
            }
        }
    }
}
