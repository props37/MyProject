package ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration.model.FiltrationState
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration.model.TopBarEvent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration.model.TopBarState

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
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    topBarState: TopBarState,
    onTopBarEvent: (TopBarEvent) -> Unit,
    filtrationState: FiltrationState,
    sideEffects: Flow<FiltrationSideEffect>,
    navActions: FiltrationNavActions,
) {
    FiltrationScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        // TODO: [Top] Implement
    }
}
