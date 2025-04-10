package ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingScreen

internal fun NavGraphBuilder.orderPlacingScreen(actions: OrderPlacingNavActions) {
    composable<OrderPlacingNavEntry>(typeMap = OrderPlacingNavEntry.typeMap()) {
        OrderPlacingScreen(actions)
    }
}
