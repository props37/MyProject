package ru.livetyping.zarina.data.order.impl.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethodType

@Serializable
@JvmInline
internal value class PaymentMethodTypeDto(val value: String) {
    fun toPaymentMethodType(): PaymentMethodType = when (value) {
        VALUE_POSTPAID -> PaymentMethodType.POSTPAID
        VALUE_PAYTURE_IN_PAY -> PaymentMethodType.PAYTURE_IN_PAY
        VALUE_PAYTURE_WALLET -> PaymentMethodType.PAYTURE_WALLET
        VALUE_QR -> PaymentMethodType.QR
        VALUE_PODELI -> PaymentMethodType.PODELI
        VALUE_PREPAID -> PaymentMethodType.PREPAID
        VALUE_GIFT_CERTIFICATE -> PaymentMethodType.GIFT_CERTIFICATE
        VALUE_FREE -> PaymentMethodType.FREE
        else -> error("Unknown payment method $value")
    }

    companion object {
        fun from(paymentMethodType: PaymentMethodType): PaymentMethodTypeDto {
            val value = when (paymentMethodType) {
                PaymentMethodType.POSTPAID -> VALUE_POSTPAID
                PaymentMethodType.PAYTURE_IN_PAY -> VALUE_PAYTURE_IN_PAY
                PaymentMethodType.PAYTURE_WALLET -> VALUE_PAYTURE_WALLET
                PaymentMethodType.QR -> VALUE_QR
                PaymentMethodType.PODELI -> VALUE_PODELI
                PaymentMethodType.PREPAID -> VALUE_PREPAID
                PaymentMethodType.GIFT_CERTIFICATE -> VALUE_GIFT_CERTIFICATE
                PaymentMethodType.FREE -> VALUE_FREE
            }
            return PaymentMethodTypeDto(value)
        }

        private const val VALUE_POSTPAID = "postpaid"
        private const val VALUE_PAYTURE_IN_PAY = "paytureinpay"
        private const val VALUE_PAYTURE_WALLET = "payturewallet"
        private const val VALUE_QR = "qr"
        private const val VALUE_PODELI = "podeli"
        private const val VALUE_PREPAID = "prepaid"
        private const val VALUE_GIFT_CERTIFICATE = "gift_card"
        private const val VALUE_FREE = "free"
    }
}
