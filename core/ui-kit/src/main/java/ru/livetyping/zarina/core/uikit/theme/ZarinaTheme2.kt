package ru.livetyping.zarina.core.uikit.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import ru.livetyping.zarina.core.uikit.impl.theme.Colors2

@Composable
public fun ZarinaTheme2(
    isDarkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val materialColors = remember(isDarkTheme) {
        if (isDarkTheme) {
            darkColors(primary = Colors2.MineShaft)
        } else {
            lightColors(primary = Colors2.MineShaft)
        }
    }

    MaterialTheme(
        colors = materialColors,
        content = content,
    )
}
