package ru.livetyping.zarina.core.uikit.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.none
import ru.livetyping.zarina.core.uikit.theme.Colors
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaBottomSheet(
    modifier: Modifier = Modifier,
    shape: Shape = ZarinaBottomSheetDefaults.Shape,
    backgroundColor: Color = ZarinaBottomSheetDefaults.BackgroundColor,
    contentColor: Color = ZarinaBottomSheetDefaults.ContentColor,
    windowInsets: WindowInsets = WindowInsets.none,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .windowInsetsPadding(windowInsets)
            .shadow(elevation = 16.dp, shape = shape)
            .drawBehind { drawRect(backgroundColor) }
            .graphicsLayer {
                this.shape = shape
                clip = true
            },
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            content = content,
        )
    }
}

public object ZarinaBottomSheetDefaults {
    public val Shape: Shape
        get() = RectangleShape

    public val BackgroundColor: Color
        @Composable
        get() = UiKitTheme.colors.background.general.regular.default

    public val ContentColor: Color
        @Composable
        get() = UiKitTheme.colors.text.general.regular.default

    public val HorizontalPadding: Dp get() = 24.dp

    public val ScrimColor: Color = Colors.MineShaftDark.copy(alpha = 0.4f)
}
