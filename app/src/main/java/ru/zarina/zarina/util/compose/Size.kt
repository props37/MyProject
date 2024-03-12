package ru.zarina.zarina.util.compose

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

fun Modifier.sizeIn(
    minSize: Dp = Dp.Unspecified,
    maxSize: Dp = Dp.Unspecified,
): Modifier = this.sizeIn(
    minWidth = minSize,
    minHeight = minSize,
    maxWidth = maxSize,
    maxHeight = maxSize,
)

fun Modifier.defaultMinSize(minSize: Dp = Dp.Unspecified): Modifier = this.defaultMinSize(
    minWidth = minSize,
    minHeight = minSize,
)
