package ru.livetyping.zarina.feature.profile.ui.impl.impl.order.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

internal val OrderInfoContentPadding: PaddingValues
    get() = PaddingValues(16.dp)

internal val OrderInfoNameTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.tertiary.light

internal val OrderInfoValueTextStyle: TextStyle
    @Composable
    get() = UiKitTheme.typography.secondary.light
