package ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed.OrderConfirmedNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed.OrderConfirmedNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed.OrderConfirmedScreen

internal fun NavGraphBuilder.orderConfirmedScreen(actions: OrderConfirmedNavActions) {
    composable<OrderConfirmedNavEntry>(typeMap = OrderConfirmedNavEntry.typeMap()) {
        OrderConfirmedScreen(actions)
    }
}
