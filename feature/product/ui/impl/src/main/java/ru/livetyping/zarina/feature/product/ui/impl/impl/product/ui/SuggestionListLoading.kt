package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.none
import ru.livetyping.zarina.core.uikit.product.ProductCardSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar

@Composable
internal fun SuggestionListSkeleton(
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberZarinaSkeletonShimmer()

    Column(modifier = modifier) {
        ZarinaTopBar(
            centerContent = {
                ZarinaTextSkeleton(
                    textStyle = UiKitTheme2.typography.h2,
                    shimmer = shimmer,
                    modifier = Modifier.fillMaxWidth(0.5f),
                )
            },
            windowInsets = WindowInsets.none,
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
            items(4) {
                ProductCardSkeleton(
                    shimmer = shimmer,
                    modifier = Modifier.fillParentMaxWidth(SuggestionListProductCardWidthFraction),
                )
            }
        }
    }
}
