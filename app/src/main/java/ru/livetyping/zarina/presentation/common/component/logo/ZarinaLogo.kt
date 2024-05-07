package ru.livetyping.zarina.presentation.common.component.logo

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
import com.valentinilk.shimmer.shimmer
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.presentation.theme.ZarinaTheme

@Composable
fun ZarinaLogo(
    modifier: Modifier = Modifier,
    contentDescription: String? = stringResource(R.string.zarina),
    color: Color = UiKitTheme.colors.icon.regular.default,
    animate: Boolean = false,
) {
    val shimmer = rememberZarinaSkeletonShimmer(
        blendMode = BlendMode.DstIn,
        shaderColors = remember {
            listOf(
                Color.Unspecified.copy(alpha = 1f),
                Color.Unspecified.copy(alpha = 0.2f),
                Color.Unspecified.copy(alpha = 1f),
            )
        },
        width = 600.dp,
    )
    val shimmerModifier = if (animate) Modifier.shimmer(shimmer) else Modifier

    Icon(
        painter = painterResource(R.drawable.zarina_logo),
        tint = color,
        contentDescription = contentDescription,
        modifier = modifier.then(shimmerModifier),
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
