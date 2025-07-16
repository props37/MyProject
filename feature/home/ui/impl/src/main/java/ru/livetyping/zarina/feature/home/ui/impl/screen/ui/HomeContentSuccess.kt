package ru.livetyping.zarina.feature.home.ui.impl.screen.ui

import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberPlatformOverscrollFactory
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.uicompose.pager.rememberPagerStateIntegratedWithTabRow
import ru.livetyping.zarina.core.uicompose.systembars.ForcedSystemBarsBehavior
import ru.livetyping.zarina.core.uikit.pullrefresh.ZarinaPullRefreshIndicator
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.feature.home.domain.model.Banner
import ru.livetyping.zarina.feature.home.domain.model.BannerContainer
import ru.livetyping.zarina.feature.home.domain.model.HomeContent
import ru.livetyping.zarina.feature.home.ui.impl.screen.model.HomeContentState
import ru.livetyping.zarina.feature.home.ui.impl.screen.model.HomeEvent

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun HomeContentSuccess(
    state: HomeContentState.Success,
    onHomeEvent: (HomeEvent) -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    Box(modifier = modifier) {
        ForcedSystemBarsBehavior(isStatusBarContentLight = true)

        var pullRefreshOffset by remember {
            mutableStateOf(PullRefreshDefaults.RefreshThreshold)
        }

        val pullRefreshState = rememberPullRefreshState(
            refreshing = state.isRefreshing,
            onRefresh = { onHomeEvent(HomeEvent.PullRefreshTriggered) },
            refreshingOffset = pullRefreshOffset,
        )

        val genderContentPagerState = rememberPagerStateIntegratedWithTabRow(
            tabs = state.genderPickerState.tabs,
            currentTab = state.genderPickerState.currentTab,
            onTabChanged = { onHomeEvent(HomeEvent.GenderSelected(it)) },
            pageCount = { state.genderPickerState.tabs.size },
        )

        ZarinaPullRefreshIndicator(
            isRefreshing = state.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(2f),
        )

        TopBar(
            genderPickerState = state.genderPickerState,
            onGenderSelected = { onHomeEvent(HomeEvent.GenderSelected(it)) },
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
                .windowInsetsPadding(
                    windowInsetsProvider()
                        .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                )
                .padding(bottom = TopBarBottomPadding),
        )

        GenderContentPager(
            genderTabs = state.genderPickerState.tabs,
            homeContent = state.content,
            onHomeContentEvent = onHomeEvent,
            pagerState = genderContentPagerState,
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
        )
    }
}

@Composable
private fun GenderContentPager(
    genderTabs: ImmutableList<GenderTab>,
    homeContent: HomeContent,
    onHomeContentEvent: (HomeEvent) -> Unit,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(LocalOverscrollFactory provides null) {
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            key = { page -> genderTabs[page] },
            userScrollEnabled = false,
            modifier = modifier,
        ) { page ->
            val banners = when (genderTabs[page]) {
                GenderTab.WOMEN -> homeContent.womenBanners
                GenderTab.MEN -> homeContent.menBanners
            }

            BannerPager(
                banners = banners,
                onBannerClicked = { onHomeContentEvent(HomeEvent.BannerClicked(it)) },
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

    val visibleBannerPagesState = remember(pagerState) {
        derivedStateOf {
            pagerState.layoutInfo.visiblePagesInfo.map { it.index }
        }
    }

    val overscrollFactory = rememberPlatformOverscrollFactory()
    CompositionLocalProvider(LocalOverscrollFactory provides overscrollFactory) {
        VerticalPager(
            state = pagerState,
            key = { page -> banners[page].id.value },
            modifier = modifier,
        ) { page ->
            val currentPage by rememberUpdatedState(page)
            val isOnScreen by remember(visibleBannerPagesState) {
                derivedStateOf { currentPage in visibleBannerPagesState.value }
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
}

private val TopBarBottomPadding: Dp get() = 80.dp
