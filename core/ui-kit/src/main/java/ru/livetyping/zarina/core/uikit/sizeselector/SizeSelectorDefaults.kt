package ru.livetyping.zarina.core.uikit.sizeselector

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

internal object SizeSelectorDefaults {
    val HeaderIconSize: Dp = 20.dp
    val HeaderContentPadding: PaddingValues get() = PaddingValues(vertical = 4.dp)
    val HeaderTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.primary.bold

    val SizeMainTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.light

    val SizeAdditionalTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.caption1.regular
}
