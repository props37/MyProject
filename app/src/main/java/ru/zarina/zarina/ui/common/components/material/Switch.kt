package ru.zarina.zarina.ui.common.components.material

import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun SwitchDefaults.zarinaColors() = colors(
    checkedThumbColor = UiKitTheme.colors.primaryContentColor,
    uncheckedThumbColor = UiKitTheme.colors.primaryContentColor
        .copy(0.08f)
        .compositeOver(Color.White),
    checkedTrackColor = UiKitTheme.colors.hint,
    uncheckedTrackColor = UiKitTheme.colors.hint,
)
