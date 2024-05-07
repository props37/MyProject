package ru.livetyping.zarina.presentation.common.component.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.util.library.shimmer.shimmerToggleable

@Composable
fun ZarinaSkeleton(
    modifier: Modifier = Modifier,
    shimmer: Shimmer? = rememberZarinaSkeletonShimmer(),
    color: Color = ZarinaSkeletonDefaults.Color,
    shape: Shape = ZarinaSkeletonDefaults.Shape,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .shimmerToggleable(shimmer = shimmer, isEnabled = shimmer != null)
            .background(color),
    )
}
