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
import ru.livetyping.zarina.feature.cart.ui.impl.impl.components.CheckoutTopBar
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector.DeliveryMethodSelectorComponents.DeliveryMethodSelector
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector.model.DeliveryMethodSelectorState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.model.CheckoutTopBarState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun DeliveryMethodSelectorScreen(
    navActions: DeliveryMethodSelectorNavActions,
    viewModel: DeliveryMethodSelectorViewModel = hiltViewModel(),
) {
    val checkoutStep by viewModel.checkoutStep.collectAsStateWithLifecycle()
    val checkoutStepCount by viewModel.checkoutStepCount.collectAsStateWithLifecycle()
    val deliveryMethodSelectorState by viewModel.deliveryMethodSelectorState.collectAsStateWithLifecycle()

    ScreenContent(
        checkoutStep = checkoutStep,
        checkoutStepCount = checkoutStepCount,
        deliveryMethodSelectorState = deliveryMethodSelectorState,
        onDeliveryMethodClicked = viewModel::onDeliveryMethodClicked,
        onDeliveryMethodsErrorRefreshClicked = viewModel::onDeliveryMethodsErrorRefreshClicked,
        onBackClicked = viewModel::onBackClicked,
        onCloseClicked = viewModel::onCloseClicked,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    checkoutStep: Int,
    checkoutStepCount: Int,
    deliveryMethodSelectorState: DeliveryMethodSelectorState,
    onDeliveryMethodClicked: (DeliveryMethod) -> Unit,
    onDeliveryMethodsErrorRefreshClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onCloseClicked: () -> Unit,
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
            state = CheckoutTopBarState(checkoutStep, checkoutStepCount),
            title = stringResource(RCommon.string.res_delivery_method),
            isBackButtonVisible = true,
            onBackClicked = onBackClicked,
            onCloseClicked = onCloseClicked,
        )

        DeliveryMethodSelector(
            state = deliveryMethodSelectorState,
            onMethodClicked = onDeliveryMethodClicked,
            onErrorRefreshClicked = onDeliveryMethodsErrorRefreshClicked,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
