package ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.uicompose.pager.rememberPagerStateWithTabRow
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.component.StoreListStoreModalBottomSheet
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.component.StoreListTopBar
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.component.StoreListViewModePager
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.component.StoreListViewModeSelector
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model.StoreListEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model.StoreListState
import ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model.StoreListViewMode

@Composable
internal fun StoreListScreen(
    navActions: StoreListNavActions,
    viewModel: StoreListViewModel = hiltViewModel(),
) {
    val viewModeSelectorState by viewModel.viewModeSelectorState.collectAsStateWithLifecycle()
    val mapState by viewModel.mapState.collectAsStateWithLifecycle()
    val listState by viewModel.listState.collectAsStateWithLifecycle()
    val currentLocation by viewModel.currentLocation.collectAsStateWithLifecycle()

    ScreenContent(
        onStoreListEvent = viewModel::onStoreListEvent,
        viewModeSelectorState = viewModeSelectorState,
        onViewModeSelectorEvent = viewModel::onViewModeSelectorEvent,
        mapStateProvider = { mapState },
        listStateProvider = { listState },
        currentLocationProvider = { currentLocation },
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScreenContent(
    onStoreListEvent: (StoreListEvent) -> Unit,
    viewModeSelectorState: TabRowState<StoreListViewMode>,
    onViewModeSelectorEvent: (TabRowEvent<StoreListViewMode>) -> Unit,
    mapStateProvider: () -> StoreListState,
    listStateProvider: () -> StoreListState,
    currentLocationProvider: () -> Location?,
    sideEffects: Flow<StoreListSideEffect>,
    navActions: StoreListNavActions,
) {
    StoreListScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    var visibleStoreModalBottomSheet by remember { mutableStateOf<Store?>(null) }
    visibleStoreModalBottomSheet?.let { store ->
        StoreListStoreModalBottomSheet(
            store = store,
            onDismissRequest = { visibleStoreModalBottomSheet = null },
        )
    }

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
        StoreListTopBar(
            onBackClicked = { onStoreListEvent(StoreListEvent.BackClicked) },
        )

        val pagerState = rememberPagerStateWithTabRow(
            tabs = viewModeSelectorState.tabs,
            currentTab = viewModeSelectorState.currentTab,
            onTabChanged = { onViewModeSelectorEvent(TabRowEvent.TabChanged(it)) },
            pageCount = { viewModeSelectorState.tabs.size }
        )

        StoreListViewModeSelector(
            state = viewModeSelectorState,
            onEvent = onViewModeSelectorEvent,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        StoreListViewModePager(
            viewModes = viewModeSelectorState.tabs,
            pagerState = pagerState,
            onStoreListEvent = onStoreListEvent,
            mapStateProvider = mapStateProvider,
            listStateProvider = listStateProvider,
            currentLocationProvider = currentLocationProvider,
            onStoreClicked = { visibleStoreModalBottomSheet = it },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
