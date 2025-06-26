package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uicompose.pager.rememberPagerStateIntegratedWithTabRow
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model.SizeTableEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model.SizeTableState
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.ui.TopBar
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.ui.ViewModePager
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.ui.ViewModeSelector

@Composable
internal fun SizeTableScreen(
    navActions: SizeTableNavActions,
    viewModel: SizeTableViewModel = hiltViewModel(),
) {
    val sizeTableState by viewModel.sizeTableState.collectAsStateWithLifecycle()

    ScreenContent(
        sizeTableState = sizeTableState,
        onSizeTableEvent = viewModel::onSizeTableEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    sizeTableState: SizeTableState,
    onSizeTableEvent: (SizeTableEvent) -> Unit,
    sideEffects: Flow<SizeTableSideEffect>,
    navActions: SizeTableNavActions,
) {
    SizeTableScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme2.colors.white),
    ) {
        TopBar(
            onCloseClicked = { onSizeTableEvent(SizeTableEvent.CloseClicked) },
        )

        val pagerState = rememberPagerStateIntegratedWithTabRow(
            tabs = sizeTableState.viewModeSelectorState.tabs,
            currentTab = sizeTableState.viewModeSelectorState.currentTab,
            onTabChanged = { onSizeTableEvent(SizeTableEvent.ViewModeSelected(it)) },
            pageCount = { sizeTableState.viewModeSelectorState.tabs.size },
        )

        ViewModeSelector(
            state = sizeTableState.viewModeSelectorState,
            onViewModeSelected = { onSizeTableEvent(SizeTableEvent.ViewModeSelected(it)) },
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        ViewModePager(
            pagerState = pagerState,
            viewModes = sizeTableState.viewModeSelectorState.tabs,
            sizeGuide = sizeTableState.sizeGuide,
        )
    }
}
