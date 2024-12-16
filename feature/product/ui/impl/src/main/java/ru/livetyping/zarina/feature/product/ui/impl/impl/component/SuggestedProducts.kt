package ru.livetyping.zarina.feature.product.ui.impl.impl.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.uikit.product.ProductCardSmallSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer

@Composable
internal fun SuggestedProductsSkeleton(
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberZarinaSkeletonShimmer(),
    contentPadding: PaddingValues = PaddingValues(),
) {
    LazyRow(
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(SuggestedProductsSpacedBy),
        modifier = modifier,
    ) {
        items(count = SuggestedProductsSkeletonCount) {
            ProductCardSmallSkeleton(
                shimmer = shimmer,
                modifier = Modifier.width(SuggestedProductCardWidth),
            )
        }
    }
}

private val SuggestedProductsSpacedBy: Dp get() = 12.dp
private const val SuggestedProductsSkeletonCount = 4
private val SuggestedProductCardWidth: Dp get() = 176.dp
