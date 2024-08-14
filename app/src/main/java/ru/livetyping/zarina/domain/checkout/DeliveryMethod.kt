package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.order.DeliveryMethodType

data class DeliveryMethod(
    val id: Id,
    val type: DeliveryMethodType,
    val name: String,
    val description: String?,
) {
    @JvmInline
    value class Id(val value: Int)
}
