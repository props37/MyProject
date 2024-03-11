package ru.zarina.zarina.ui.theme.old

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import ru.zarina.zarina.ui.theme.UiKitTheme

private val DarkColorScheme = darkColorScheme()

private val LightColorScheme = lightColorScheme()

@Composable
fun ZarinaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val uiKitColorsOld = UiKitColorsOld()
    val uiKitTypographyOld = UiKitTypographyOld()

    CompositionLocalProvider(
        LocalUiKitColorsOld provides uiKitColorsOld,
        LocalUiKitTypographyOld provides uiKitTypographyOld,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography.copy(
                bodyLarge = UiKitTheme.typographyOld.circle1718,
                bodySmall = UiKitTheme.typographyOld.circle1316
            ),
            content = {
                val textSelectionColors = TextSelectionColors(
                    handleColor = uiKitColorsOld.primaryContentColor,
                    backgroundColor = uiKitColorsOld.primaryContentColor.copy(alpha = 0.2f),
                )
                CompositionLocalProvider(
                    LocalTextSelectionColors provides textSelectionColors,
                ) {
                    content()
                }
            }
        )
    }
}
