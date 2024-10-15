package ru.livetyping.zarina.feature.home.ui.impl.impl

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ru.livetyping.zarina.core.ui.compose.Crossfade
import ru.livetyping.zarina.core.ui.compose.pager.rememberPagerConnectedToTabRowState
import ru.livetyping.zarina.core.ui.compose.systembars.ForcedSystemBarsBehavior
import ru.livetyping.zarina.core.ui.kit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.ui.kit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.ui.kit.pullrefresh.ZarinaPullRefreshIndicator
import ru.livetyping.zarina.core.ui.kit.screen.ZarinaLoadingScreen
import ru.livetyping.zarina.feature.home.domain.model.HomeContent
import ru.livetyping.zarina.feature.home.ui.impl.impl.gender.GenderSelectorEvent
import ru.livetyping.zarina.feature.home.ui.impl.impl.gender.GenderSelectorState
import ru.livetyping.zarina.feature.home.ui.impl.impl.gender.GenderTab
import ru.livetyping.zarina.feature.home.ui.impl.impl.homecontent.HomeContentEvent
import ru.livetyping.zarina.feature.home.ui.impl.impl.homecontent.HomeContentState

@Composable
internal fun HomeContent(
    homeContentState: HomeContentState,
    onHomeContentEvent: (HomeContentEvent) -> Unit,
    genderSelectorState: GenderSelectorState,
    onGenderSelectorEvent: (GenderSelectorEvent) -> Unit,
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
    genderSelectorState: GenderSelectorState,
    onGenderSelectorEvent: (GenderSelectorEvent) -> Unit,
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

        val pagerState = rememberPagerConnectedToTabRowState(
            tabs = genderSelectorState.genders,
            currentTab = genderSelectorState.currentGender,
            onTabChanged = {
                onGenderSelectorEvent(GenderSelectorEvent.GenderChanged(it))
            },
            initialPage = remember {
                genderSelectorState.genders.indexOf(genderSelectorState.currentGender)
            },
            pageCount = { genderSelectorState.genders.size },
        )

        ZarinaPullRefreshIndicator(
            isRefreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(2f),
        )

        TopBar(
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
            pagerState = pagerState,
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
    genderSelectorState: GenderSelectorState,
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
            val banners = when (genderSelectorState.genders[page]) {
                GenderTab.WOMEN -> homeContent.womenBanners
                GenderTab.MEN -> homeContent.menBanners
            }

            // TODO: [Top] Implement
        }
    }
}

private val TopBarBottomPadding: Dp get() = 80.dp

private enum class HomeContentKey { Success }
