package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.core.domain.model.media.Media
import ru.livetyping.zarina.core.uicompose.pager.rememberEndlessPagerState
import ru.livetyping.zarina.core.uikit.media.ZarinaMediaHorizontalPager
import ru.livetyping.zarina.core.uikit.product.ProductDefaults

@Composable
internal fun MediaPager(
    mediaList: List<Media>,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.aspectRatio(ProductDefaults.MediaAspectRatio)) {
        ZarinaMediaHorizontalPager(
            pagerState = rememberEndlessPagerState(itemCount = mediaList.size),
            media = mediaList,
            modifier = Modifier.matchParentSize(),
        )
    }
}
