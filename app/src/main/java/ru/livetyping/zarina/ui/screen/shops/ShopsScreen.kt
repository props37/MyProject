package ru.livetyping.zarina.ui.screen.shops

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.screen.shops.ShopsScreenComponents.TopBar
import ru.livetyping.zarina.ui.screen.shops.ShopsScreenComponents.ViewModePager
import ru.livetyping.zarina.ui.screen.shops.ShopsScreenComponents.ViewModeTabRow
import ru.livetyping.zarina.ui.screen.shops.ShopsViewModel.ShopListState
import ru.livetyping.zarina.ui.screen.shops.ShopsViewModel.ViewMode
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.pager.PagerTabRowIntegration

@Composable
fun ShopsScreen(
    navigate: (ShopsScreenAction) -> Unit,
    viewModel: ShopsViewModel = hiltViewModel(),
) {
    val viewModes by viewModel.viewModes.collectAsStateWithLifecycle()
    val currentViewMode by viewModel.currentViewMode.collectAsStateWithLifecycle()
    val shopListState by viewModel.shopListState.collectAsStateWithLifecycle()

    ScreenContent(
        viewModes = viewModes,
        currentViewMode = currentViewMode,
        onViewModeChanged = viewModel::onViewModeChanged,
        shopListState = shopListState,
        onShopsErrorRefreshClicked = viewModel::onShopsErrorRefreshClicked,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScreenContent(
    viewModes: ImmutableList<ViewMode>,
    currentViewMode: ViewMode,
    onViewModeChanged: (ViewMode) -> Unit,
    shopListState: ShopListState,
    onShopsErrorRefreshClicked: () -> Unit,
    onBackClicked: () -> Unit,
    sideEffects: Flow<ShopsViewModel.SideEffect>,
    navigate: (ShopsScreenAction) -> Unit,
) {
    ShopsScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding(),
    ) {
        TopBar(onBackClicked = onBackClicked)

        val viewModePagerState = rememberPagerState { viewModes.size }

        PagerTabRowIntegration(
            pagerState = viewModePagerState,
            tabs = viewModes,
            currentTab = currentViewMode,
            onCurrentTabChanged = onViewModeChanged,
        )

        ViewModeTabRow(
            viewModes = viewModes,
            currentViewMode = currentViewMode,
            onViewModeChanged = onViewModeChanged,
            viewModePagerState = viewModePagerState,
        )

        ViewModePager(
            viewModes = viewModes,
            pagerState = viewModePagerState,
            shopListState = shopListState,
            onShopsErrorRefreshClicked = onShopsErrorRefreshClicked,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
