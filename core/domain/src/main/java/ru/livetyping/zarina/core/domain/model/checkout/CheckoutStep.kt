package ru.livetyping.zarina.core.domain.model.checkout

import ru.livetyping.zarina.core.domain.model.order.OrderDetailed

public sealed interface CheckoutStep {
    public data class PaymentStarted(val paymentData: PaymentData) : CheckoutStep

    public data object PaymentStatusChecked : CheckoutStep

    public data object PaymentCompleted : CheckoutStep

    public data class CheckoutCompleted(
        val order: OrderDetailed,
        val paymentMethodType: PaymentMethodType,
        val shouldUpdateOrderStatus: Boolean,
        val shouldAwaitPaymentCompleted: Boolean,
    ) : CheckoutStep
}
