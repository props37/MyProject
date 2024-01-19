package ru.zarina.zarina.ui.common.component.base.skeleton

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.ShimmerTheme
import com.valentinilk.shimmer.rememberShimmer

@Composable
fun rememberSkeletonShimmer(
    bounds: ShimmerBounds = ShimmerBounds.View,
    durationMillis: Int = DurationMillis,
    width: Dp = Width,
): Shimmer {
    val shimmerTheme = remember(durationMillis, width) {
        getShimmerTheme(durationMillis, width)
    }
    return rememberShimmer(
        shimmerBounds = bounds,
        theme = shimmerTheme,
    )
}

private fun getShimmerTheme(
    durationMillis: Int,
    width: Dp,
): ShimmerTheme {
    return ShimmerTheme(
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                delayMillis = 0,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        blendMode = BlendMode.DstIn,
        rotation = 0f,
        shaderColors = listOf(
            Color.Unspecified.copy(alpha = 1f),
            Color.Unspecified.copy(alpha = 0.4f),
            Color.Unspecified.copy(alpha = 1f),
        ),
        shaderColorStops = listOf(0f, 0.5f, 1f),
        shimmerWidth = width,
    )
}

private const val DurationMillis = 1500
private val Width: Dp get() = 200.dp
