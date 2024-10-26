package ru.livetyping.zarina.core.uikit.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaLinearProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = ZarinaLinearProgressIndicatorDefaults.Color,
    backgroundColor: Color = ZarinaLinearProgressIndicatorDefaults.BackgroundColor,
    strokeCap: StrokeCap = StrokeCap.Round,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = remember { tween(durationMillis = 200) },
        label = "ZarinaLinearProgressIndicator progress",
    )

    LinearProgressIndicator(
        progress = animatedProgress,
        color = color,
        backgroundColor = backgroundColor,
        strokeCap = strokeCap,
        modifier = modifier,
    )
}

public object ZarinaLinearProgressIndicatorDefaults {
    public val Color: Color
        @Composable
        get() = UiKitTheme.colors.background.general.inversed.default

    public val BackgroundColor: Color
        @Composable
        get() = UiKitTheme.colors.background.general.regular.muted
}
