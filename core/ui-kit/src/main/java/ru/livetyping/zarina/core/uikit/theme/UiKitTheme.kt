package ru.livetyping.zarina.core.uikit.theme

import androidx.compose.runtime.Composable

// TODO: [Top] Remove after full migration

public object UiKitTheme {
    public val colors: UiKitColors
        @Composable
        get() = LocalUiKitColors.current

    public val typography: UiKitTypography
        @Composable
        get() = LocalUiKitTypography.current
}
