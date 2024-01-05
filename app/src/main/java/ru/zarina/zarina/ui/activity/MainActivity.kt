package ru.zarina.zarina.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import ru.zarina.zarina.ui.activity.extension.ActivityExtensionManager
import ru.zarina.zarina.ui.app.ZarinaApp
import ru.zarina.zarina.ui.theme.ZarinaTheme
import ru.zarina.zarina.util.library.activity.DefaultDarkScrim
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var activityExtensionManager: ActivityExtensionManager

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
        installActivityExtensions()

        setContent {
            ZarinaTheme {
                ZarinaApp()
            }
        }
    }

    private fun installActivityExtensions() {
        activityExtensionManager.extensions.forEach { extension ->
            lifecycle.addObserver(extension)
        }
    }
}
