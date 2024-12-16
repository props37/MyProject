package ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.OrderNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.OrderNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.order.OrderScreen

internal fun NavGraphBuilder.orderScreen(actions: OrderNavActions) {
    composable<OrderNavEntry> {
        OrderScreen(actions)
    }
}
