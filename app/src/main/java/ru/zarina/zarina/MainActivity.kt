package ru.zarina.zarina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import ru.zarina.zarina.ui.ZarinaApp
import ru.zarina.zarina.ui.common.system.TransparentSystemBars
import ru.zarina.zarina.ui.theme.ZarinaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ZarinaTheme {
                TransparentSystemBars()
                ZarinaApp()
            }
        }
    }
}
