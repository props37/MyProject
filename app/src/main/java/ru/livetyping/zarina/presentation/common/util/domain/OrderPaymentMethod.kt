package ru.livetyping.zarina.presentation.common.util.domain

import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.order.OrderPaymentMethod

val OrderPaymentMethod.nameResId: Int
    get() = when (this) {
        OrderPaymentMethod.POSTPAID -> R.string.order_payment_method_upon_receipt
        OrderPaymentMethod.PAYTURE_IN_PAY -> R.string.order_payment_method_by_card_online
        OrderPaymentMethod.PAYTURE_WALLET -> R.string.order_payment_method_by_card_online
        OrderPaymentMethod.QR -> R.string.order_payment_method_sbp
        OrderPaymentMethod.PODELI -> R.string.order_payment_method_podeli
        OrderPaymentMethod.PREPAID -> R.string.order_payment_method_by_card_online
        OrderPaymentMethod.GIFT_CARD -> R.string.order_payment_method_gift_certificate
        OrderPaymentMethod.FREE -> R.string.order_payment_method_gift_certificate
    }
