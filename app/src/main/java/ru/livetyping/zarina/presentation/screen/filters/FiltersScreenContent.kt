package ru.livetyping.zarina.presentation.screen.filters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.domain.filter.Filter
import ru.livetyping.zarina.presentation.common.component.overlay.ZarinaRefreshingOverlay
import ru.livetyping.zarina.presentation.screen.filters.FiltersScreenComponents.FilterList
import ru.livetyping.zarina.presentation.screen.filters.FiltersScreenComponents.TopBar

@Composable
fun FiltersScreenContent(
    filterListState: FilterListState,
    isRefreshing: Boolean,
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
    // Do not use passed modifier here since refreshing overlay should take the whole available space
    Box(modifier = Modifier) {
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

        AnimatedVisibility(
            visible = isRefreshing,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.matchParentSize(),
        ) {
            ZarinaRefreshingOverlay(modifier = Modifier.fillMaxSize())
        }
    }
}
