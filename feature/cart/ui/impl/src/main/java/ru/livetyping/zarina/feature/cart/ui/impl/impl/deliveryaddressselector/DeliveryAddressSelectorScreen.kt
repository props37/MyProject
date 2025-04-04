package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector

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
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryAddressSelectorEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryAddressSelectorState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.ui.DeliveryAddressSelector
import ru.livetyping.zarina.feature.cart.ui.impl.impl.ui.topbar.CheckoutTopBar
import ru.livetyping.zarina.feature.cart.ui.impl.impl.ui.topbar.CheckoutTopBarEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.ui.topbar.CheckoutTopBarState

@Composable
internal fun DeliveryAddressSelectorScreen(
    navActions: DeliveryAddressSelectorNavActions,
    viewModel: DeliveryAddressSelectorViewModel = hiltViewModel(),
) {
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()
    val deliveryAddressSelectorState by viewModel.deliveryAddressSelectorState.collectAsStateWithLifecycle()

    ScreenContent(
        topBarState = topBarState,
        onTopBarEvent = viewModel::onTopBarEvent,
        deliveryAddressSelectorState = deliveryAddressSelectorState,
        onDeliveryAddressSelectorEvent = viewModel::onDeliveryAddressSelectorEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    topBarState: CheckoutTopBarState,
    onTopBarEvent: (CheckoutTopBarEvent) -> Unit,
    deliveryAddressSelectorState: DeliveryAddressSelectorState,
    onDeliveryAddressSelectorEvent: (DeliveryAddressSelectorEvent) -> Unit,
    sideEffects: Flow<DeliveryAddressSelectorSideEffect>,
    navActions: DeliveryAddressSelectorNavActions,
) {
    DeliveryAddressSelectorScreenBehavior(
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
            onEvent = onTopBarEvent,
        )

        DeliveryAddressSelector(
            state = deliveryAddressSelectorState,
            onEvent = onDeliveryAddressSelectorEvent,
        )
    }
}
