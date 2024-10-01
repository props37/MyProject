package ru.livetyping.zarina.data.order.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.order.PaymentMethodType

@Serializable
@JvmInline
value class PaymentMethodTypeDto(val value: String) {
    fun toPaymentMethodType(): PaymentMethodType = when (value) {
        "postpaid" -> PaymentMethodType.POSTPAID
        "paytureinpay" -> PaymentMethodType.PAYTURE_IN_PAY
        "payturewallet" -> PaymentMethodType.PAYTURE_WALLET
        "qr" -> PaymentMethodType.QR
        "podeli" -> PaymentMethodType.PODELI
        "prepaid" -> PaymentMethodType.PREPAID
        "gift_card" -> PaymentMethodType.GIFT_CARD
        "free" -> PaymentMethodType.FREE
        else -> error("Unknown payment method $value")
    }
}
