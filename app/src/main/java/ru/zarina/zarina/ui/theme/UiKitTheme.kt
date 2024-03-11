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

    val colorsOld: UiKitColorsOld
        @Composable
        get() = LocalUiKitColorsOld.current

    val typographyOld: UiKitTypographyOld
        @Composable
        get() = LocalUiKitTypographyOld.current
}
