package ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uicomponent.filtration.model.ProductFiltrationEvent
import ru.livetyping.zarina.core.uicomponent.filtration.model.ProductFiltrationState
import ru.livetyping.zarina.core.uicomponent.filtration.model.ProductFiltrationTopBarEvent
import ru.livetyping.zarina.core.uicomponent.filtration.model.ProductFiltrationTopBarState
import ru.livetyping.zarina.core.uicomponent.filtration.ui.ProductFiltrationContent
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
internal fun FiltrationScreen(
    navActions: FiltrationNavActions,
    viewModel: FiltrationViewModel = hiltViewModel(),
) {
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()
    val filtrationState by viewModel.filtrationState.collectAsStateWithLifecycle()

    ScreenContent(
        topBarState = topBarState,
        onTopBarEvent = viewModel::onTopBarEvent,
        filtrationState = filtrationState,
        onFiltrationEvent = viewModel::onFiltrationEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    topBarState: ProductFiltrationTopBarState,
    onTopBarEvent: (ProductFiltrationTopBarEvent) -> Unit,
    filtrationState: ProductFiltrationState,
    onFiltrationEvent: (ProductFiltrationEvent) -> Unit,
    sideEffects: Flow<FiltrationSideEffect>,
    navActions: FiltrationNavActions,
) {
    FiltrationScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    ProductFiltrationContent(
        topBarState = topBarState,
        onTopBarEvent = onTopBarEvent,
        filtrationState = filtrationState,
        onFiltrationEvent = onFiltrationEvent,
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default),
    )
}
