package ru.livetyping.zarina.feature.profile.ui.impl.impl.orderlist

import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.navigation.NavigationActions

internal class OrderListNavActions(
    val onBackClicked: () -> Unit,
    val onOrderClicked: (Order) -> Unit,
) : NavigationActions
