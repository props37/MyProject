package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint

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
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.component.SelectedPickupPoint
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.component.TopBar
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.model.SelectedPickupPointEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.model.SelectedPickupPointState

@Composable
internal fun SelectedPickupPointScreen(
    navActions: SelectedPickupPointNavActions,
    viewModel: SelectedPickupPointViewModel = hiltViewModel(),
) {
    val selectedPickupPointState by viewModel.selectedPickupPointState.collectAsStateWithLifecycle()

    ScreenContent(
        selectedPickupPointState = selectedPickupPointState,
        onSelectedPickupPointEvent = viewModel::onSelectedPickupPointEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    selectedPickupPointState: SelectedPickupPointState,
    onSelectedPickupPointEvent: (SelectedPickupPointEvent) -> Unit,
    sideEffects: Flow<SelectedPickupPointSideEffect>,
    navActions: SelectedPickupPointNavActions,
) {
    SelectedPickupPointScreenBehavior(
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
        TopBar(
            onBackClicked = { onSelectedPickupPointEvent(SelectedPickupPointEvent.BackClicked) },
        )

        SelectedPickupPoint(
            state = selectedPickupPointState,
            onEvent = onSelectedPickupPointEvent,
        )
    }
}
