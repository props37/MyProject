package ru.livetyping.zarina.domain.checkout

sealed interface CheckoutStage {
    data class Payment(val paymentData: PaymentData) : CheckoutStage

    data object PaymentCompleted : CheckoutStage

    data object Completed : CheckoutStage
}
