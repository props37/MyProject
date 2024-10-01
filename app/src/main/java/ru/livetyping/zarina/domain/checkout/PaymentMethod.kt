package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.order.PaymentMethodType

data class PaymentMethod(
    val id: Id,
    val type: PaymentMethodType,
    val title: String,
    val description: String,
) {
    @JvmInline
    value class Id(val value: Int)
}
