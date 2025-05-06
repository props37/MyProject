package ru.livetyping.zarina.core.uikit.theme

import androidx.compose.runtime.Composable

// TODO: [Top] Rename after full migration

public object UiKitTheme2 {
    public val colors: UiKitColors2
        @Composable
        get() = LocalUiKitColors2.current

    public val typography: UiKitTypography2
        @Composable
        get() = LocalUiKitTypography2.current
}
