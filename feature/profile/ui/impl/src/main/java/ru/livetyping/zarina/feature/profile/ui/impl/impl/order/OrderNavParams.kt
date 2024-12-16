package ru.livetyping.zarina.feature.profile.ui.impl.impl.order

import ru.livetyping.zarina.core.domain.model.order.Order

internal data class OrderNavParams(val orderId: Order.Id) {
    fun toNavEntry(): OrderNavEntry = OrderNavEntry(orderId.value)
}
