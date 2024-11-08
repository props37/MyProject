package ru.livetyping.zarina.presentation.model.order

import ru.livetyping.zarina.domain.order.PaymentMethodType

enum class PaymentMethodTypeParcelable {
    POSTPAID,
    PAYTURE_IN_PAY,
    PAYTURE_WALLET,
    QR,
    PODELI,
    PREPAID,
    GIFT_CARD,
    FREE;

    fun toPaymentMethodType(): PaymentMethodType = when (this) {
        POSTPAID -> PaymentMethodType.POSTPAID
        PAYTURE_IN_PAY -> PaymentMethodType.PAYTURE_IN_PAY
        PAYTURE_WALLET -> PaymentMethodType.PAYTURE_WALLET
        QR -> PaymentMethodType.QR
        PODELI -> PaymentMethodType.PODELI
        PREPAID -> PaymentMethodType.PREPAID
        GIFT_CARD -> PaymentMethodType.GIFT_CARD
        FREE -> PaymentMethodType.FREE
    }

    companion object {
        fun from(type: PaymentMethodType): PaymentMethodTypeParcelable = when (type) {
            PaymentMethodType.POSTPAID -> POSTPAID
            PaymentMethodType.PAYTURE_IN_PAY -> PAYTURE_IN_PAY
            PaymentMethodType.PAYTURE_WALLET -> PAYTURE_WALLET
            PaymentMethodType.QR -> QR
            PaymentMethodType.PODELI -> PODELI
            PaymentMethodType.PREPAID -> PREPAID
            PaymentMethodType.GIFT_CARD -> GIFT_CARD
            PaymentMethodType.FREE -> FREE
        }
    }
}
