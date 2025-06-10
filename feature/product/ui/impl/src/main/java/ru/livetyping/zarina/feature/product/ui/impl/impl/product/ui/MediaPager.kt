package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.media.Media
import ru.livetyping.zarina.core.uicompose.pager.rememberEndlessPagerState
import ru.livetyping.zarina.core.uikit.media.ZarinaMediaHorizontalPager
import ru.livetyping.zarina.core.uikit.pager.ZarinaHorizontalPagerIndicator
import ru.livetyping.zarina.core.uikit.pager.ZarinaHorizontalPagerIndicatorStyle
import ru.livetyping.zarina.core.uikit.product.ProductDefaults

@Composable
internal fun MediaPager(
    mediaList: List<Media>,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.aspectRatio(ProductDefaults.MediaAspectRatio)) {
        val pagerState = rememberEndlessPagerState(itemCount = mediaList.size)

        ZarinaMediaHorizontalPager(
            pagerState = pagerState,
            media = mediaList,
            modifier = Modifier.matchParentSize(),
        )

        ZarinaHorizontalPagerIndicator(
            pagerState = pagerState,
            itemCount = mediaList.size,
            style = ZarinaHorizontalPagerIndicatorStyle.Rectangles(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
        )
    }
}
