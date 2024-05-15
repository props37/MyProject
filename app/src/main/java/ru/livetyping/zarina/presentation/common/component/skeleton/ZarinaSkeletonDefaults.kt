package ru.livetyping.zarina.presentation.common.component.skeleton

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.presentation.theme.UiKitTheme

object ZarinaSkeletonDefaults {
    val Color: Color
        @Composable
        get() = UiKitTheme.colors.background.skeleton

    val Shape: Shape get() = RoundedCornerShape(2.dp)
}
