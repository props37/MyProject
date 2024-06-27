package ru.livetyping.zarina.presentation.screen.productsearch.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.filter.Filter
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.filters.FilterListState
import ru.livetyping.zarina.presentation.screen.filters.FiltersScreenContent
import ru.livetyping.zarina.presentation.screen.productsearch.filters.ProductSearchFiltersViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun ProductSearchFiltersScreen(
    navigateForward: (ProductSearchFiltersScreenAction) -> Unit,
    navigateBackward: (ProductSearchFiltersScreenResult) -> Unit,
    viewModel: ProductSearchFiltersViewModel = hiltViewModel(),
) {
    val filterListState by viewModel.filterListState.collectAsStateWithLifecycle()
    val isPickupStoresFilterVisible by viewModel.isPickupStoresFilterVisible.collectAsStateWithLifecycle()
    val isResetFiltersButtonVisible by viewModel.isResetFiltersButtonVisible.collectAsStateWithLifecycle()
    val productCount by viewModel.productCount.collectAsStateWithLifecycle()

    ScreenContent(
        filterListState = filterListState,
        isPickupStoresFilterVisible = isPickupStoresFilterVisible,
        isResetFiltersButtonVisible = isResetFiltersButtonVisible,
        onResetFiltersClicked = viewModel::onResetFiltersClicked,
        onBackClicked = viewModel::onBackClicked,
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
    isResetFiltersButtonVisible: Boolean,
    onResetFiltersClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onFilterChanged: (Filter) -> Unit,
    onFilterClicked: (Filter) -> Unit,
    productCount: Int?,
    onShowProductsClicked: () -> Unit,
    onFilterListErrorRefreshClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigateForward: (ProductSearchFiltersScreenAction) -> Unit,
    navigateBackward: (ProductSearchFiltersScreenResult) -> Unit,
) {
    ProductSearchFiltersScreenBehavior(
        sideEffects = sideEffects,
        navigateForward = navigateForward,
        navigateBackward = navigateBackward,
    )

    FiltersScreenContent(
        filterListState = filterListState,
        isResetFiltersButtonVisible = isResetFiltersButtonVisible,
        onResetFiltersClicked = onResetFiltersClicked,
        onBackClicked = onBackClicked,
        isPickupStoresFilterVisible = isPickupStoresFilterVisible,
        onFilterChanged = onFilterChanged,
        onFilterClicked = onFilterClicked,
        productCount = productCount,
        onShowProductsClicked = onShowProductsClicked,
        onFilterListErrorRefreshClicked = onFilterListErrorRefreshClicked,
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.systemBars
                    .union(WindowInsets.displayCutout),
            ),
    )
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
