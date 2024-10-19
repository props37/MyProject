package ru.livetyping.zarina.core.uikit.skeleton

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
public fun rememberZarinaSkeletonShimmer(
    bounds: ShimmerBounds = ShimmerBounds.View,
    durationMillis: Int = DurationMillis,
    delayMillis: Int = DelayMillis,
    rotation: Float = 0f,
    blendMode: BlendMode = BlendMode.Overlay,
    shaderColors: List<Color> = remember { ShaderColors },
    width: Dp = Width,
): Shimmer {
    val theme = rememberZarinaSkeletonShimmerTheme(
        durationMillis = durationMillis,
        delayMillis = delayMillis,
        rotation = rotation,
        blendMode = blendMode,
        shaderColors = shaderColors,
        width = width,
    )
    return rememberShimmer(
        shimmerBounds = bounds,
        theme = theme,
    )
}

@Composable
public fun rememberZarinaSkeletonShimmerTheme(
    durationMillis: Int,
    delayMillis: Int,
    rotation: Float,
    blendMode: BlendMode,
    shaderColors: List<Color>,
    width: Dp,
): ShimmerTheme {
    return remember(durationMillis, delayMillis, rotation, blendMode, shaderColors, width) {
        ShimmerTheme(
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    delayMillis = delayMillis,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            blendMode = blendMode,
            rotation = rotation,
            shaderColors = shaderColors,
            shaderColorStops = null,
            shimmerWidth = width,
        )
    }
}

private const val DurationMillis = 1500
private const val DelayMillis = 500

private val ShaderColors: List<Color> = listOf(
    Color.White.copy(alpha = 0.01f),
    Color.White.copy(alpha = 0.6f),
    Color.White.copy(alpha = 0.01f),
)

private val Width: Dp get() = 200.dp
