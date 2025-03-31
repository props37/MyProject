package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBar
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.component.PickupPointSelector
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.PickupPointSelectorEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model.PickupPointSelectorState

@Composable
internal fun PickupPointSelectorScreen(
    navActions: PickupPointSelectorNavActions,
    viewModel: PickupPointSelectorViewModel = hiltViewModel(),
) {
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()
    val pickupPointSelectorState by viewModel.pickupPointSelectorState.collectAsStateWithLifecycle()
    val currentLocation = viewModel.currentLocation.collectAsStateWithLifecycle()

    ScreenContent(
        topBarState = topBarState,
        onTopBarEvent = viewModel::onTopBarEvent,
        pickupPointSelectorState = pickupPointSelectorState,
        onPickupPointSelectorEvent = viewModel::onPickupPointSelectorEvent,
        currentLocationProvider = { currentLocation.value },
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    topBarState: CheckoutTopBarState,
    onTopBarEvent: (CheckoutTopBarEvent) -> Unit,
    pickupPointSelectorState: PickupPointSelectorState,
    onPickupPointSelectorEvent: (PickupPointSelectorEvent) -> Unit,
    currentLocationProvider: () -> Location?,
    sideEffects: Flow<PickupPointSelectorSideEffect>,
    navActions: PickupPointSelectorNavActions,
) {
    PickupPointSelectorScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout)
                    .union(WindowInsets.ime),
            )
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        CheckoutTopBar(
            state = topBarState,
            title = stringResource(R.string.cart_delivery_to_pickup_point),
            onEvent = onTopBarEvent,
        )
        Spacer(modifier = Modifier.height(4.dp))

        PickupPointSelector(
            state = pickupPointSelectorState,
            onEvent = onPickupPointSelectorEvent,
        )
    }
}
