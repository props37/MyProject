package ru.livetyping.zarina.core.uikit.topbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

public object ZarinaTopBarDefaults {
    public val MinHeight: Dp = 56.dp

    public val HorizontalPadding: Dp get() = 16.dp
    public val VerticalPadding: Dp get() = 8.dp
    public val ContentPadding: PaddingValues
        get() = PaddingValues(horizontal = HorizontalPadding, vertical = VerticalPadding)
}
