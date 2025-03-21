package ru.livetyping.zarina.presentation.common.util.domain

import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.order.PaymentMethodType

val PaymentMethodType.nameResId: Int
    get() = when (this) {
        PaymentMethodType.SBER -> R.string.order_payment_method_by_card_online
        PaymentMethodType.POSTPAID -> R.string.order_payment_method_upon_receipt
        PaymentMethodType.PAYTURE_IN_PAY -> R.string.order_payment_method_by_card_online
        PaymentMethodType.PAYTURE_WALLET -> R.string.order_payment_method_by_card_online
        PaymentMethodType.QR -> R.string.order_payment_method_sbp
        PaymentMethodType.PODELI -> R.string.order_payment_method_podeli
        PaymentMethodType.PREPAID -> R.string.order_payment_method_by_card_online
        PaymentMethodType.GIFT_CERTIFICATE -> R.string.order_payment_method_gift_certificate
        PaymentMethodType.FREE -> R.string.order_payment_method_gift_certificate
    }
