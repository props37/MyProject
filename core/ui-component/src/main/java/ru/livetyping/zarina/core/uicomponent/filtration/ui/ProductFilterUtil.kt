package ru.livetyping.zarina.core.uicomponent.filtration.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2

internal val FilterMinHeight: Dp get() = 56.dp

internal val FilterTitleTextStyle: TextStyle
    @Composable
    get() = UiKitTheme2.typography.body

internal val FilterTitleColor: Color
    @Composable
    get() = UiKitTheme2.colors.mainBlack
