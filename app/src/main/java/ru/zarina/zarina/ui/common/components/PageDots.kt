package ru.zarina.zarina.ui.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun PageDots(
    count: Int,
    activeIndex: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(count) { index ->
            Dot(isActive = index == activeIndex)
        }
    }
}

@Composable
private fun Dot(
    isActive: Boolean,
    modifier: Modifier = Modifier,
) {
    val size by animateDpAsState(
        targetValue = if (isActive) 7.dp else 6.dp,
        label = "dot size"
    )
    val color by animateColorAsState(
        targetValue = if (isActive)
            UiKitTheme.colors.dotColor
        else
            UiKitTheme.colors.dotColor.copy(0.5f),
        label = "dot color"
    )
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color),
    )
}
