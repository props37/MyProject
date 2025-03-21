package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.common.Url

sealed interface PaymentData

data class PayturePaymentData(
    val paymentId: PaymentId,
    val paymentUrl: Url,
) : PaymentData {
    @JvmInline
    value class PaymentId(val value: String)
}

data class SberPaymentData(
    val sberUid: SberUid,
    val sberOrderId: SberOrderId,
    val paymentUrl: Url,
) : PaymentData {
    @JvmInline
    value class SberUid(val value: String)

    @JvmInline
    value class SberOrderId(val value: String)
}

data class UrlPaymentData(
    val paymentUrl: Url,
) : PaymentData
