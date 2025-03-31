package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.uicompose.animateFastScrollToItem
import ru.livetyping.zarina.core.uicompose.pager.rememberPagerStateWithTabRow
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.PickupPointSelectorEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.PickupPointSelectorState

@Composable
internal fun PickupPointSelector(
    state: PickupPointSelectorState,
    onEvent: (PickupPointSelectorEvent) -> Unit,
    currentLocationProvider: () -> Location?,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing },
) {
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {
        Filtration(
            filterTextFieldState = state.filterTextFieldState,
            filters = state.filters,
            onFilterClicked = { onEvent(PickupPointSelectorEvent.FilterClicked(it)) },
        )
        Spacer(modifier = Modifier.height(4.dp))

        val viewModePagerState = rememberPagerStateWithTabRow(
            tabs = state.viewModeSelectorState.tabs,
            currentTab = state.viewModeSelectorState.currentTab,
            onTabChanged = { mode ->
                val tabRowEvent = TabRowEvent.TabChanged(mode)
                onEvent(PickupPointSelectorEvent.ViewModeSelectorEvent(tabRowEvent))
            },
            pageCount = { state.viewModeSelectorState.tabs.size },
        )

        val pickupPointLazyListState = rememberLazyListState()

        ViewModeSelector(
            state = state.viewModeSelectorState,
            onEvent = { event ->
                if (event is TabRowEvent.TabReselected) {
                    coroutineScope.launch {
                        pickupPointLazyListState.animateFastScrollToItem(
                            item = 0,
                            distanceThreshold = PickupPointListFastScrollThreshold,
                        )
                    }
                }
                onEvent(PickupPointSelectorEvent.ViewModeSelectorEvent(event))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        ViewModeHorizontalPager(
            pagerState = viewModePagerState,
            viewModes = state.viewModeSelectorState.tabs,
            pickupPointListState = state.pickupPointListState,
            pickupPointLazyListState = pickupPointLazyListState,
            onPickupPointClicked = { onEvent(PickupPointSelectorEvent.PickupPointClicked(it)) },
            onErrorRefreshClicked = { onEvent(PickupPointSelectorEvent.ErrorRefreshClicked) },
            currentLocationProvider = currentLocationProvider,
            onMyLocationClicked = { onEvent(PickupPointSelectorEvent.MyLocationClicked) },
            windowInsetsProvider = windowInsetsProvider,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

private const val PickupPointListFastScrollThreshold = 10
