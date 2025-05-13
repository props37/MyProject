package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer

@Composable
internal fun CityListLoading(
    topPadding: Dp,
    bottomPaddingProvider: @Composable () -> Dp,
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberZarinaSkeletonShimmer()

    LazyColumn(
        contentPadding = PaddingValues(
            top = topPadding,
            bottom = bottomPaddingProvider() + ZarinaScrollableDefaults.ScrollableBottomPadding,
        ),
        modifier = modifier,
    ) {
        items(20) {
            CityListItemSkeleton(shimmer)
        }
    }
}
