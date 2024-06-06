package ru.livetyping.zarina.presentation.screen.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.domain.filter.Filter
import ru.livetyping.zarina.presentation.screen.filters.FiltersScreenComponents.FilterList
import ru.livetyping.zarina.presentation.screen.filters.FiltersScreenComponents.TopBar
import ru.livetyping.zarina.presentation.theme.UiKitTheme

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
