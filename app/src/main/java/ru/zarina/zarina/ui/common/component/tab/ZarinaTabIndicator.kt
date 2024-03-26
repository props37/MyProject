package ru.zarina.zarina.ui.common.component.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ZarinaTabIndicator(
    modifier: Modifier = Modifier,
    height: Dp = 1.dp,
    color: Color = LocalContentColor.current,
    shape: Shape = CircleShape,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(color)
            .clip(shape),
    )
}
