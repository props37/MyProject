package ru.livetyping.zarina.feature.home.ui.impl.impl.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uicompose.pager.rememberPagerConnectedToTabRowState
import ru.livetyping.zarina.core.uicompose.systembars.ForcedSystemBarsBehavior
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.pullrefresh.ZarinaPullRefreshIndicator
import ru.livetyping.zarina.core.uikit.screen.ZarinaLoadingScreen
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.home.domain.model.Banner
import ru.livetyping.zarina.feature.home.domain.model.BannerContainer
import ru.livetyping.zarina.feature.home.domain.model.HomeContent
import ru.livetyping.zarina.feature.home.ui.impl.impl.homecontent.HomeContentEvent
import ru.livetyping.zarina.feature.home.ui.impl.impl.homecontent.HomeContentState
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun HomeContent(
    homeContentState: HomeContentState,
    onHomeContentEvent: (HomeContentEvent) -> Unit,
    genderSelectorState: TabRowState<GenderTab>,
    onGenderSelectorEvent: (TabRowEvent<GenderTab>) -> Unit,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = homeContentState,
        contentKey = { state ->
            when (state) {
                is HomeContentState.Success -> HomeContentKey.Success
                is HomeContentState.Error -> state
                HomeContentState.Loading -> state
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is HomeContentState.Success -> {
                HomeContentSuccess(
                    homeContentState = state,
                    onHomeContentEvent = onHomeContentEvent,
                    genderSelectorState = genderSelectorState,
                    onGenderSelectorEvent = onGenderSelectorEvent,
                    isRefreshing = isRefreshing,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            HomeContentState.Loading -> {
                ZarinaLoadingScreen(modifier = Modifier.fillMaxSize())
            }

            is HomeContentState.Error -> {
                ZarinaErrorScreen(
                    state = state.state,
                    onButtonClicked = {
                        onHomeContentEvent(HomeContentEvent.ErrorRefreshClicked)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(
                            WindowInsets.statusBars
                                .union(WindowInsets.displayCutout)
                        )
                        .bottomNavBarPadding()
                        .padding(16.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HomeContentSuccess(
    homeContentState: HomeContentState.Success,
    onHomeContentEvent: (HomeContentEvent) -> Unit,
    genderSelectorState: TabRowState<GenderTab>,
    onGenderSelectorEvent: (TabRowEvent<GenderTab>) -> Unit,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    Box(modifier = modifier) {
        ForcedSystemBarsBehavior(isStatusBarContentLight = true)

        var pullRefreshOffset by remember {
            mutableStateOf(PullRefreshDefaults.RefreshThreshold)
        }

        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshing,
            onRefresh = { onHomeContentEvent(HomeContentEvent.RefreshTriggered) },
            refreshingOffset = pullRefreshOffset,
        )

        val genderSelectorPagerState = rememberPagerConnectedToTabRowState(
            tabs = genderSelectorState.tabs,
            currentTab = genderSelectorState.currentTab,
            onTabChanged = { onGenderSelectorEvent(TabRowEvent.TabChanged(it)) },
            initialPage = remember { genderSelectorState.currentTabIndex },
            pageCount = { genderSelectorState.tabs.size },
        )

        ZarinaPullRefreshIndicator(
            isRefreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(2f),
        )

        GenderSelector(
            genderSelectorState = genderSelectorState,
            onGenderSelectorEvent = onGenderSelectorEvent,
            modifier = Modifier
                .zIndex(1f)
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .background(rememberTopBarScrimBrush())
                .onSizeChanged { size ->
                    with(density) {
                        pullRefreshOffset = size.height.toDp() - TopBarBottomPadding
                    }
                }
                .statusBarsPadding()
                .padding(top = 12.dp, bottom = TopBarBottomPadding),
        )

        GenderContentPager(
            genderSelectorState = genderSelectorState,
            homeContent = homeContentState.content,
            onHomeContentEvent = onHomeContentEvent,
            pagerState = genderSelectorPagerState,
            modifier = Modifier
                .fillMaxSize()
                .bottomNavBarPadding()
                .pullRefresh(pullRefreshState),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GenderContentPager(
    genderSelectorState: TabRowState<GenderTab>,
    homeContent: HomeContent,
    onHomeContentEvent: (HomeContentEvent) -> Unit,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        HorizontalPager(
            state = pagerState,
            modifier = modifier,
        ) { page ->
            val banners = when (genderSelectorState.tabs[page]) {
                GenderTab.WOMEN -> homeContent.womenBanners
                GenderTab.MEN -> homeContent.menBanners
            }

            BannerPager(
                banners = banners,
                onBannerClicked = {
                    onHomeContentEvent(HomeContentEvent.BannerClicked(it))
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun BannerPager(
    banners: List<BannerContainer>,
    onBannerClicked: (Banner) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState { banners.size }

    var wasScrolled by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.isScrollInProgress }.collect {
            if (it) wasScrolled = true
        }
    }

    val density = LocalDensity.current
    LaunchedEffect(pagerState, density) {
        delay(3.seconds)
        if (!wasScrolled) {
            val scrollValue = with(density) { 80.dp.toPx() }
            val animationSpec = tween<Float>(durationMillis = 500)
            pagerState.animateScrollBy(scrollValue, animationSpec)
            delay(timeMillis = 750)
            pagerState.animateScrollToPage(
                page = pagerState.currentPage,
                animationSpec = animationSpec
            )
        }
    }

    val visibleBannerPagesState = remember(pagerState) {
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
        val isOnScreen by remember(visibleBannerPagesState) {
            derivedStateOf { updatedPage in visibleBannerPagesState.value }
        }

        val banner = banners[page]

        Banner(
            bannerContainer = banner,
            onBannerClicked = onBannerClicked,
            isOnScreen = isOnScreen,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

private val TopBarBottomPadding: Dp get() = 80.dp

private enum class HomeContentKey { Success }
