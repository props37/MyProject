package ru.zarina.zarina.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ColorIcon(
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 8.dp,
    borderColor: Dp = 0.5.dp,
) {
    val shape = CircleShape
    val borderModifier = if (color.luminance() >= WhiteColorLuminanceThreshold) {
        Modifier.border(
            width = borderColor,
            color = UiKitTheme.colorsReworked.border.general.disabled,
            shape = shape,
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(color)
            .then(borderModifier),
    )
}

private const val WhiteColorLuminanceThreshold = 0.95f
