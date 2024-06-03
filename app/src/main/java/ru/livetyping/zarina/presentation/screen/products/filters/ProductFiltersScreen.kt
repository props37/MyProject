package ru.livetyping.zarina.presentation.screen.products.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.filter.Filter
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.products.filters.ProductFiltersScreenComponents.FilterList
import ru.livetyping.zarina.presentation.screen.products.filters.ProductFiltersScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.products.filters.ProductFiltersScreenComponents.TopBarActions
import ru.livetyping.zarina.presentation.screen.products.filters.ProductFiltersViewModel.FilterListState
import ru.livetyping.zarina.presentation.screen.products.filters.ProductFiltersViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun ProductFiltersScreen(
    navigateForward: (ProductFiltersScreenAction) -> Unit,
    navigateBackward: (ProductFiltersScreenResult) -> Unit,
    viewModel: ProductFiltersViewModel = hiltViewModel(),
) {
    val filterListState by viewModel.filterListState.collectAsStateWithLifecycle()
    val isPickupStoresFilterVisible by viewModel.isPickupStoresFilterVisible.collectAsStateWithLifecycle()
    val isResetButtonVisible by viewModel.isResetButtonVisible.collectAsStateWithLifecycle()
    val productCount by viewModel.productCount.collectAsStateWithLifecycle()
    val topBarActions = remember(viewModel) {
        TopBarActions(
            onBackClicked = viewModel::onBackClicked,
            onResetClicked = viewModel::onResetClicked,
        )
    }

    ScreenContent(
        filterListState = filterListState,
        isPickupStoresFilterVisible = isPickupStoresFilterVisible,
        isResetButtonVisible = isResetButtonVisible,
        topBarActions = topBarActions,
        onFilterChanged = viewModel::onFilterChanged,
        onFilterClicked = viewModel::onFilterClicked,
        productCount = productCount,
        onShowProductsClicked = viewModel::onShowProductsClicked,
        onFilterListErrorRefreshClicked = viewModel::onFilterListErrorRefreshClicked,
        sideEffects = viewModel.sideEffects,
        navigateForward = navigateForward,
        navigateBackward = navigateBackward,
    )
}

@Composable
private fun ScreenContent(
    filterListState: FilterListState,
    isPickupStoresFilterVisible: Boolean,
    isResetButtonVisible: Boolean,
    topBarActions: TopBarActions,
    onFilterChanged: (Filter) -> Unit,
    onFilterClicked: (Filter) -> Unit,
    productCount: Int?,
    onShowProductsClicked: () -> Unit,
    onFilterListErrorRefreshClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigateForward: (ProductFiltersScreenAction) -> Unit,
    navigateBackward: (ProductFiltersScreenResult) -> Unit,
) {
    ProductFiltersScreenBehavior(
        sideEffects = sideEffects,
        navigateForward = navigateForward,
        navigateBackward = navigateBackward,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.systemBars
                    .union(WindowInsets.displayCutout),
            ),
    ) {
        TopBar(
            isResetButtonVisible = isResetButtonVisible,
            actions = topBarActions,
        )

        FilterList(
            state = filterListState,
            isPickupStoresFilterVisible = isPickupStoresFilterVisible,
            onFilterChanged = onFilterChanged,
            onFilterClicked = onFilterClicked,
            onShowProductsClicked = onShowProductsClicked,
            productCount = productCount,
            onFilterListErrorRefreshClicked = onFilterListErrorRefreshClicked,
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
