package ru.zarina.zarina.ui.common.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ru.zarina.zarina.ui.theme.old.UiKitTheme

@Composable
fun ElevationContainer(
    isElevated: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val elevationDp by animateDpAsState(
        if (isElevated) 6.dp else 0.dp,
        label = "elevation"
    )
    val backgroundColor = UiKitTheme.colors.screenBackground
    Box(
        modifier = modifier
            .graphicsLayer {
                shadowElevation = elevationDp.toPx()
            }
            .drawBehind {
                if (elevationDp > 0.dp) drawRect(backgroundColor)
            }
            .zIndex(1000f),
    ) {
        content()
    }
}
