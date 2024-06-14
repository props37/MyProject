package ru.livetyping.zarina.presentation.screen.filters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.domain.filter.Filter
import ru.livetyping.zarina.presentation.screen.filters.FiltersScreenComponents.FilterList
import ru.livetyping.zarina.presentation.screen.filters.FiltersScreenComponents.TopBar

@Composable
fun FiltersScreenContent(
    filterListState: FilterListState,
    isResetFiltersButtonVisible: Boolean,
    onResetFiltersClicked: () -> Unit,
    onBackClicked: () -> Unit,
    isPickupStoresFilterVisible: Boolean,
    onFilterChanged: (Filter) -> Unit,
    onFilterClicked: (Filter) -> Unit,
    productCount: Int?,
    onShowProductsClicked: () -> Unit,
    onFilterListErrorRefreshClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        TopBar(
            isResetButtonVisible = isResetFiltersButtonVisible,
            onBackClicked = onBackClicked,
            onResetClicked = onResetFiltersClicked,
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
