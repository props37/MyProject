package ru.zarina.zarina.ui.common.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun SelectionCircle(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    val color = UiKitTheme.colors.primaryContentColor
    val selectionFillRadiusMultiplier by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        label = "selection fill radius multiplier",
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        )
    )
    Box(
        modifier = modifier
            .size(24.dp)
            .drawWithCache {
                this.onDrawWithContent {
                    drawCircle(
                        color = color,
                        radius = this.size.minDimension / 3f,
                        style = Stroke(width = 1.dp.toPx())
                    )
                    drawCircle(
                        color = color,
                        radius = this.size.minDimension * 5 / 24f * selectionFillRadiusMultiplier,
                    )
                }
            }
    )
}
