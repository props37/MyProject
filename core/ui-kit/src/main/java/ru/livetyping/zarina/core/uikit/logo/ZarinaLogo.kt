package ru.livetyping.zarina.core.uikit.logo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
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
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer
import ru.livetyping.zarina.core.resource.R
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2

@Composable
public fun ZarinaLogo(
    modifier: Modifier = Modifier,
    contentDescription: String? = stringResource(R.string.res_zarina),
    color: Color = ZarinaLogoDefaults.Color,
    animate: Boolean = false,
) {
    val shimmerModifier = if (animate) {
        Modifier.shimmer(rememberShimmer())
    } else {
        Modifier
    }

    Icon(
        painter = painterResource(R.drawable.zarina_logo),
        tint = color,
        contentDescription = contentDescription,
        modifier = modifier
            .aspectRatio(ZarinaLogoDefaults.ZarinaLogoAspectRatio)
            .then(shimmerModifier),
    )
}

@Preview
@Composable
private fun Preview() {
    ZarinaTheme2 {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            ZarinaLogo(animate = true)
        }
    }
}

@Composable
private fun rememberShimmer(): Shimmer {
    return rememberZarinaSkeletonShimmer(
        blendMode = BlendMode.DstIn,
        shaderColors = remember {
            listOf(
                Color.Unspecified.copy(alpha = 1f),
                Color.Unspecified.copy(alpha = 0.2f),
                Color.Unspecified.copy(alpha = 1f),
            )
        },
    )
}

public object ZarinaLogoDefaults {
    public val Color: Color
        @Composable
        get() = UiKitTheme.colors.icon.regular.default

    // According to R.drawable.zarina_logo size
    public const val ZarinaLogoAspectRatio: Float = 100f / 13.56f
}
