package ru.livetyping.zarina.ui.common.component.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.library.shimmer.shimmerToggleable

@Composable
fun ZarinaTextSkeleton(
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    shimmer: Shimmer? = rememberZarinaSkeletonShimmer(),
    color: Color = UiKitTheme.colors.background.skeleton,
    shape: Shape = ZarinaSkeletonTextShape,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .shimmerToggleable(shimmer = shimmer, isEnabled = shimmer != null)
            .background(color),
    ) {
        Text(
            text = "",
            style = textStyle,
        )
    }
}
