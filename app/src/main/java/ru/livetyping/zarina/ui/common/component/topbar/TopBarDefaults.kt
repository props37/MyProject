package ru.livetyping.zarina.ui.common.component.topbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object TopBarDefaults {
    val MinHeight: Dp get() = 56.dp

    val HorizontalPadding: Dp get() = 16.dp
    val VerticalPadding: Dp get() = 8.dp
    val ContentPadding: PaddingValues
        get() = PaddingValues(horizontal = HorizontalPadding, vertical = VerticalPadding)
}
