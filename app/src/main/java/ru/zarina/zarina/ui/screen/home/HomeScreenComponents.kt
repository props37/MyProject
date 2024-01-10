package ru.zarina.zarina.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import coil.compose.AsyncImage
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.common.MediaType
import ru.zarina.zarina.domain.rework.content.HomeContent
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarHeightAsState
import ru.zarina.zarina.ui.common.component.LooseTabRow
import ru.zarina.zarina.ui.common.component.LooseTabRowDefaults.looseTabIndicatorOffset
import ru.zarina.zarina.ui.common.component.VideoPlayer
import ru.zarina.zarina.ui.common.component.ZarinaLogo
import ru.zarina.zarina.ui.common.component.ZarinaLogoAspectRatio
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonSize
import ru.zarina.zarina.ui.common.component.screen.ZarinaLoadingScreen
import ru.zarina.zarina.ui.common.media.exoplayer.LocalExoPlayerCacheHolder
import ru.zarina.zarina.ui.screen.home.HomeViewModel.Tab
import ru.zarina.zarina.ui.theme.UiKitTheme
import timber.log.Timber

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

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ContentPager(
        tabs: List<Tab>,
        currentPage: Int,
        content: HomeContent,
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
            val banners = when (tabs[page]) {
                Tab.FOR_WOMEN -> content.womenBanners
                Tab.FOR_MEN -> content.menBanners
            }

            BannerList(
                banners = banners,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun BannerList(
        banners: List<HomeContent.Banner>,
        modifier: Modifier = Modifier,
    ) {
        val listState = rememberLazyListState()
        val flingBehavior = rememberSnapFlingBehavior(listState)

        val visibleBannersIndicesState = remember {
            derivedStateOf {
                listState.layoutInfo.visibleItemsInfo.map { it.index }
            }
        }

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = modifier,
        ) {
            itemsIndexed(
                items = banners,
                key = { _, banner -> banner.id.value },
                contentType = { _, banner -> createBannerListContentType(banner) },
            ) { index, banner ->
                val updatedIndex by rememberUpdatedState(index)
                val isVisible by remember {
                    derivedStateOf { updatedIndex in visibleBannersIndicesState.value }
                }

                Banner(
                    banner = banner,
                    isVisible = isVisible,
                    modifier = Modifier.fillParentMaxSize(),
                )
            }
        }
    }

    @Composable
    private fun Banner(
        banner: HomeContent.Banner,
        isVisible: Boolean,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier) {
            var isBannerDisplayed by remember(banner) { mutableStateOf(false) }

            when (banner) {
                is HomeContent.Banner.SingleItem -> {
                    FullscreenBanner(
                        banner = banner,
                        isVisible = isVisible,
                        onBannerDisplayed = { isBannerDisplayed = true },
                        modifier = Modifier.matchParentSize(),
                    )
                }

                is HomeContent.Banner.MultipleItems -> {
                    when (banner.viewType) {
                        HomeContent.Banner.MultipleItems.ViewType.GRID -> {
                            GridBanner(
                                banner = banner,
                                onBannerDisplayed = { isBannerDisplayed = true },
                                modifier = Modifier.matchParentSize(),
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = !isBannerDisplayed,
                enter = remember {
                    fadeIn(tween(BannerLoaderAnimationDuration))
                },
                exit = remember {
                    fadeOut(tween(BannerLoaderAnimationDuration))
                },
                modifier = Modifier.matchParentSize(),
            ) {
                ZarinaLoadingScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(UiKitTheme.colorsReworked.background.general.regular.default)
                        // Add bottomNavBar padding at the top to align the loader at the center
                        // of the entire screen
                        .padding(top = bottomNavBarHeightAsState().value),
                )
            }
        }
    }

    @Composable
    private fun FullscreenBanner(
        banner: HomeContent.Banner.SingleItem,
        isVisible: Boolean,
        onBannerDisplayed: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        when (banner.item.mediaType) {
            MediaType.IMAGE -> {
                ImageBanner(
                    bannerItem = banner.item,
                    showTitle = false,
                    onBannerDisplayed = onBannerDisplayed,
                    modifier = modifier,
                )
            }

            MediaType.VIDEO -> {
                VideoBanner(
                    bannerItem = banner.item,
                    isVisible = isVisible,
                    onBannerDisplayed = onBannerDisplayed,
                    modifier = modifier,
                )
            }
        }
    }

    @Composable
    private fun GridBanner(
        banner: HomeContent.Banner.MultipleItems,
        onBannerDisplayed: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            val items = remember(banner.items) {
                List(GridBannerItemCount) { index -> banner.items.getOrNull(index) }
            }

            items.chunked(GridBannerRowItemCount).forEach { rowItems ->
                Row(modifier = Modifier.weight(1f)) {
                    val itemModifier = remember {
                        Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    }

                    rowItems.forEach { item ->
                        when (item?.mediaType) {
                            MediaType.IMAGE -> {
                                ImageBanner(
                                    bannerItem = item,
                                    showTitle = true,
                                    onBannerDisplayed = onBannerDisplayed,
                                    modifier = itemModifier,
                                )
                            }

                            MediaType.VIDEO -> {
                                SideEffect {
                                    Timber.w("Video banners are not supported in Grid view")
                                }
                                Box(modifier = itemModifier)
                            }

                            null -> {
                                Box(modifier = itemModifier)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ImageBanner(
        bannerItem: HomeContent.Banner.Item,
        showTitle: Boolean,
        onBannerDisplayed: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier) {
            AsyncImage(
                model = bannerItem.mediaUrl.value,
                contentDescription = bannerItem.title,
                contentScale = ContentScale.Crop,
                onSuccess = { onBannerDisplayed() },
                modifier = Modifier.matchParentSize(),
            )

            if (showTitle) {
                Text(
                    text = bannerItem.title?.uppercase().orEmpty(),
                    style = UiKitTheme.typographyReworked.tertiary.regular,
                    color = UiKitTheme.colorsReworked.text.general.inversed.default,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                        .padding(horizontal = 12.dp),
                )
            }
        }
    }

    @androidx.annotation.OptIn(UnstableApi::class)
    @Composable
    private fun VideoBanner(
        bannerItem: HomeContent.Banner.Item,
        isVisible: Boolean,
        onBannerDisplayed: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val context = LocalContext.current

        val updatedOnBannerDisplayed by rememberUpdatedState(onBannerDisplayed)
        val exoPlayer = remember(context) {
            ExoPlayer.Builder(context)
                .build()
                .apply {
                    repeatMode = Player.REPEAT_MODE_ONE

                    val listener = object : Player.Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            if (playbackState == Player.STATE_READY) {
                                updatedOnBannerDisplayed()
                            }
                        }
                    }
                    addListener(listener)
                }
        }

        // Release ExoPlayer when it is no longer needed
        DisposableEffect(exoPlayer) {
            onDispose { exoPlayer.release() }
        }

        // Set media to ExoPlayer
        val mediaUrl = bannerItem.mediaUrl.value
        val cacheDataSourceFactory = LocalExoPlayerCacheHolder.current?.cacheDataSourceFactory
        LaunchedEffect(exoPlayer, mediaUrl, cacheDataSourceFactory) {
            val dataSourceFactory = cacheDataSourceFactory ?: run {
                Timber.w("CacheDataSource factory is null. Use fallback DataSource factory instead")
                DefaultHttpDataSource.Factory()
            }
            val mediaItem = MediaItem.fromUri(mediaUrl)
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaItem)
            exoPlayer.setMediaSource(mediaSource)
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

    private fun createBannerListContentType(banner: HomeContent.Banner): String {
        return when (banner) {
            is HomeContent.Banner.SingleItem -> {
                when (banner.item.mediaType) {
                    MediaType.IMAGE -> BannerListContentTypeFullscreenImage
                    MediaType.VIDEO -> BannerListContentTypeFullscreenVideo
                }
            }

            is HomeContent.Banner.MultipleItems -> {
                when (banner.viewType) {
                    HomeContent.Banner.MultipleItems.ViewType.GRID -> BannerListContentTypeGrid
                }
            }
        }
    }

    private const val BannerListContentTypeFullscreenImage = "BannerListContentTypeFullscreenImage"
    private const val BannerListContentTypeFullscreenVideo = "BannerListContentTypeFullscreenVideo"
    private const val BannerListContentTypeGrid = "BannerListContentTypeGrid"

    private const val BannerLoaderAnimationDuration = 250

    private const val GridBannerItemCount = 4
    private const val GridBannerRowItemCount = GridBannerItemCount / 2
}
