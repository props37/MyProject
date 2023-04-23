package ru.zarina.zarina.ui.common.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.media3.datasource.cache.Cache
import coil.compose.AsyncImage
import ru.zarina.zarina.domain.Media
import ru.zarina.zarina.ui.theme.UiKitTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPager(
    media: List<Media>,
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
            .aspectRatio(3f / 4f)
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
    var isLoaded by remember(media) { mutableStateOf(false) }
    val contentAlpha by animateFloatAsState(
        targetValue = if (isLoaded) 1f else 0f,
        label = "content alpha"
    )
    Box(modifier = modifier) {
        AsyncImage(
            model = media.url.value,
            onSuccess = { isLoaded = true },
            alignment = Alignment.Center,
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = contentAlpha },
        )
        if (!isLoaded)
            CircularProgressIndicator(
                color = UiKitTheme.colors.primaryContentColor,
                modifier = Modifier.align(Alignment.Center)
            )
    }
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
