package ru.livetyping.zarina.core.network.zarina.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethodType
import timber.log.Timber

@Serializable
@JvmInline
public value class PaymentMethodTypeDto(public val value: String) {
    public fun toPaymentMethodType(): PaymentMethodType? = when (value) {
        VALUE_SBER -> PaymentMethodType.SBER
        VALUE_SBER_SBP -> PaymentMethodType.SBER_SBP
        VALUE_PAYTURE_IN_PAY -> PaymentMethodType.PAYTURE_IN_PAY
        VALUE_PAYTURE_WALLET -> PaymentMethodType.PAYTURE_WALLET
        VALUE_QR -> PaymentMethodType.SBP
        VALUE_PODELI -> PaymentMethodType.PODELI
        VALUE_PREPAID -> PaymentMethodType.PREPAID
        VALUE_POSTPAID -> PaymentMethodType.POSTPAID
        VALUE_GIFT_CERTIFICATE -> PaymentMethodType.GIFT_CERTIFICATE
        VALUE_FREE -> PaymentMethodType.FREE
        else -> {
            Timber.tag(TAG).e("Ignore $this because it can't be mapped to PaymentMethodType")
            null
        }
    }

    public companion object {
        public fun from(paymentMethodType: PaymentMethodType): PaymentMethodTypeDto {
            val value = when (paymentMethodType) {
                PaymentMethodType.SBER -> VALUE_SBER
                PaymentMethodType.SBER_SBP -> VALUE_SBER_SBP
                PaymentMethodType.PAYTURE_IN_PAY -> VALUE_PAYTURE_IN_PAY
                PaymentMethodType.PAYTURE_WALLET -> VALUE_PAYTURE_WALLET
                PaymentMethodType.SBP -> VALUE_QR
                PaymentMethodType.PODELI -> VALUE_PODELI
                PaymentMethodType.PREPAID -> VALUE_PREPAID
                PaymentMethodType.POSTPAID -> VALUE_POSTPAID
                PaymentMethodType.GIFT_CERTIFICATE -> VALUE_GIFT_CERTIFICATE
                PaymentMethodType.FREE -> VALUE_FREE
            }
            return PaymentMethodTypeDto(value)
        }

        private const val VALUE_SBER = "sber"
        private const val VALUE_SBER_SBP = "sbersbp"
        private const val VALUE_PAYTURE_IN_PAY = "paytureinpay"
        private const val VALUE_PAYTURE_WALLET = "payturewallet"
        private const val VALUE_QR = "qr"
        private const val VALUE_PODELI = "podeli"
        private const val VALUE_PREPAID = "prepaid"
        private const val VALUE_POSTPAID = "postpaid"
        private const val VALUE_GIFT_CERTIFICATE = "gift_card"
        private const val VALUE_FREE = "free"

        private const val TAG = "PaymentMethodTypeDto"
    }
}
