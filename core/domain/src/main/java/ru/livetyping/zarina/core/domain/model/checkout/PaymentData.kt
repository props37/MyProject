package ru.livetyping.zarina.core.domain.model.checkout

import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.order.Order

public sealed interface PaymentData {
    public val paymentUrl: Url
}

public data class PayturePaymentData(
    val paymentId: PaymentId,
    override val paymentUrl: Url,
) : PaymentData {
    @JvmInline
    public value class PaymentId(public val value: String)
}

public data class SberPaymentData(
    val sberUid: SberUid,
    val sberOrderId: SberOrderId,
    override val paymentUrl: Url,
) : PaymentData {
    @JvmInline
    public value class SberUid(public val value: String)

    @JvmInline
    public value class SberOrderId(public val value: String)
}

public data class SberSbpPaymentData(
    val orderNumber: Order.Number,
    override val paymentUrl: Url,
) : PaymentData

public data class UrlPaymentData(
    override val paymentUrl: Url,
) : PaymentData
