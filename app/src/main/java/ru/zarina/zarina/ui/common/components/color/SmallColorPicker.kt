package ru.zarina.zarina.ui.common.components.color

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableList
import ru.zarina.zarina.ui.common.utils.domain.toColorOr
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.domain.Color as ZarinaColor

@Composable
fun SmallColorPicker(
    colors: ImmutableList<ZarinaColor>,
    selectedColor: ZarinaColor?,
    modifier: Modifier = Modifier,
    dimensions: ColorPickerDimensions = ColorPickerDefaults.smallDimensions(),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.colorsSpacing),
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {
        colors.forEach { color ->
            ColorCircle(
                color = color,
                isSelected = color == selectedColor,
                dimensions = dimensions,
            )
        }
    }
}

@Composable
private fun ColorCircle(
    color: ZarinaColor,
    isSelected: Boolean,
    dimensions: ColorPickerDimensions,
    modifier: Modifier = Modifier,
) {
    val shape = CircleShape
    val selectionBorderColor by animateColorAsState(
        targetValue = if (isSelected) UiKitTheme.colors.colorPickerCircleSelectionBorder else Color.Transparent,
        label = "selection border color"
    )
    Box(
        modifier = modifier
            .size(dimensions.circleSize)
            .clip(shape)
            .border(
                width = dimensions.selectionBorderWidth,
                color = selectionBorderColor,
                shape = shape,
            )
            .padding(dimensions.selectionBorderPadding)
            .border(
                width = dimensions.colorBorderWidth,
                color = UiKitTheme.colors.colorPickerCircleBorder,
                shape = shape,
            )
            .clip(shape)
            .background(color.toColorOr(Color.Transparent))
    )
}

