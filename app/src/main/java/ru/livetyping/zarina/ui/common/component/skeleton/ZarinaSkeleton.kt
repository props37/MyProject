package ru.livetyping.zarina.ui.common.component.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun ZarinaSkeleton(
    modifier: Modifier = Modifier,
    shimmer: Shimmer? = rememberZarinaSkeletonShimmer(),
    color: Color = ZarinaSkeletonDefaults.Color,
    shape: Shape = ZarinaSkeletonDefaults.Shape,
) {
    val shimmerModifier = if (shimmer != null) Modifier.shimmer(shimmer) else Modifier
    Box(
        modifier = modifier
            .clip(shape)
            .then(shimmerModifier)
            .background(color),
    )
}
