package ru.zarina.zarina.ui.common.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.ShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.rework.ZarinaTheme

@Composable
fun ZarinaLogo(
    modifier: Modifier = Modifier,
    contentDescription: String? = stringResource(R.string.zarina),
    color: Color = UiKitTheme.colorsReworked.icon.regular.default,
    animate: Boolean = false,
) {
    val shimmerTheme = remember { getShimmerTheme() }
    val shimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.View,
        theme = shimmerTheme,
    )
    val shimmerModifier = if (animate) Modifier.shimmer(shimmer) else Modifier

    Icon(
        painter = painterResource(R.drawable.zarina_logo),
        tint = color,
        contentDescription = contentDescription,
        modifier = modifier.then(shimmerModifier),
    )
}

private fun getShimmerTheme(): ShimmerTheme {
    return ShimmerTheme(
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = LinearEasing,
                delayMillis = 0,
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
        shimmerWidth = 600.dp,
    )
}

// According to R.drawable.zarina_logo size
const val ZarinaLogoAspectRatio = 100f / 10

@Preview
@Composable
private fun Preview() {
    ZarinaTheme {
        Box(modifier = Modifier.background(Color.White)) {
            ZarinaLogo(
                animate = true,
                modifier = Modifier
                    .padding(32.dp)
                    .width(200.dp)
                    .aspectRatio(ZarinaLogoAspectRatio),
            )
        }
    }
}
