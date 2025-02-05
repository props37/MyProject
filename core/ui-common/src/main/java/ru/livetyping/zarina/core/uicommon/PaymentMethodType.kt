package ru.livetyping.zarina.core.uicommon

import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethodType
import ru.livetyping.zarina.core.resource.R as RCommon

public val PaymentMethodType.nameResId: Int
    get() = when (this) {
        PaymentMethodType.POSTPAID -> RCommon.string.res_payment_method_upon_receipt
        PaymentMethodType.PAYTURE_IN_PAY -> RCommon.string.res_payment_method_by_card_online
        PaymentMethodType.PAYTURE_WALLET -> RCommon.string.res_payment_method_by_card_online
        PaymentMethodType.QR -> RCommon.string.res_payment_method_sbp
        PaymentMethodType.PODELI -> RCommon.string.res_payment_method_podeli
        PaymentMethodType.PREPAID -> RCommon.string.res_payment_method_by_card_online
        PaymentMethodType.GIFT_CERTIFICATE -> RCommon.string.res_payment_method_gift_certificate
        PaymentMethodType.FREE -> RCommon.string.res_payment_method_gift_certificate
    }
