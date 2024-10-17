package ru.livetyping.zarina.domain.checkout

sealed interface CheckoutStage {
    data class Payment(val paymentData: PaymentData) : CheckoutStage

    data object PaymentCompleted : CheckoutStage

    data class Completed(val waitUntilPaymentClosed: Boolean) : CheckoutStage
}
