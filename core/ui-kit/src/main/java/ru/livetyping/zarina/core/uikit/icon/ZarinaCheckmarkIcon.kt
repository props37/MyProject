package ru.livetyping.zarina.core.uikit.icon

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import ru.livetyping.zarina.core.resource.R
import ru.livetyping.zarina.core.uikit.button.ZarinaIconButtonDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaCheckmarkIcon(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    @Suppress("NAME_SHADOWING")
    contentDescriptionResId: (Boolean) -> Int? = { isVisible ->
        if (isVisible) R.string.checkmark_checked else R.string.checkmark_unchecked
    },
    color: Color = ZarinaIconButtonDefaults.IconColor,
    maskColor: Color = UiKitTheme.colors.background.general.regular.default,
    iconSize: Dp = ZarinaIconButtonDefaults.IconSize,
) {
    Box(modifier = modifier) {
        val contentDescription = contentDescriptionResId(isVisible)?.let { stringResource(it) }
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_checkmark_24),
            contentDescription = contentDescription,
            tint = color,
            modifier = Modifier.size(iconSize),
        )

        val maskWidthFraction by animateFloatAsState(
            targetValue = if (isVisible) 0f else 1f,
            animationSpec = tween(durationMillis = 200),
            label = "ZarinaCheckmarkIcon mask width fraction",
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .drawBehind {
                    val width = size.width * maskWidthFraction
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
