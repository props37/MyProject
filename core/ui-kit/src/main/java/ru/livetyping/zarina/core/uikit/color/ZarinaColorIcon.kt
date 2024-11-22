package ru.livetyping.zarina.core.uikit.color

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
import ru.livetyping.zarina.core.uikit.color.ZarinaColorIconDefaults.WhiteColorLuminanceThreshold
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaColorIcon(
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = ZarinaColorIconDefaults.Size,
    borderWidth: Dp = ZarinaColorIconDefaults.BorderWidth,
) {
    val shape = CircleShape
    val borderModifier = if (color.luminance() >= WhiteColorLuminanceThreshold) {
        Modifier.border(
            width = borderWidth,
            color = UiKitTheme.colors.border.general.disabled,
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

public object ZarinaColorIconDefaults {
    public val Size: Dp = 8.dp
    public val BorderWidth: Dp = 0.5.dp

    internal const val WhiteColorLuminanceThreshold = 0.95f
}
