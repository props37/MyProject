package ru.livetyping.zarina.feature.home.ui.impl.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ru.livetyping.zarina.core.ui.compose.Crossfade
import ru.livetyping.zarina.core.ui.compose.systembars.ForcedSystemBarsBehavior
import ru.livetyping.zarina.core.ui.kit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.ui.kit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.ui.kit.pullrefresh.ZarinaPullRefreshIndicator
import ru.livetyping.zarina.core.ui.kit.screen.ZarinaLoadingScreen
import ru.livetyping.zarina.feature.home.ui.impl.impl.gender.GenderSelectorEvent
import ru.livetyping.zarina.feature.home.ui.impl.impl.gender.GenderSelectorState
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
    onHomeContentEvent: (HomeContentEvent) -> Unit,
    genderSelectorState: GenderSelectorState,
    onGenderSelectorEvent: (GenderSelectorEvent) -> Unit,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
) {
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

        val pagerState = rememberPagerState(
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

        // TODO: [Top] Implement
    }
}

private enum class HomeContentKey { Success }
