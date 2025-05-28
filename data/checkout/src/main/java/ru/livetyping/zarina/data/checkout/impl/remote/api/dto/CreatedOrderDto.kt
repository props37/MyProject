package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.core.network.zarina.dto.OrderDto

@Serializable
internal data class CreatedOrderDto(
    @SerialName("order")
    val order: OrderDto? = null,
) {
    fun toOrder(): OrderDetailed {
        checkPropertyNotNull(order) { "order" }
        return order.toOrder()
    }
}
