package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.common.Url

data class CardPaymentData(
    val paymentId: PaymentId,
    val paymentUrl: Url,
) {
    @JvmInline
    value class PaymentId(val value: String)
}
