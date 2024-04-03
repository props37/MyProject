package ru.livetyping.zarina.ui.common.component.icon

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.ui.theme.UiKitTheme

// TODO: [Low] Rewrite animation
@Composable
fun ZarinaCheckmarkAnimatedIcon(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    contentDescriptionResId: (Boolean) -> Int? = { isVisible ->
        if (isVisible) R.string.checkmark_checked else R.string.checkmark_unchecked
    },
    color: Color = UiKitTheme.colors.icon.regular.default,
    maskColor: Color = UiKitTheme.colors.background.general.regular.default,
    iconSize: Dp = 24.dp,
) {
    Box(modifier = modifier) {
        val contentDescription = contentDescriptionResId(isVisible)?.let { stringResource(it) }
        Icon(
            painter = painterResource(R.drawable.ic_checkmark_24),
            contentDescription = contentDescription,
            tint = color,
            modifier = Modifier.size(iconSize),
        )

        val maskWidthFraction = animateFloatAsState(
            targetValue = if (isVisible) 0f else 1f,
            animationSpec = tween(durationMillis = 200),
            label = "ZarinaCheckmarkAnimatedIcon mask",
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .drawBehind {
                    val width = size.width * maskWidthFraction.value
                    val topLeft = Offset(size.width - width, 0f)
                    val size = Size(width, size.height)
                    drawRect(
                        color = maskColor,
                        topLeft = topLeft,
                        size = size,
                    )
                }
        )
    }
}
