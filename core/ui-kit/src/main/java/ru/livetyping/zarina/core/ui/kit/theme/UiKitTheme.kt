package ru.livetyping.zarina.core.ui.kit.theme

import androidx.compose.runtime.Composable

public object UiKitTheme {
    public val colors: UiKitColors
        @Composable
        get() = LocalUiKitColors.current

    public val typography: UiKitTypography
        @Composable
        get() = LocalUiKitTypography.current
}
