package ru.livetyping.zarina.presentation.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.domain.content.HomeContent
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.behavior.systembars.ForcedSystemBarsBehavior
import ru.livetyping.zarina.presentation.common.component.pullrefresh.ZarinaPullRefreshIndicator
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaLoadingScreen
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.home.HomeScreenComponents.GenderContentPager
import ru.livetyping.zarina.presentation.screen.home.HomeScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.home.HomeScreenComponents.rememberTopBarScrimBrush
import ru.livetyping.zarina.presentation.screen.home.HomeViewModel.ContentState
import ru.livetyping.zarina.presentation.screen.home.HomeViewModel.GenderTab
import ru.livetyping.zarina.presentation.screen.home.HomeViewModel.SideEffect
import ru.livetyping.zarina.presentation.screen.home.tooling.preview.ContentStatePreviewParameterProvider
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade
import ru.livetyping.zarina.util.compose.pager.PagerTabRowIntegration

@Composable
fun HomeScreen(
    navigateForward: (HomeScreenAction) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val genderTabs by viewModel.genderTabs.collectAsStateWithLifecycle()
    val currentGenderTab by viewModel.currentGenderTab.collectAsStateWithLifecycle()
    val contentState by viewModel.contentState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    ScreenContent(
        genderTabs = genderTabs,
        currentGenderTab = currentGenderTab,
        onGenderTabChanged = viewModel::onGenderTabChanged,
        contentState = contentState,
        onBannerClicked = viewModel::onBannerClicked,
        isRefreshing = isRefreshing,
        onRefreshTriggered = viewModel::onRefreshTriggered,
        onContentErrorRefreshClicked = viewModel::onContentErrorRefreshClicked,
        sideEffects = viewModel.sideEffects,
        navigateForward = navigateForward,
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun ScreenContent(
    genderTabs: ImmutableList<GenderTab>,
    currentGenderTab: GenderTab,
    onGenderTabChanged: (GenderTab) -> Unit,
    contentState: ContentState,
    onBannerClicked: (HomeContent.Banner) -> Unit,
    isRefreshing: Boolean,
    onRefreshTriggered: () -> Unit,
    onContentErrorRefreshClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigateForward: (HomeScreenAction) -> Unit,
) {
    HomeScreenBehavior(
        sideEffects = sideEffects,
        navigateForward = navigateForward,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default),
    ) {
        Crossfade(
            targetState = contentState,
            modifier = Modifier.fillMaxSize(),
        ) { contentState ->
            when (contentState) {
                ContentState.Loading -> {
                    ZarinaLoadingScreen(modifier = Modifier.fillMaxSize())
                }

                is ContentState.Success -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        ForcedSystemBarsBehavior(isStatusBarContentLight = true)

                        val density = LocalDensity.current
                        var pullRefreshOffset by remember {
                            mutableStateOf(PullRefreshDefaults.RefreshThreshold)
                        }

                        val pullRefreshState = rememberPullRefreshState(
                            refreshing = isRefreshing,
                            onRefresh = onRefreshTriggered,
                            refreshingOffset = pullRefreshOffset,
                        )

                        val pagerState = rememberPagerState(
                            initialPage = remember { genderTabs.indexOf(currentGenderTab) },
                            pageCount = { genderTabs.size },
                        )

                        ZarinaPullRefreshIndicator(
                            refreshing = isRefreshing,
                            state = pullRefreshState,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .zIndex(2f),
                        )

                        PagerTabRowIntegration(
                            pagerState = pagerState,
                            tabs = genderTabs,
                            currentTab = currentGenderTab,
                            onCurrentTabChanged = onGenderTabChanged,
                        )

                        val topBarBottomPadding = 80.dp
                        TopBar(
                            genders = genderTabs,
                            pagerState = pagerState,
                            onGenderChanged = onGenderTabChanged,
                            modifier = Modifier
                                .zIndex(1f)
                                .align(Alignment.TopCenter)
                                .fillMaxWidth()
                                .background(rememberTopBarScrimBrush())
                                .onSizeChanged {
                                    with(density) {
                                        pullRefreshOffset = it.height.toDp() - topBarBottomPadding
                                    }
                                }
                                .statusBarsPadding()
                                .padding(top = 12.dp, bottom = topBarBottomPadding),
                        )

                        GenderContentPager(
                            genders = genderTabs,
                            pagerState = pagerState,
                            content = contentState.content,
                            onBannerClicked = onBannerClicked,
                            modifier = Modifier
                                .fillMaxSize()
                                .bottomNavBarPadding()
                                .pullRefresh(pullRefreshState),
                        )
                    }
                }

                is ContentState.Error -> {
                    ZarinaErrorScreen(
                        state = contentState.errorState,
                        onButtonClicked = onContentErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(
                                WindowInsets.statusBars
                                    .union(WindowInsets.displayCutout),
                            )
                            .bottomNavBarPadding()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp),
                    )
                }
            }
        }
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview(
    @PreviewParameter(ContentStatePreviewParameterProvider::class)
    contentState: ContentState,
) {
    ZarinaPreview {
        ScreenContent(
            genderTabs = remember { GenderTab.entries.toImmutableList() },
            currentGenderTab = GenderTab.WOMEN,
            onGenderTabChanged = {},
            contentState = contentState,
            onBannerClicked = {},
            isRefreshing = false,
            onRefreshTriggered = {},
            onContentErrorRefreshClicked = {},
            sideEffects = remember { emptyFlow() },
            navigateForward = {},
        )
    }
}
