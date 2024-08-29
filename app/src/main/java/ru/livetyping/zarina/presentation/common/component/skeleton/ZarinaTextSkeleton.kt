package ru.livetyping.zarina.presentation.common.component.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.util.library.shimmer.shimmerToggleable

@Composable
fun ZarinaTextSkeleton(
    textStyle: TextStyle,
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
    ) {
        // Use default font family as theme font family has huge top and bottom paddings
        Text(
            text = "",
            style = textStyle.copy(fontFamily = FontFamily.Default),
        )
    }
}
