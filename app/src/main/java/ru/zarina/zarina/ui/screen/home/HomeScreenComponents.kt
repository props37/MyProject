package ru.zarina.zarina.ui.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import coil.compose.AsyncImage
import ru.zarina.zarina.domain.rework.content.HomeBanners
import ru.zarina.zarina.ui.common.component.VideoPlayer

object HomeScreenComponents {

    // TODO: [High] Rename
    @OptIn(ExperimentalFoundationApi::class)
    @androidx.annotation.OptIn(UnstableApi::class)
    @Composable
    fun Banners(
        banners: HomeBanners,
        modifier: Modifier = Modifier,
    ) {
        // TODO: [High] Add top bar
        // TODO: [High] Do not hardcode
        val pagerState = rememberPagerState { 2 }
        HorizontalPager(
            state = pagerState,
            beyondBoundsPageCount = 0,
            userScrollEnabled = false,
            modifier = modifier,
        ) { pageIndex ->
            val listState = rememberLazyListState()
            val flingBehavior = rememberSnapFlingBehavior(listState)
            // TODO: [High] Do not hardcode
            val items = when (pageIndex) {
                0 -> banners.womenBanners
                1 -> banners.menBanners
                else -> error("Unknown page $pageIndex")
            }

            val visibleBannersIndicesState = remember {
                derivedStateOf {
                    listState.layoutInfo.visibleItemsInfo.map { it.index }
                }
            }

            LazyColumn(
                state = listState,
                flingBehavior = flingBehavior,
                modifier = Modifier.fillMaxSize(),
            ) {
                itemsIndexed(
                    items = items,
                    key = { _, banner -> banner.id.value },
                    contentType = { _, banner -> banner.mediaType }, // TODO: [High] Implement
                ) { index, banner ->
                    when (banner.mediaType) {
                        HomeBanners.Banner.MediaType.IMAGE -> {
                            AsyncImage(
                                model = banner.mediaUrl.value,
                                contentDescription = null, // TODO: [High] Implement
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillParentMaxSize(),
                            )
                        }

                        HomeBanners.Banner.MediaType.VIDEO -> {
                            val updatedIndex by rememberUpdatedState(index)
                            val isVisible by remember {
                                derivedStateOf { updatedIndex in visibleBannersIndicesState.value }
                            }

                            VideoBanner(
                                banner = banner,
                                isVisible = isVisible,
                                modifier = Modifier.fillParentMaxSize(),
                            )
                        }
                    }
                }
            }
        }
    }

    // TODO: [High] Add loader
    // TODO: [High] Add caching
    @androidx.annotation.OptIn(UnstableApi::class)
    @Composable
    private fun VideoBanner(
        banner: HomeBanners.Banner,
        isVisible: Boolean,
        modifier: Modifier = Modifier,
    ) {
        val context = LocalContext.current

        val exoPlayer = remember(context) {
            ExoPlayer.Builder(context)
                .build()
                .apply {
                    repeatMode = Player.REPEAT_MODE_ONE
                    videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                }
        }

        // Release ExoPlayer when it is no longer needed
        DisposableEffect(exoPlayer) {
            onDispose { exoPlayer.release() }
        }

        // Set media to ExoPlayer
        val mediaUrl = banner.mediaUrl.value
        LaunchedEffect(exoPlayer, mediaUrl) {
            val mediaItem = MediaItem.fromUri(mediaUrl)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }

        // Control the playback state of the media
        LifecycleStartEffect(exoPlayer, isVisible) {
            if (isVisible) exoPlayer.play()
            onStopOrDispose { exoPlayer.pause() }
        }

        VideoPlayer(
            exoPlayer = exoPlayer,
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM,
            modifier = modifier,
        )
    }
}
