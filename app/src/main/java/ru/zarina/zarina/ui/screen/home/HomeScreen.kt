package ru.zarina.zarina.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
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
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.zarina.zarina.ui.common.component.screen.ZarinaLoadingScreen
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.home.HomeScreenComponents.ContentPager
import ru.zarina.zarina.ui.screen.home.HomeScreenComponents.TabBar
import ru.zarina.zarina.ui.screen.home.HomeViewModel.ContentState
import ru.zarina.zarina.ui.screen.home.HomeViewModel.Tab
import ru.zarina.zarina.ui.screen.home.tooling.preview.ContentStatePreviewParameterProvider
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.Crossfade

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val tabs by viewModel.tabs.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val contentState by viewModel.contentState.collectAsStateWithLifecycle()

    ScreenContent(
        tabs = tabs,
        currentTab = currentTab,
        onTabClicked = viewModel::onTabClicked,
        contentState = contentState,
        onContentErrorRefreshClicked = viewModel::onContentErrorRefreshClicked,
    )
}

@Composable
private fun ScreenContent(
    tabs: List<Tab>,
    currentTab: Tab,
    onTabClicked: (Tab) -> Unit,
    contentState: ContentState,
    onContentErrorRefreshClicked: () -> Unit,
) {
    ForcedBottomNavBarBehavior(isVisible = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colorsReworked.background.general.regular.default),
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
                        // TODO: [Medium] Hide TabBar on scroll
                        TabBar(
                            tabs = tabs,
                            currentTab = currentTab,
                            onTabClicked = onTabClicked,
                            modifier = Modifier
                                .zIndex(1f)
                                .align(Alignment.TopCenter)
                                .statusBarsPadding()
                                .padding(top = 12.dp),
                        )

                        ContentPager(
                            tabs = tabs,
                            currentPage = tabs.indexOf(currentTab),
                            content = contentState.content,
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
            tabs = remember { Tab.entries.toList() },
            currentTab = Tab.FOR_WOMEN,
            onTabClicked = {},
            contentState = contentState,
            onContentErrorRefreshClicked = {},
        )
    }
}
