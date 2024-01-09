package ru.zarina.zarina.ui.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import coil.compose.AsyncImage
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.content.HomeBanners
import ru.zarina.zarina.ui.common.component.LooseTabRow
import ru.zarina.zarina.ui.common.component.LooseTabRowDefaults.looseTabIndicatorOffset
import ru.zarina.zarina.ui.common.component.VideoPlayer
import ru.zarina.zarina.ui.common.component.ZarinaLogo
import ru.zarina.zarina.ui.common.component.ZarinaLogoAspectRatio
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonSize
import ru.zarina.zarina.ui.screen.home.HomeViewModel.Tab
import ru.zarina.zarina.ui.theme.UiKitTheme

object HomeScreenComponents {

    @Composable
    fun TabBar(
        tabs: List<Tab>,
        currentTab: Tab,
        onTabClicked: (Tab) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier,
        ) {
            ZarinaLogo(
                modifier = Modifier
                    .width(140.dp)
                    .aspectRatio(ZarinaLogoAspectRatio),
            )

            Spacer(modifier = Modifier.height(20.dp))

            val selectedTabIndex = tabs.indexOf(currentTab)
            LooseTabRow(
                selectedTabIndex = selectedTabIndex,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier
                            .looseTabIndicatorOffset(tabPositions[selectedTabIndex])
                            .clip(CircleShape),
                    )
                },
            ) {
                tabs.forEach { tab ->
                    val textResId = when (tab) {
                        Tab.FOR_WOMEN -> R.string.for_women
                        Tab.FOR_MEN -> R.string.for_men
                    }

                    ZarinaButton(
                        onClick = { onTabClicked(tab) },
                        size = ZarinaButtonSize.Medium,
                        colors = ZarinaButtonDefaults.backlessColors(),
                    ) {
                        Text(
                            text = stringResource(textResId).uppercase(),
                            style = UiKitTheme.typographyReworked.tertiary.regular,
                            color = UiKitTheme.colorsReworked.text.general.regular.default,
                        )
                    }
                }
            }
        }
    }

    // TODO: [High] Rename
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun Banners(
        tabs: List<Tab>,
        currentPage: Int,
        banners: HomeBanners,
        modifier: Modifier = Modifier,
    ) {
        val pagerState = rememberPagerState(
            initialPage = currentPage,
            pageCount = { tabs.size },
        )

        LaunchedEffect(currentPage) {
            pagerState.animateScrollToPage(currentPage)
        }

        HorizontalPager(
            state = pagerState,
            beyondBoundsPageCount = 0,
            userScrollEnabled = false,
            modifier = modifier,
        ) { page ->
            val listState = rememberLazyListState()
            val flingBehavior = rememberSnapFlingBehavior(listState)

            val items = when (tabs[page]) {
                Tab.FOR_WOMEN -> banners.womenBanners
                Tab.FOR_MEN -> banners.menBanners
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
