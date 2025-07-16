package ru.livetyping.zarina.feature.profile.ui.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.profile.ui.impl.orderlist.OrderListNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.orderlist.OrderListNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.orderlist.OrderListScreen

internal fun NavGraphBuilder.orderListScreen(actions: OrderListNavActions) {
    composable<OrderListNavEntry> {
        OrderListScreen(actions)
    }
}
