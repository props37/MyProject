package ru.livetyping.zarina.core.uikit.skeleton

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

public object ZarinaSkeletonDefaults {
    public val Color: Color
        @Composable
        get() = UiKitTheme.colors.background.skeleton

    public val Shape: Shape = RoundedCornerShape(2.dp)
}
