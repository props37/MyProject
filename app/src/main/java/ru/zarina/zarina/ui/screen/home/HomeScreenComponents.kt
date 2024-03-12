package ru.zarina.zarina.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.common.MediaType
import ru.zarina.zarina.domain.content.HomeContent
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarHeightAsState
import ru.zarina.zarina.ui.common.component.ZarinaLogo
import ru.zarina.zarina.ui.common.component.ZarinaLogoAspectRatio
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonSize
import ru.zarina.zarina.ui.common.component.media.VideoPlayer
import ru.zarina.zarina.ui.common.component.screen.ZarinaLoadingScreen
import ru.zarina.zarina.ui.common.component.tab.LooseTabRowDefaults.looseTabIndicatorOffset
import ru.zarina.zarina.ui.common.component.tab.ZarinaLooseTabRow
import ru.zarina.zarina.ui.common.component.tab.ZarinaTabIndicator
import ru.zarina.zarina.ui.screen.home.HomeViewModel.GenderTab
import ru.zarina.zarina.ui.theme.UiKitTheme
import timber.log.Timber

object HomeScreenComponents {

    @Composable
    fun TopBar(
        genders: ImmutableList<GenderTab>,
        currentGender: GenderTab,
        onGenderClicked: (GenderTab) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val color = UiKitTheme.colors.text.general.inversed.default
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier,
        ) {
            ZarinaLogo(
                color = color,
                modifier = Modifier
                    .width(140.dp)
                    .aspectRatio(ZarinaLogoAspectRatio),
            )

            Spacer(modifier = Modifier.height(12.dp))

            val selectedTabIndex = remember(genders, currentGender) {
                genders.indexOf(currentGender)
            }
            ZarinaLooseTabRow(
                selectedTabIndex = selectedTabIndex,
                indicator = { tabPositions ->
                    ZarinaTabIndicator(
                        color = color,
                        modifier = Modifier.looseTabIndicatorOffset(tabPositions[selectedTabIndex]),
                    )
                },
            ) {
                genders.forEach { gender ->
                    ZarinaButton(
                        onClick = { onGenderClicked(gender) },
                        size = ZarinaButtonSize.Medium,
                        colors = ZarinaButtonDefaults.backlessColors(contentColor = color),
                    ) {
                        val textResId = when (gender) {
                            GenderTab.WOMEN -> R.string.for_women
                            GenderTab.MEN -> R.string.for_men
                        }

                        val style = if (gender == currentGender) {
                            UiKitTheme.typography.tertiary.regular
                        } else {
                            UiKitTheme.typography.tertiary.light
                        }

                        Text(
                            text = stringResource(textResId).uppercase(),
                            style = style,
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

            BannerPager(
                banners = banners,
                onBannerClicked = onBannerClicked,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun BannerPager(
        banners: List<HomeContent.BannerContainer>,
        onBannerClicked: (HomeContent.Banner) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val pagerState = rememberPagerState { banners.size }

        val visibleBannersPagesState = remember {
            derivedStateOf {
                pagerState.layoutInfo.visiblePagesInfo.map { it.index }
            }
        }

        VerticalPager(
            state = pagerState,
            key = { page -> banners[page].id.value },
            modifier = modifier,
        ) { page ->
            val updatedPage by rememberUpdatedState(page)
            val isOnScreen by remember {
                derivedStateOf { updatedPage in visibleBannersPagesState.value }
            }

            val bannerContainer = banners[page]

            Banner(
                bannerContainer = bannerContainer,
                onBannerClicked = onBannerClicked,
                isOnScreen = isOnScreen,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    @Composable
    private fun Banner(
        bannerContainer: HomeContent.BannerContainer,
        onBannerClicked: (HomeContent.Banner) -> Unit,
        isOnScreen: Boolean,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier) {
            var isBannerDisplayed by remember(bannerContainer) { mutableStateOf(false) }

            when (bannerContainer) {
                is HomeContent.BannerContainer.SingleBanner -> {
                    FullscreenBanner(
                        bannerContainer = bannerContainer,
                        onBannerClicked = onBannerClicked,
                        isOnScreen = isOnScreen,
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
                        .background(UiKitTheme.colors.background.general.regular.default)
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
        isOnScreen: Boolean,
        onBannerDisplayed: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        when (bannerContainer.banner.media.type) {
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
                    isOnScreen = isOnScreen,
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
                        when (banner?.media?.type) {
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
                model = banner.media.url.value,
                contentDescription = banner.title,
                contentScale = ContentScale.Crop,
                onSuccess = { onBannerDisplayed() },
                modifier = Modifier.matchParentSize(),
            )

            if (showTitle) {
                Text(
                    text = banner.title?.uppercase().orEmpty(),
                    style = UiKitTheme.typography.tertiary.regular,
                    color = UiKitTheme.colors.text.general.inversed.default,
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
        isOnScreen: Boolean,
        onBannerDisplayed: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        VideoPlayer(
            url = banner.media.url,
            isOnScreen = isOnScreen,
            onReadyToPlay = onBannerDisplayed,
            modifier = modifier
                .clickable(
                    enabled = banner.clickAction != null,
                    onClick = { onBannerClicked(banner) },
                )
        )
    }

    @Composable
    fun rememberTopBarScrimBrush(): Brush {
        val scrimColor = UiKitTheme.colors.background.general.inversed.default
        return remember(scrimColor) {
            val colors = listOf(
                scrimColor.copy(alpha = TopBarScrimAlpha),
                Color.Transparent,
            )
            Brush.verticalGradient(colors)
        }
    }

    private const val BannerLoaderAnimationDuration = 250

    private const val GridBannersItemCount = 4
    private const val GridBannersRowItemCount = GridBannersItemCount / 2

    private const val TopBarScrimAlpha = 0.24f
}
