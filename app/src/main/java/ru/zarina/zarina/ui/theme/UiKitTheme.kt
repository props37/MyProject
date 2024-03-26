package ru.zarina.zarina.ui.theme

import androidx.compose.runtime.Composable
import ru.zarina.zarina.ui.theme.old.LocalUiKitColorsOld
import ru.zarina.zarina.ui.theme.old.LocalUiKitTypographyOld
import ru.zarina.zarina.ui.theme.old.UiKitColorsOld
import ru.zarina.zarina.ui.theme.old.UiKitTypographyOld

object UiKitTheme {
    val colors: UiKitColors
        @Composable
        get() = LocalUiKitColors.current

    val typography: UiKitTypography
        @Composable
        get() = LocalUiKitTypography.current

    @Deprecated(
        message = "Use UiKitTheme.colors instead.",
        replaceWith = ReplaceWith("UiKitTheme.colors"),
    )
    val colorsOld: UiKitColorsOld
        @Composable
        get() = LocalUiKitColorsOld.current

    @Deprecated(
        message = "Use UiKitTheme.typography instead.",
        replaceWith = ReplaceWith("UiKitTheme.typography"),
    )
    val typographyOld: UiKitTypographyOld
        @Composable
        get() = LocalUiKitTypographyOld.current
}
