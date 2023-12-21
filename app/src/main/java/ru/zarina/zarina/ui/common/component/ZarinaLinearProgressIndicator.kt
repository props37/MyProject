package ru.zarina.zarina.ui.common.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ZarinaLinearProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = UiKitTheme.colorsReworked.background.general.inverse.inverse,
    trackColor: Color = UiKitTheme.colorsReworked.background.general.regular.muted,
    strokeCap: StrokeCap = StrokeCap.Round,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = remember { tween(durationMillis = 200, delayMillis = 1) },
        label = "ZarinaLinearProgressIndicator progress",
    )

    LinearProgressIndicator(
        progress = animatedProgress,
        color = color,
        trackColor = trackColor,
        strokeCap = strokeCap,
        modifier = modifier,
    )
}
