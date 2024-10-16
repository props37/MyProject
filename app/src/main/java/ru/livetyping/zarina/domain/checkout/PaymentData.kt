package ru.livetyping.zarina.domain.checkout

sealed interface PaymentData

data class PaytureInPayPaymentData(
    val data: CardPaymentData,
) : PaymentData

data class PaytureWalletPaymentData(
    val data: CardPaymentData,
) : PaymentData
