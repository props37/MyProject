package ru.livetyping.zarina.data.order.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.order.OrderPaymentMethod

@Serializable
@JvmInline
value class OrderPaymentMethodDto(val value: String) {
    fun toOrderPaymentMethod(): OrderPaymentMethod = when (value) {
        "postpaid" -> OrderPaymentMethod.POSTPAID
        "paytureinpay" -> OrderPaymentMethod.PAYTURE_IN_PAY
        "payturewallet" -> OrderPaymentMethod.PAYTURE_WALLET
        "qr" -> OrderPaymentMethod.QR
        "podeli" -> OrderPaymentMethod.PODELI
        "prepaid" -> OrderPaymentMethod.PREPAID
        "gift_card" -> OrderPaymentMethod.GIFT_CARD
        "free" -> OrderPaymentMethod.FREE
        else -> error("Unknown payment method $value")
    }
}
