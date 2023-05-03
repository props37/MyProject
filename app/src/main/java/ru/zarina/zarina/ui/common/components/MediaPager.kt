package ru.zarina.zarina.ui.common.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.media3.datasource.cache.Cache
import kotlinx.collections.immutable.ImmutableList
import ru.zarina.zarina.domain.Media

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPager(
    media: ImmutableList<Media>,
    cache: State<Cache?>,
    modifier: Modifier = Modifier,
    state: PagerState = rememberPagerState(),
) {
    HorizontalPager(
        pageCount = media.size,
        beyondBoundsPageCount = 1,
        key = { media[it].url.value },
        state = state,
        modifier = modifier
    ) { pageIndex ->
        val item = media[pageIndex]
        val itemModifier = Modifier
            .fillMaxWidth()
            .aspectRatio(Media.Defaults.PRODUCT_MEDIA_ASPECT_RATIO)
        when (item.type) {
            Media.Type.IMAGE -> ImageItem(
                media = item,
                modifier = itemModifier,
            )

            Media.Type.VIDEO -> VideoItem(
                media = item,
                cache = cache,
                modifier = itemModifier,
            )
        }
    }
}

@Composable
private fun ImageItem(
    media: Media,
    modifier: Modifier = Modifier,
) {
    AsyncImageLoader(
        model = media.url.value,
        alignment = Alignment.Center,
        contentScale = ContentScale.FillWidth,
        contentDescription = null,
        modifier = modifier,
    )
}

@Composable
private fun VideoItem(
    media: Media,
    cache: State<Cache?>,
    modifier: Modifier = Modifier,
) {
    VideoPlayer(
        media = media,
        cache = cache,
        modifier = modifier
    )
}
