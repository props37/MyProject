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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.zarina.zarina.ui.common.component.screen.ZarinaLoadingScreen
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.home.HomeScreenComponents.Banners
import ru.zarina.zarina.ui.screen.home.HomeScreenComponents.TabBar
import ru.zarina.zarina.ui.screen.home.HomeViewModel.BannersState
import ru.zarina.zarina.ui.screen.home.HomeViewModel.Tab
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.Crossfade

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val tabs by viewModel.tabs.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val bannersState by viewModel.bannersState.collectAsStateWithLifecycle()

    ScreenContent(
        tabs = tabs,
        currentTab = currentTab,
        onTabClicked = viewModel::onTabClicked,
        bannersState = bannersState,
        onBannersErrorRefreshClicked = viewModel::onBannersErrorRefreshClicked,
    )
}

@Composable
private fun ScreenContent(
    tabs: List<Tab>,
    currentTab: Tab,
    onTabClicked: (Tab) -> Unit,
    bannersState: BannersState,
    onBannersErrorRefreshClicked: () -> Unit,
) {
    ForcedBottomNavBarBehavior(isVisible = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colorsReworked.background.general.regular.default),
    ) {
        Crossfade(
            targetState = bannersState,
            modifier = Modifier.fillMaxSize(),
        ) { bannersState ->
            when (bannersState) {
                BannersState.Loading -> {
                    ZarinaLoadingScreen(modifier = Modifier.fillMaxSize())
                }

                is BannersState.Success -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        TabBar(
                            tabs = tabs,
                            currentTab = currentTab,
                            onTabClicked = onTabClicked,
                            modifier = Modifier
                                .zIndex(1f)
                                .align(Alignment.TopCenter)
                                .statusBarsPadding()
                                .padding(top = 16.dp),
                        )

                        Banners(
                            pageCount = tabs.size,
                            currentPage = tabs.indexOf(currentTab),
                            banners = bannersState.banners,
                            modifier = Modifier
                                .fillMaxSize()
                                .bottomNavBarPadding(),
                        )
                    }
                }

                is BannersState.Error -> {
                    ZarinaErrorScreen(
                        state = bannersState.errorState,
                        onRefreshClicked = onBannersErrorRefreshClicked,
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
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [High] Add preview
    }
}
