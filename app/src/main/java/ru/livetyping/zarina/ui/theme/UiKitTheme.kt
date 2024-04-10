package ru.livetyping.zarina.ui.theme

import androidx.compose.runtime.Composable

object UiKitTheme {
    val colors: UiKitColors
        @Composable
        get() = LocalUiKitColors.current

    val typography: UiKitTypography
        @Composable
        get() = LocalUiKitTypography.current
}
