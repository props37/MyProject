package ru.zarina.zarina.ui.common.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import ru.zarina.zarina.domain.rework.common.Media
import ru.zarina.zarina.ui.common.component.base.media.VideoPlayer
import ru.zarina.zarina.util.kotlin.loopingGet

// TODO: [High] Add shimmer while loading
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaHorizontalPager(
    pagerState: PagerState,
    medias: ImmutableList<Media>,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
    ) { page ->
        val media = medias.loopingGet(page)
        when (media?.type) {
            Media.Type.IMAGE -> {
                AsyncImage(
                    model = media.url.value,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Media.Type.VIDEO -> {
                VideoPlayer(
                    media = media,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            null -> Unit
        }
    }
}
