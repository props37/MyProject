package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector

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
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBar
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector.DeliveryMethodSelectorComponents.DeliveryMethodSelector
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector.model.DeliveryMethodSelectorState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun DeliveryMethodSelectorScreen(
    navActions: DeliveryMethodSelectorNavActions,
    viewModel: DeliveryMethodSelectorViewModel = hiltViewModel(),
) {
    val topBarState by viewModel.topBarState.collectAsStateWithLifecycle()
    val deliveryMethodSelectorState by viewModel.deliveryMethodSelectorState.collectAsStateWithLifecycle()

    ScreenContent(
        topBarState = topBarState,
        onTopBarEvent = viewModel::onTopBarEvent,
        deliveryMethodSelectorState = deliveryMethodSelectorState,
        onDeliveryMethodClicked = viewModel::onDeliveryMethodClicked,
        onDeliveryMethodsErrorRefreshClicked = viewModel::onDeliveryMethodsErrorRefreshClicked,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    topBarState: CheckoutTopBarState,
    onTopBarEvent: (CheckoutTopBarEvent) -> Unit,
    deliveryMethodSelectorState: DeliveryMethodSelectorState,
    onDeliveryMethodClicked: (DeliveryMethod) -> Unit,
    onDeliveryMethodsErrorRefreshClicked: () -> Unit,
    sideEffects: Flow<DeliveryMethodSelectorSideEffect>,
    navActions: DeliveryMethodSelectorNavActions,
) {
    DeliveryMethodSelectorScreenBehavior(
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
            title = stringResource(RCommon.string.res_delivery_method),
            onEvent = onTopBarEvent,
        )

        DeliveryMethodSelector(
            state = deliveryMethodSelectorState,
            onMethodClicked = onDeliveryMethodClicked,
            onErrorRefreshClicked = onDeliveryMethodsErrorRefreshClicked,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
