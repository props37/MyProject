package ru.livetyping.zarina.core.domain.analytics

import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethodType
import ru.livetyping.zarina.core.analytics.model.PaymentMethodType as AppMetricaPaymentMethodType

public fun PaymentMethodType.toAppMetricaPaymentMethodType(): AppMetricaPaymentMethodType {
    return when (this) {
        PaymentMethodType.SBER -> AppMetricaPaymentMethodType.SBER
        PaymentMethodType.SBER_SBP -> AppMetricaPaymentMethodType.SBER_SBP
        PaymentMethodType.PAYTURE_IN_PAY -> AppMetricaPaymentMethodType.PAYTURE_IN_PAY
        PaymentMethodType.PAYTURE_WALLET -> AppMetricaPaymentMethodType.PAYTURE_WALLET
        PaymentMethodType.SBP -> AppMetricaPaymentMethodType.SBP
        PaymentMethodType.PODELI -> AppMetricaPaymentMethodType.PODELI
        PaymentMethodType.PREPAID -> AppMetricaPaymentMethodType.PREPAID
        PaymentMethodType.POSTPAID -> AppMetricaPaymentMethodType.POSTPAID
        PaymentMethodType.GIFT_CERTIFICATE -> AppMetricaPaymentMethodType.GIFT_CERTIFICATE
        PaymentMethodType.FREE -> AppMetricaPaymentMethodType.FREE
    }
}
