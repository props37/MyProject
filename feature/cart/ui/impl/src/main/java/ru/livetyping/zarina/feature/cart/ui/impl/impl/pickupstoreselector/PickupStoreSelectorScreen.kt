package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.checkout.PickupStore
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBar
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector.component.PickupStoreSelector
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector.model.PickupStoreSelectorState

@Composable
internal fun PickupStoreSelectorScreen(
    navActions: PickupStoreSelectorNavActions,
    viewModel: PickupStoreSelectorViewModel = hiltViewModel(),
) {
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()
    val pickupStoreSelectorState by viewModel.pickupStoreSelectorState.collectAsStateWithLifecycle()

    ScreenContent(
        topBarState = topBarState,
        onTopBarEvent = viewModel::onTopBarEvent,
        pickupStoreSelectorState = pickupStoreSelectorState,
        onStoreClicked = viewModel::onStoreClicked,
        onStoresErrorRefreshClicked = viewModel::onStoresErrorRefreshClicked,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    topBarState: CheckoutTopBarState,
    onTopBarEvent: (CheckoutTopBarEvent) -> Unit,
    pickupStoreSelectorState: PickupStoreSelectorState,
    onStoreClicked: (PickupStore) -> Unit,
    onStoresErrorRefreshClicked: () -> Unit,
    sideEffects: Flow<PickupStoreSelectorSideEffect>,
    navActions: PickupStoreSelectorNavActions,
) {
    PickupStoreSelectorScreenBehavior(
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
            ),
    ) {
        CheckoutTopBar(
            state = topBarState,
            title = stringResource(R.string.cart_store_selection),
            onEvent = onTopBarEvent,
        )

        PickupStoreSelector(
            state = pickupStoreSelectorState,
            onStoreClicked = onStoreClicked,
            onStoresErrorRefreshClicked = onStoresErrorRefreshClicked,
        )
    }
}
