package ru.livetyping.zarina.presentation.common.component.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.presentation.theme.Colors
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.none

@Composable
fun ZarinaBottomSheet(
    modifier: Modifier = Modifier,
    shape: Shape = ZarinaBottomSheetDefaults.Shape,
    containerColor: Color = ZarinaBottomSheetDefaults.ContainerColor,
    contentColor: Color = ZarinaBottomSheetDefaults.ContentColor,
    windowInsets: WindowInsets = WindowInsets.none,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .windowInsetsPadding(windowInsets)
            .shadow(elevation = 16.dp, shape = shape)
            .drawBehind { drawRect(containerColor) }
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

object ZarinaBottomSheetDefaults {
    val Shape: Shape
        get() = RoundedCornerShape(
            topStart = 4.dp,
            topEnd = 4.dp,
            bottomEnd = 0.dp,
            bottomStart = 0.dp,
        )

    val ContainerColor: Color
        @Composable
        get() = UiKitTheme.colors.background.general.regular.default

    val ContentColor: Color
        @Composable
        get() = UiKitTheme.colors.text.general.regular.default

    val ScrimColor: Color = Colors.MineShaftDark.copy(alpha = 0.4f)
}
