package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.order.OrderDetails
import ru.livetyping.zarina.domain.order.PaymentMethodType

sealed interface CheckoutStage {
    data class Payment(val paymentData: PaymentData) : CheckoutStage

    data object PaymentCompleted : CheckoutStage

    data class Completed(
        val order: OrderDetails,
        val paymentMethodType: PaymentMethodType,
        val shouldUpdateOrderStatus: Boolean,
        val shouldAwaitPaymentCompleted: Boolean,
    ) : CheckoutStage
}
