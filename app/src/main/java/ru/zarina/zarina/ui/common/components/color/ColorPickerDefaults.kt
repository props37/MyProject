package ru.zarina.zarina.ui.common.components.color

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ColorPickerDefaults {
    @Composable
    fun smallDimensions(
        colorsSpacing: Dp = 8.dp,
        size: Dp = 22.dp,
        outerPadding: Dp = 0.dp,
        selectionBorderWidth: Dp = 1.dp,
        selectionBorderPadding: Dp = 2.dp,
        colorBorderWidth: Dp = 1.dp,
    ) = ColorPickerDimensions(
        colorsSpacing = colorsSpacing,
        circleSize = size,
        outerPadding = outerPadding,
        selectionBorderWidth = selectionBorderWidth,
        selectionBorderPadding = selectionBorderPadding,
        colorBorderWidth = colorBorderWidth,
    )

    @Composable
    fun dimensions(
        colorsSpacing: Dp = 12.dp,
        size: Dp = 48.dp,
        outerPadding: Dp = 4.dp,
        selectionBorderWidth: Dp = 1.dp,
        selectionBorderPadding: Dp = 5.dp,
        colorBorderWidth: Dp = 1.dp,
    ) = ColorPickerDimensions(
        colorsSpacing = colorsSpacing,
        circleSize = size,
        outerPadding = outerPadding,
        selectionBorderWidth = selectionBorderWidth,
        selectionBorderPadding = selectionBorderPadding,
        colorBorderWidth = colorBorderWidth,
    )
}

@Immutable
data class ColorPickerDimensions constructor(
    val colorsSpacing: Dp = 8.dp,
    val circleSize: Dp = 22.dp,
    val outerPadding: Dp = 0.dp,
    val selectionBorderWidth: Dp = 1.dp,
    val selectionBorderPadding: Dp = 2.dp,
    val colorBorderWidth: Dp = 1.dp,
)
