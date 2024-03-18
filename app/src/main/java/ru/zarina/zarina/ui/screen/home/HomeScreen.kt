package ru.zarina.zarina.ui.screen.home

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.zarina.zarina.domain.content.HomeContent
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.zarina.zarina.ui.common.behavior.systembars.ForcedSystemBarsBehavior
import ru.zarina.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.zarina.zarina.ui.common.component.screen.ZarinaLoadingScreen
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.home.HomeScreenComponents.GenderContentPager
import ru.zarina.zarina.ui.screen.home.HomeScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.home.HomeScreenComponents.rememberTopBarScrimBrush
import ru.zarina.zarina.ui.screen.home.HomeViewModel.ContentState
import ru.zarina.zarina.ui.screen.home.HomeViewModel.GenderTab
import ru.zarina.zarina.ui.screen.home.HomeViewModel.SideEffect
import ru.zarina.zarina.ui.screen.home.tooling.preview.ContentStatePreviewParameterProvider
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.animation.Crossfade
import ru.zarina.zarina.util.compose.pager.PagerTabRowIntegration

@Composable
fun HomeScreen(
    navigateForward: (HomeScreenAction) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val genderTabs by viewModel.genderTabs.collectAsStateWithLifecycle()
    val currentGenderTab by viewModel.currentGenderTab.collectAsStateWithLifecycle()
    val contentState by viewModel.contentState.collectAsStateWithLifecycle()

    ScreenContent(
        genderTabs = genderTabs,
        currentGenderTab = currentGenderTab,
        onGenderTabChanged = viewModel::onGenderTabChanged,
        contentState = contentState,
        onBannerClicked = viewModel::onBannerClicked,
        onContentErrorRefreshClicked = viewModel::onContentErrorRefreshClicked,
        sideEffects = viewModel.sideEffects,
        navigateForward = navigateForward,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScreenContent(
    genderTabs: ImmutableList<GenderTab>,
    currentGenderTab: GenderTab,
    onGenderTabChanged: (GenderTab) -> Unit,
    contentState: ContentState,
    onBannerClicked: (HomeContent.Banner) -> Unit,
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

                        val pagerState = rememberPagerState(
                            initialPage = remember { genderTabs.indexOf(currentGenderTab) },
                            pageCount = { genderTabs.size },
                        )

                        PagerTabRowIntegration(
                            pagerState = pagerState,
                            tabs = genderTabs,
                            currentTab = currentGenderTab,
                            onCurrentTabChanged = onGenderTabChanged,
                        )

                        TopBar(
                            genders = genderTabs,
                            pagerState = pagerState,
                            onGenderChanged = onGenderTabChanged,
                            modifier = Modifier
                                .zIndex(1f)
                                .align(Alignment.TopCenter)
                                .fillMaxWidth()
                                .background(rememberTopBarScrimBrush())
                                .statusBarsPadding()
                                .padding(top = 12.dp, bottom = 80.dp),
                        )

                        GenderContentPager(
                            genders = genderTabs,
                            pagerState = pagerState,
                            content = contentState.content,
                            onBannerClicked = onBannerClicked,
                            modifier = Modifier
                                .fillMaxSize()
                                .bottomNavBarPadding(),
                        )
                    }
                }

                is ContentState.Error -> {
                    ZarinaErrorScreen(
                        state = contentState.errorState,
                        onRefreshClicked = onContentErrorRefreshClicked,
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
@FontScalePreviews
@DensityPreviews
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
            onContentErrorRefreshClicked = {},
            sideEffects = remember { emptyFlow() },
            navigateForward = {},
        )
    }
}
