package ru.livetyping.zarina.feature.product.ui.impl.impl.product.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.media.Media
import ru.livetyping.zarina.core.uicompose.pager.rememberEndlessPagerState
import ru.livetyping.zarina.core.uikit.media.ZarinaMediaHorizontalPager
import ru.livetyping.zarina.core.uikit.pager.ZarinaHorizontalPagerIndicator

@Composable
internal fun ProductMediaPager(
    media: List<Media>,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberEndlessPagerState(itemCount = media.size)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(MediaPagerAspectRatio),
    ) {
        ZarinaMediaHorizontalPager(
            pagerState = pagerState,
            media = media,
            modifier = Modifier.matchParentSize(),
        )

        ZarinaHorizontalPagerIndicator(
            pagerState = pagerState,
            itemCount = media.size,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 16.dp),
        )
    }
}

internal const val MediaPagerAspectRatio = 0.7f
