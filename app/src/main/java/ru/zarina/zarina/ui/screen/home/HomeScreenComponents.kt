package ru.zarina.zarina.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.FloatState
import androidx.compose.runtime.IntState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.asFloatState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Velocity
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
import kotlinx.collections.immutable.ImmutableList
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.common.MediaType
import ru.zarina.zarina.domain.rework.content.HomeContent
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarHeightAsState
import ru.zarina.zarina.ui.common.component.LooseTabRow
import ru.zarina.zarina.ui.common.component.LooseTabRowDefaults.looseTabIndicatorOffset
import ru.zarina.zarina.ui.common.component.VideoPlayer
import ru.zarina.zarina.ui.common.component.ZarinaLogo
import ru.zarina.zarina.ui.common.component.ZarinaLogoAspectRatio
import ru.zarina.zarina.ui.common.component.ZarinaTabIndicator
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonSize
import ru.zarina.zarina.ui.common.component.screen.ZarinaLoadingScreen
import ru.zarina.zarina.ui.common.media.exoplayer.LocalExoPlayerCacheHolder
import ru.zarina.zarina.ui.screen.home.HomeViewModel.GenderTab
import ru.zarina.zarina.ui.theme.UiKitTheme
import timber.log.Timber
import kotlin.math.roundToInt

object HomeScreenComponents {

    @Composable
    fun GenderPicker(
        genders: ImmutableList<GenderTab>,
        currentGender: GenderTab,
        onGenderClicked: (GenderTab) -> Unit,
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

            Spacer(modifier = Modifier.height(12.dp))

            val selectedTabIndex = remember(genders, currentGender) {
                genders.indexOf(currentGender)
            }
            // TODO: [Medium] Extract?
            LooseTabRow(
                selectedTabIndex = selectedTabIndex,
                indicator = { tabPositions ->
                    ZarinaTabIndicator(
                        modifier = Modifier.looseTabIndicatorOffset(tabPositions[selectedTabIndex]),
                    )
                },
            ) {
                genders.forEach { gender ->
                    ZarinaButton(
                        onClick = { onGenderClicked(gender) },
                        size = ZarinaButtonSize.Medium,
                        colors = ZarinaButtonDefaults.backlessColors(),
                    ) {
                        val textResId = when (gender) {
                            GenderTab.WOMEN -> R.string.for_women
                            GenderTab.MEN -> R.string.for_men
                        }

                        val style = if (gender == currentGender) {
                            UiKitTheme.typographyReworked.tertiary.regular
                        } else {
                            UiKitTheme.typographyReworked.tertiary.light
                        }

                        Text(
                            text = stringResource(textResId).uppercase(),
                            style = style,
                            color = UiKitTheme.colorsReworked.text.general.regular.default,
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun GenderContentPager(
        genders: ImmutableList<GenderTab>,
        currentGender: GenderTab,
        content: HomeContent,
        onBannerClicked: (HomeContent.Banner) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val pagerState = rememberPagerState(
            initialPage = remember { genders.indexOf(currentGender) },
            pageCount = { genders.size },
        )

        LaunchedEffect(pagerState, genders, currentGender) {
            val page = genders.indexOf(currentGender)
            pagerState.animateScrollToPage(page)
        }

        HorizontalPager(
            state = pagerState,
            beyondBoundsPageCount = 0,
            userScrollEnabled = false,
            modifier = modifier,
        ) { page ->
            val banners = when (genders[page]) {
                GenderTab.WOMEN -> content.womenBanners
                GenderTab.MEN -> content.menBanners
            }

            BannerList(
                banners = banners,
                onBannerClicked = onBannerClicked,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun BannerList(
        banners: List<HomeContent.BannerContainer>,
        onBannerClicked: (HomeContent.Banner) -> Unit,
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
                key = { _, bannerContainer -> bannerContainer.id.value },
                contentType = { _, bannerContainer ->
                    createBannerListContentType(bannerContainer)
                },
            ) { index, bannerContainer ->
                val updatedIndex by rememberUpdatedState(index)
                val isVisible by remember {
                    derivedStateOf { updatedIndex in visibleBannersIndicesState.value }
                }

                Banner(
                    bannerContainer = bannerContainer,
                    onBannerClicked = onBannerClicked,
                    isVisible = isVisible,
                    modifier = Modifier.fillParentMaxSize(),
                )
            }
        }
    }

    @Composable
    private fun Banner(
        bannerContainer: HomeContent.BannerContainer,
        onBannerClicked: (HomeContent.Banner) -> Unit,
        isVisible: Boolean,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier) {
            var isBannerDisplayed by remember(bannerContainer) { mutableStateOf(false) }

            when (bannerContainer) {
                is HomeContent.BannerContainer.SingleBanner -> {
                    FullscreenBanner(
                        bannerContainer = bannerContainer,
                        onBannerClicked = onBannerClicked,
                        isVisible = isVisible,
                        onBannerDisplayed = { isBannerDisplayed = true },
                        modifier = Modifier.matchParentSize(),
                    )
                }

                is HomeContent.BannerContainer.MultipleBanners -> {
                    when (bannerContainer.arrangement) {
                        HomeContent.BannerContainer.MultipleBanners.Arrangement.GRID -> {
                            GridBanners(
                                bannerContainer = bannerContainer,
                                onBannerClicked = onBannerClicked,
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
        bannerContainer: HomeContent.BannerContainer.SingleBanner,
        onBannerClicked: (HomeContent.Banner) -> Unit,
        isVisible: Boolean,
        onBannerDisplayed: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        when (bannerContainer.banner.mediaType) {
            MediaType.IMAGE -> {
                ImageBanner(
                    banner = bannerContainer.banner,
                    onBannerClicked = onBannerClicked,
                    showTitle = false,
                    onBannerDisplayed = onBannerDisplayed,
                    modifier = modifier,
                )
            }

            MediaType.VIDEO -> {
                VideoBanner(
                    banner = bannerContainer.banner,
                    onBannerClicked = onBannerClicked,
                    isVisible = isVisible,
                    onBannerDisplayed = onBannerDisplayed,
                    modifier = modifier,
                )
            }
        }
    }

    @Composable
    private fun GridBanners(
        bannerContainer: HomeContent.BannerContainer.MultipleBanners,
        onBannerClicked: (HomeContent.Banner) -> Unit,
        onBannerDisplayed: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            val banners = remember(bannerContainer.banners) {
                List(GridBannersItemCount) { index -> bannerContainer.banners.getOrNull(index) }
            }

            banners.chunked(GridBannersRowItemCount).forEach { rowBanners ->
                Row(modifier = Modifier.weight(1f)) {
                    val bannerModifier = remember {
                        Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    }

                    rowBanners.forEach { banner ->
                        when (banner?.mediaType) {
                            MediaType.IMAGE -> {
                                ImageBanner(
                                    banner = banner,
                                    onBannerClicked = onBannerClicked,
                                    showTitle = true,
                                    onBannerDisplayed = onBannerDisplayed,
                                    modifier = bannerModifier,
                                )
                            }

                            MediaType.VIDEO -> {
                                SideEffect {
                                    Timber.w("Video banners are not supported in Grid view")
                                }
                                Box(modifier = bannerModifier)
                            }

                            null -> {
                                Box(modifier = bannerModifier)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ImageBanner(
        banner: HomeContent.Banner,
        onBannerClicked: (HomeContent.Banner) -> Unit,
        showTitle: Boolean,
        onBannerDisplayed: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Box(
            modifier = modifier
                .clickable(
                    enabled = banner.clickAction != null,
                    onClick = { onBannerClicked(banner) },
                ),
        ) {
            AsyncImage(
                model = banner.mediaUrl.value,
                contentDescription = banner.title,
                contentScale = ContentScale.Crop,
                onSuccess = { onBannerDisplayed() },
                modifier = Modifier.matchParentSize(),
            )

            if (showTitle) {
                Text(
                    text = banner.title?.uppercase().orEmpty(),
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
        banner: HomeContent.Banner,
        onBannerClicked: (HomeContent.Banner) -> Unit,
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
        val mediaUrl = banner.mediaUrl.value
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
            modifier = modifier
                .clickable(
                    enabled = banner.clickAction != null,
                    onClick = { onBannerClicked(banner) },
                ),
        )
    }

    @Composable
    fun rememberTabBarScrollBehavior(): TabBarScrollBehavior {
        return remember { TabBarScrollBehavior() }
    }

    private fun createBannerListContentType(bannerContainer: HomeContent.BannerContainer): String {
        return when (bannerContainer) {
            is HomeContent.BannerContainer.SingleBanner -> {
                when (bannerContainer.banner.mediaType) {
                    MediaType.IMAGE -> BannerListContentTypeFullscreenImage
                    MediaType.VIDEO -> BannerListContentTypeFullscreenVideo
                }
            }

            is HomeContent.BannerContainer.MultipleBanners -> {
                when (bannerContainer.arrangement) {
                    HomeContent.BannerContainer.MultipleBanners.Arrangement.GRID ->
                        BannerListContentTypeGrid
                }
            }
        }
    }

    // TODO: [Medium] Add ability to disable scroll, e.g. if the list is empty
    @Stable
    class TabBarScrollBehavior {
        private val height = mutableIntStateOf(0)

        private val _yOffset = mutableIntStateOf(TabBarMaxYOffset)
        val yOffset: IntState = _yOffset

        val alpha: FloatState = derivedStateOf {
            val scrollProgress = -yOffset.intValue.toFloat() / height.intValue
            1f - scrollProgress * TabBarAlphaProgressFactor
        }.asFloatState()

        val nestedScrollConnection = object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                val minYOffset = -height.intValue
                val newTabBarYOffset = (yOffset.intValue + available.y.roundToInt())
                    .coerceIn(minYOffset, TabBarMaxYOffset)
                _yOffset.intValue = newTabBarYOffset
                return super.onPreScroll(available, source)
            }

            override suspend fun onPostFling(
                consumed: Velocity,
                available: Velocity,
            ): Velocity {
                // Settle tab bar if it is between states
                val minYOffset = -height.intValue
                val targetYOffset =
                    if (yOffset.intValue <= minYOffset / 2) minYOffset else TabBarMaxYOffset
                AnimationState(
                    initialValue = yOffset.intValue,
                    typeConverter = Int.VectorConverter,
                ).animateTo(targetValue = targetYOffset) {
                    _yOffset.intValue = value
                }
                return super.onPostFling(consumed, available)
            }
        }

        fun onTabBarHeightChanged(height: Int) {
            this.height.intValue = height
        }
    }

    private const val BannerListContentTypeFullscreenImage = "BannerListContentTypeFullscreenImage"
    private const val BannerListContentTypeFullscreenVideo = "BannerListContentTypeFullscreenVideo"
    private const val BannerListContentTypeGrid = "BannerListContentTypeGrid"

    private const val BannerLoaderAnimationDuration = 250

    private const val GridBannersItemCount = 4
    private const val GridBannersRowItemCount = GridBannersItemCount / 2

    private const val TabBarMaxYOffset = 0
    private const val TabBarAlphaProgressFactor = 2
}
