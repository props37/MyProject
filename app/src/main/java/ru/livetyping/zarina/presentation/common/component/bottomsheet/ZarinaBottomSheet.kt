package ru.livetyping.zarina.presentation.common.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.presentation.theme.Colors
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.none

@Composable
fun ZarinaBottomSheet(
    modifier: Modifier = Modifier,
    shape: Shape = ZarinaBottomSheetDefaults.Shape,
    windowInsets: WindowInsets = WindowInsets.none,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .windowInsetsPadding(windowInsets)
            .shadow(elevation = 16.dp, shape = shape)
            .background(
                color = UiKitTheme.colors.background.general.regular.default,
                shape = shape,
            ),
    ) {
        content()
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

    val ScrimColor = Colors.MineShaftDark.copy(alpha = 0.4f)
}
