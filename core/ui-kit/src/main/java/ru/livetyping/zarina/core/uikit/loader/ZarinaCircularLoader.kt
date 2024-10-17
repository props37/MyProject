package ru.livetyping.zarina.core.uikit.loader

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaCircularLoader(
    modifier: Modifier = Modifier,
    color: Color = ZarinaCircularLoaderDefaults.Color,
    strokeWidth: Dp = 2.dp,
    strokeCap: StrokeCap = StrokeCap.Round,
) {
    CircularProgressIndicator(
        color = color,
        strokeWidth = strokeWidth,
        strokeCap = strokeCap,
        modifier = modifier,
    )
}

public object ZarinaCircularLoaderDefaults {
    internal val Color: Color
        @Composable
        get() = UiKitTheme.colors.icon.regular.default
}
