package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.common.Url

sealed interface PaymentData

data class CardPaymentData(
    val paymentId: PaymentId,
    val paymentUrl: Url,
) : PaymentData {
    @JvmInline
    value class PaymentId(val value: String)
}

data class UrlPaymentData(
    val paymentUrl: Url,
) : PaymentData
