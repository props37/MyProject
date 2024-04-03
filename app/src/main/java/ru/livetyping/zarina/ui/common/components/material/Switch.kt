package ru.livetyping.zarina.ui.common.components.material

import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import ru.livetyping.zarina.ui.theme.UiKitTheme

@Composable
fun SwitchDefaults.zarinaColors() = colors(
    checkedThumbColor = UiKitTheme.colorsOld.primaryContentColor,
    uncheckedThumbColor = UiKitTheme.colorsOld.primaryContentColor
        .copy(0.08f)
        .compositeOver(Color.White),
    checkedTrackColor = UiKitTheme.colorsOld.hint,
    uncheckedTrackColor = UiKitTheme.colorsOld.hint,
)
