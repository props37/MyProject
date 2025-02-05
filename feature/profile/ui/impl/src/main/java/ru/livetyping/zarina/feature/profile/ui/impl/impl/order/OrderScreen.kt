package ru.livetyping.zarina.feature.profile.ui.impl.impl.order

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.component.Order
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.component.OrderTopBar
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.model.OrderEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.model.OrderState

// TODO: [Top] Add ability to pay for the order

@Composable
internal fun OrderScreen(
    navActions: OrderNavActions,
    viewModel: OrderViewModel = hiltViewModel(),
) {
    val orderState by viewModel.orderState.collectAsStateWithLifecycle()

    ScreenContent(
        orderState = orderState,
        onOrderEvent = viewModel::onOrderEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    orderState: OrderState,
    onOrderEvent: (OrderEvent) -> Unit,
    sideEffects: Flow<OrderSideEffect>,
    navActions: OrderNavActions,
) {
    OrderScreenBehavior(
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
            .bottomNavBarPadding(),
    ) {
        val order = (orderState as? OrderState.Success)?.order

        OrderTopBar(
            orderNumber = order?.number,
            onBackClicked = { onOrderEvent(OrderEvent.BackClicked) },
        )

        Order(
            state = orderState,
            onEvent = onOrderEvent,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
