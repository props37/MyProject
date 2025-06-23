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
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeletonShimmerDefaults.BlendMode
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeletonShimmerDefaults.DelayMillis
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeletonShimmerDefaults.DurationMillis
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeletonShimmerDefaults.Rotation
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeletonShimmerDefaults.ShaderColors
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeletonShimmerDefaults.Width

@Composable
public fun rememberZarinaSkeletonShimmer(
    bounds: ShimmerBounds = ShimmerBounds.View,
    durationMillis: Int = DurationMillis,
    delayMillis: Int = DelayMillis,
    rotation: Float = Rotation,
    blendMode: BlendMode = BlendMode,
    shaderColors: List<Color> = ShaderColors,
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
    durationMillis: Int = DurationMillis,
    delayMillis: Int = DelayMillis,
    rotation: Float = Rotation,
    blendMode: BlendMode = BlendMode,
    shaderColors: List<Color> = ShaderColors,
    width: Dp = Width,
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

public object ZarinaSkeletonShimmerDefaults {
    public const val DurationMillis: Int = 1500
    public const val DelayMillis: Int = 800

    public val ShaderColors: List<Color> = listOf(
        Color.White.copy(alpha = 0.25f),
        Color.White.copy(alpha = 0.6f),
        Color.White.copy(alpha = 0.6f),
        Color.White.copy(alpha = 0.25f),
    )

    public const val Rotation: Float = 15f

    public val BlendMode: BlendMode = androidx.compose.ui.graphics.BlendMode.Overlay

    public val Width: Dp = 400.dp
}
