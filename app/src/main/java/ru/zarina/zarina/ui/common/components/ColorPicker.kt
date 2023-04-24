package ru.zarina.zarina.ui.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.common.utils.domain.toColorOr
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.utils.compose.minInteractionSize
import ru.zarina.zarina.domain.Color as ZarinaColor

@Composable
fun ColorPicker(
    colors: List<ZarinaColor>,
    selectedColor: ZarinaColor?,
    onColorSelected: (ZarinaColor) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        colors.forEach { color ->
            ColorCircle(
                color = color,
                isSelected = color == selectedColor,
                onClick = { onColorSelected(color) }
            )
        }
    }
}

@Composable
private fun ColorCircle(
    color: ZarinaColor,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = CircleShape
    val selectionBorderColor by animateColorAsState(
        targetValue = if (isSelected) UiKitTheme.colors.colorPickerCircleSelectionBorder else Color.Transparent,
        label = "selection border color"
    )
    Box(
        modifier = modifier
            .minInteractionSize()
            .clip(shape)
            .clickable(
                enabled = !isSelected,
                onClick = onClick
            )
            .padding(4.dp)
            .border(
                width = 1.dp,
                color = selectionBorderColor,
                shape = shape,
            )
            .padding(5.dp)
            .border(
                width = 1.dp,
                color = UiKitTheme.colors.colorPickerCircleBorder,
                shape = shape
            )
            .clip(shape)
            .background(color.toColorOr(Color.Transparent))
    )
}
