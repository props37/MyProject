package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.core.domain.model.media.Media
import ru.livetyping.zarina.core.uicompose.pager.EndlessPagerStateUtils
import ru.livetyping.zarina.core.uicompose.pager.rememberEndlessPagerState
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer

@Composable
internal fun MediaPager(
    mediaList: List<Media>,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberEndlessPagerState(itemCount = mediaList.size)
    val shimmer = rememberZarinaSkeletonShimmer()

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
    ) { page ->
        val media = EndlessPagerStateUtils.getLooping(mediaList, page)

        if (media != null) {
            Media(
                media = media,
                shimmer = shimmer,
            )
        }
    }
}
