package ru.zarina.zarina.ui.common.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import ru.zarina.zarina.domain.Media

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPager(
    media: List<Media>,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        pageCount = media.size,
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
    // TODO add loader
    AsyncImage(
        model = media.url.value,
        alignment = Alignment.Center,
        contentScale = ContentScale.FillWidth,
        contentDescription = null,
        modifier = modifier
    )
}

@Composable
private fun VideoItem(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier)
}
