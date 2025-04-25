package ru.livetyping.zarina.core.uikit.theme

import androidx.compose.runtime.Composable

public object UiKitTheme2 {
    public val colors: UiKitColors2
        @Composable
        get() = LocalUiKitColors2.current
}
