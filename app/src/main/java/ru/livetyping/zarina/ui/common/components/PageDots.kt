package ru.livetyping.zarina.ui.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.ui.theme.UiKitTheme

private const val VISIBLE_DOT_COUNT = 5

@Composable
fun PageDots(
    count: Int,
    activeIndex: Int,
    modifier: Modifier = Modifier,
    onDotClick: (index: Int) -> Unit = {},
) {
    val startIndex = (activeIndex - VISIBLE_DOT_COUNT / 2)
        .coerceAtLeast(0)
        .coerceAtMost(count - VISIBLE_DOT_COUNT)
    val endIndex = startIndex + VISIBLE_DOT_COUNT - 1
    val visibleRange = startIndex..endIndex
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(count) { index ->
            val state = when {
                index == activeIndex -> DotState.ACTIVE
                index !in visibleRange -> DotState.HIDDEN
                (index == visibleRange.first || index == visibleRange.last) && index != 0 && index != count - 1 -> DotState.COLLAPSED
                else -> DotState.VISIBLE
            }
            Dot(
                state = state,
                onClick = { onDotClick(index) },
            )
        }
    }
}

@Composable
private fun Dot(
    state: DotState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val size by animateDpAsState(
        targetValue = when (state) {
            DotState.ACTIVE -> 7.dp
            DotState.VISIBLE -> 6.dp
            DotState.COLLAPSED -> 3.dp
            else -> 0.dp
        },
        label = "dot size"
    )
    val color by animateColorAsState(
        targetValue = if (state == DotState.ACTIVE)
            UiKitTheme.colorsOld.pagerDot
        else
            UiKitTheme.colorsOld.pagerDot.copy(0.5f),
        label = "dot color"
    )
    val horizontalPadding by animateDpAsState(
        targetValue = if (state != DotState.HIDDEN) 3.dp else 0.dp,
        label = "horizontal padding"
    )
    Box(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 16.dp, horizontal = horizontalPadding)
            .size(size)
            .clip(CircleShape)
            .background(color),
    )
}

private enum class DotState { HIDDEN, COLLAPSED, VISIBLE, ACTIVE }
