package ru.livetyping.zarina.core.domain.model.checkout

import ru.livetyping.zarina.core.domain.model.common.Url

public sealed interface PaymentData

public data class PayturePaymentData(
    val paymentId: PaymentId,
    val paymentUrl: Url,
) : PaymentData {
    @JvmInline
    public value class PaymentId(public val value: String)
}

public data class SberPaymentData(
    val sberUid: SberUid,
    val sberOrderId: SberOrderId,
    val paymentUrl: Url,
) : PaymentData {
    @JvmInline
    public value class SberUid(public val value: String)

    @JvmInline
    public value class SberOrderId(public val value: String)
}

public data class UrlPaymentData(
    val paymentUrl: Url,
) : PaymentData
