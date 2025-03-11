package ru.livetyping.zarina.feature.search.ui.impl.impl.listfilter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uicomponent.filtration.listfilter.model.ListFilterEvent
import ru.livetyping.zarina.core.uicomponent.filtration.listfilter.model.ListFilterState
import ru.livetyping.zarina.core.uicomponent.filtration.listfilter.model.ListFilterTopBarEvent
import ru.livetyping.zarina.core.uicomponent.filtration.listfilter.model.ListFilterTopBarState
import ru.livetyping.zarina.core.uicomponent.filtration.listfilter.ui.ListFilterContent
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
internal fun ListFilterScreen(
    navActions: ListFilterNavActions,
    viewModel: ListFilterViewModel = hiltViewModel(),
) {
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()
    val listFilterState by viewModel.listFilterState.collectAsStateWithLifecycle()

    ScreenContent(
        topBarState = topBarState,
        onTopBarEvent = viewModel::onTopBarEvent,
        listFilterState = listFilterState,
        onListFilterEvent = viewModel::onListFilterEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    topBarState: ListFilterTopBarState,
    onTopBarEvent: (ListFilterTopBarEvent) -> Unit,
    listFilterState: ListFilterState,
    onListFilterEvent: (ListFilterEvent) -> Unit,
    sideEffects: Flow<ListFilterSideEffect>,
    navActions: ListFilterNavActions,
) {
    ListFilterScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    ListFilterContent(
        topBarState = topBarState,
        onTopBarEvent = onTopBarEvent,
        listFilterState = listFilterState,
        onListFilterEvent = onListFilterEvent,
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default),
    )
}
