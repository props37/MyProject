package ru.livetyping.zarina.core.analytics.model

/**
 * [typeName] matches payment method types on backend.
 */
public enum class PaymentMethodType(public val typeName: String) {
    SBER("sber"),
    SBER_SBP("sbersbp"),
    PAYTURE_IN_PAY("paytureinpay"),
    PAYTURE_WALLET("payturewallet"),
    SBP("qr"),
    PODELI("podeli"),
    PREPAID("prepaid"),
    POSTPAID("postpaid"),
    GIFT_CERTIFICATE("gift_card"),
    FREE("free"),
}
