package ru.zarina.zarina.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

@Composable
fun ZarinaThemeReworked(
    isDarkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val materialColors = remember(isDarkTheme) {
        if (isDarkTheme) darkColors() else lightColors()
    }

    CompositionLocalProvider(
        LocalUiKitColorsReworked provides LightUiKitColors,
    ) {
        MaterialTheme(
            colors = materialColors,
            content = content,
        )
    }
}
