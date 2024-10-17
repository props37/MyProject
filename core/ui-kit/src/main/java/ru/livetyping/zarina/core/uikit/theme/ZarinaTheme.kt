package ru.livetyping.zarina.core.uikit.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import ru.livetyping.zarina.core.uikit.impl.theme.Colors
import ru.livetyping.zarina.core.uikit.impl.theme.LightUiKitColors

@Composable
public fun ZarinaTheme(
    isDarkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val materialColors = remember(isDarkTheme) {
        if (isDarkTheme) {
            darkColors(primary = Colors.MineShaftDark)
        } else {
            lightColors(primary = Colors.MineShaftDark)
        }
    }

    CompositionLocalProvider(
        LocalUiKitColors provides LightUiKitColors,
        LocalUiKitTypography provides UiKitTypography(),
    ) {
        MaterialTheme(
            colors = materialColors,
            content = content,
        )
    }
}
