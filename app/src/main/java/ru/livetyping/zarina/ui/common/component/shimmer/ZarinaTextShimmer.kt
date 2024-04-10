package ru.livetyping.zarina.ui.common.component.shimmer

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
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
fun rememberZarinaTextShimmer(
    bounds: ShimmerBounds = ShimmerBounds.View,
    animationSpec: DurationBasedAnimationSpec<Float> = AnimationSpec,
    targetColorAlpha: Float = TargetColorAlpha,
    width: Dp = Width,
): Shimmer {
    val theme = remember(animationSpec, targetColorAlpha, width) {
        getShimmerTheme(animationSpec, targetColorAlpha, width)
    }
    return rememberShimmer(
        shimmerBounds = bounds,
        theme = theme,
    )
}

private fun getShimmerTheme(
    animationSpec: DurationBasedAnimationSpec<Float>,
    targetColorAlpha: Float,
    width: Dp,
): ShimmerTheme {
    return ShimmerTheme(
        animationSpec = infiniteRepeatable(
            animation = animationSpec,
            repeatMode = RepeatMode.Restart,
        ),
        blendMode = BlendMode.DstIn,
        rotation = 0f,
        shaderColors = listOf(
            Color.Unspecified.copy(alpha = 1f),
            Color.Unspecified.copy(alpha = targetColorAlpha),
            Color.Unspecified.copy(alpha = 1f),
        ),
        shaderColorStops = listOf(0f, 0.5f, 1f),
        shimmerWidth = width,
    )
}

@Stable
private val AnimationSpec: DurationBasedAnimationSpec<Float>
    get() = tween(durationMillis = 1500, easing = LinearEasing, delayMillis = 0)

private const val TargetColorAlpha = 0.2f

private val Width: Dp get() = 600.dp
