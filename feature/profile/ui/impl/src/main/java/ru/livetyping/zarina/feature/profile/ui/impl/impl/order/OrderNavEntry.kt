package ru.livetyping.zarina.feature.profile.ui.impl.impl.order

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.navigation.NavigationEntry

@Serializable
internal class OrderNavEntry private constructor(
    private val orderId: String,
) : NavigationEntry {
    fun getOrderId(): Order.Id = Order.Id(orderId)

    companion object {
        fun create(orderId: Order.Id): OrderNavEntry = OrderNavEntry(orderId.value)
    }
}
