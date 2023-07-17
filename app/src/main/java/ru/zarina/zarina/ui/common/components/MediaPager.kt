package ru.zarina.zarina.ui.common.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.media3.datasource.cache.Cache
import kotlinx.collections.immutable.ImmutableList
import ru.zarina.zarina.domain.Media
import ru.zarina.zarina.utils.compose.conditional
import ru.zarina.zarina.utils.kotlin.loopingGet
import ru.zarina.zarina.utils.kotlin.roundToMultipleOf

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPager(
    media: ImmutableList<Media>,
    modifier: Modifier = Modifier,
    onMediaClick: ((Media) -> Unit)? = null,
    cache: State<Cache?> = remember { mutableStateOf(null) },
    state: PagerState = rememberInfinitePagerState(itemCount = media.size),
    aspectRatio: Float = Media.Defaults.PRODUCT_MEDIA_ASPECT_RATIO,
) {
    HorizontalPager(
        pageCount = Int.MAX_VALUE,
        beyondBoundsPageCount = 1,
        key = { it },
        state = state,
        modifier = modifier
    ) { pageIndex ->
        val item = media.loopingGet(pageIndex)
        val itemModifier = Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .conditional(onMediaClick != null && item != null) {
                this.clickable(onClick = { item?.let { onMediaClick?.invoke(it) } })
            }
        when (item?.type) {
            Media.Type.IMAGE -> ImageItem(
                media = item,
                modifier = itemModifier,
            )

            Media.Type.VIDEO -> VideoItem(
                media = item,
                cache = cache,
                modifier = itemModifier,
            )

            else -> Unit
        }
    }
}

@Composable
private fun ImageItem(
    media: Media,
    modifier: Modifier = Modifier,
) {
    AsyncImageLoader(
        url = media.url.value,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberInfinitePagerState(itemCount: Int): PagerState =
    rememberPagerState(initialPage = (Int.MAX_VALUE / 2).roundToMultipleOf(itemCount))
