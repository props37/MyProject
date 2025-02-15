package ru.livetyping.zarina.core.uicomponent.filtration

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

internal val FilterMinHeight: Dp get() = 56.dp

internal val FilterTitleTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.secondary.light

internal val FilterTitleColor: Color
    @Composable
    get() = UiKitTheme.colors.text.general.regular.default
