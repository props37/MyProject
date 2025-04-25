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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.bottombar.navigation.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryAddressSelectorEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryAddressSelectorState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.GenericBottomSheetState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.search.AddressSearchBottomSheetState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.search.AddressSearchEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.search.AddressSearchModalBottomSheet
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.ui.DeliveryAddressSelector
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.ui.GenericModalBottomSheet
import ru.livetyping.zarina.feature.cart.ui.impl.impl.ui.topbar.CheckoutTopBar
import ru.livetyping.zarina.feature.cart.ui.impl.impl.ui.topbar.CheckoutTopBarEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.ui.topbar.CheckoutTopBarState

@Composable
internal fun DeliveryAddressSelectorScreen(
    navActions: DeliveryAddressSelectorNavActions,
    viewModel: DeliveryAddressSelectorViewModel,
) {
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()
    val deliveryAddressSelectorState by viewModel.deliveryAddressSelectorState.collectAsStateWithLifecycle()
    val addressSearchBottomSheetState by viewModel.addressSearchBottomSheetState.collectAsStateWithLifecycle()
    val genericBottomSheetState by viewModel.genericBottomSheetState.collectAsStateWithLifecycle()

    ScreenContent(
        topBarState = topBarState,
        onTopBarEvent = viewModel::onTopBarEvent,
        deliveryAddressSelectorState = deliveryAddressSelectorState,
        onDeliveryAddressSelectorEvent = viewModel::onDeliveryAddressSelectorEvent,
        addressSearchBottomSheetState = addressSearchBottomSheetState,
        onAddressSearchEvent = viewModel::onAddressSearchEvent,
        genericBottomSheetState = genericBottomSheetState,
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
    addressSearchBottomSheetState: AddressSearchBottomSheetState,
    onAddressSearchEvent: (AddressSearchEvent) -> Unit,
    genericBottomSheetState: GenericBottomSheetState,
    sideEffects: Flow<DeliveryAddressSelectorSideEffect>,
    navActions: DeliveryAddressSelectorNavActions,
) {
    DeliveryAddressSelectorScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    AddressSearchModalBottomSheet(
        state = addressSearchBottomSheetState,
        onEvent = onAddressSearchEvent,
    )

    GenericModalBottomSheet(
        state = genericBottomSheetState,
        onClose = {
            onDeliveryAddressSelectorEvent(DeliveryAddressSelectorEvent.GenericBottomSheetClosed)
        },
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
