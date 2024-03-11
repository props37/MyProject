package ru.zarina.zarina.ui.theme

import androidx.compose.runtime.Composable
import ru.zarina.zarina.ui.theme.old.LocalUiKitColorsOld
import ru.zarina.zarina.ui.theme.old.LocalUiKitTypography
import ru.zarina.zarina.ui.theme.old.UiKitColorsOld
import ru.zarina.zarina.ui.theme.old.UiKitTypography

object UiKitTheme {
    val colorsReworked: UiKitColorsReworked
        @Composable
        get() = LocalUiKitColorsReworked.current

    val typographyReworked: UiKitTypographyReworked
        @Composable
        get() = LocalUiKitTypographyReworked.current

    val colorsOld: UiKitColorsOld
        @Composable
        get() = LocalUiKitColorsOld.current

    val typography: UiKitTypography
        @Composable
        get() = LocalUiKitTypography.current
}
