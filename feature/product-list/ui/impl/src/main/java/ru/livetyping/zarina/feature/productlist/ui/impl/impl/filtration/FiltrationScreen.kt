package ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uicomponent.filtration.FiltrationContent
import ru.livetyping.zarina.core.uicomponent.filtration.FiltrationState
import ru.livetyping.zarina.core.uicomponent.filtration.FiltrationTopBarEvent
import ru.livetyping.zarina.core.uicomponent.filtration.FiltrationTopBarState
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
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    topBarState: FiltrationTopBarState,
    onTopBarEvent: (FiltrationTopBarEvent) -> Unit,
    filtrationState: FiltrationState,
    sideEffects: Flow<FiltrationSideEffect>,
    navActions: FiltrationNavActions,
) {
    FiltrationScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    FiltrationContent(
        topBarState = topBarState,
        onTopBarEvent = onTopBarEvent,
        filtrationState = filtrationState,
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default),
    )

//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(UiKitTheme.colors.background.general.regular.default)
//            .windowInsetsPadding(
//                WindowInsets.statusBars
//                    .union(WindowInsets.displayCutout),
//            )
//            .bottomNavBarPadding(WindowInsets.ime),
//    ) {
//        // TODO: [Top] Implement
//    }
}
