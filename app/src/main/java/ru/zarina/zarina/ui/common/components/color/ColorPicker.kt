package ru.zarina.zarina.ui.common.components.color

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ru.zarina.zarina.ui.common.utils.domain.toColorOr
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.domain.Color as ZarinaColor

@Composable
fun ColorPicker(
    colors: ImmutableList<ZarinaColor>,
    selectedColor: ZarinaColor?,
    modifier: Modifier = Modifier,
    onColorSelected: ((ZarinaColor) -> Unit)? = null,
    dimensions: ColorPickerDimensions = ColorPickerDefaults.dimensions(),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.colorsSpacing),
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.width(4.dp))
        colors.forEach { color ->
            ColorCircle(
                color = color,
                isSelected = color == selectedColor,
                onClick = onColorSelected?.let { { it(color) } },
                dimensions = dimensions
            )
        }
        Spacer(Modifier.width(4.dp))
    }
}

@Composable
private fun ColorCircle(
    color: ZarinaColor,
    isSelected: Boolean,
    dimensions: ColorPickerDimensions,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
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
            .clickable(
                enabled = !isSelected && onClick != null,
                onClick = onClick ?: {}
            )
            .padding(dimensions.outerPadding)
            .border(
                width = dimensions.selectionBorderWidth,
                color = selectionBorderColor,
                shape = shape,
            )
            .padding(dimensions.selectionBorderPadding)
            .border(
                width = dimensions.colorBorderWidth,
                color = UiKitTheme.colors.colorPickerCircleBorder,
                shape = shape
            )
            .clip(shape)
            .background(color.toColorOr(Color.Transparent))
    )
}
