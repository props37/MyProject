package ru.livetyping.zarina.core.uikit.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.uikit.shimmer.shimmerToggleable
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaSkeleton(
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberZarinaSkeletonShimmer(),
    isShimmerEnabled: Boolean = true,
    color: Color = ZarinaSkeletonDefaults.Color,
    shape: Shape = ZarinaSkeletonDefaults.Shape,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .shimmerToggleable(shimmer = shimmer, isEnabled = isShimmerEnabled)
            .background(color),
    )
}

public object ZarinaSkeletonDefaults {
    public val Color: Color
        @Composable
        get() = UiKitTheme.colors.background.skeleton

    public val Shape: Shape = RoundedCornerShape(2.dp)
}
