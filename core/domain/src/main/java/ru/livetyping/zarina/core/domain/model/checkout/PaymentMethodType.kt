package ru.livetyping.zarina.core.domain.model.checkout

// Marked as stable on config/compose/stability_config.txt
public enum class PaymentMethodType {
    SBER,
    SBER_SBP,
    PAYTURE_IN_PAY,
    PAYTURE_WALLET,
    SBP, // qr on backend
    PODELI,
    PREPAID,
    POSTPAID,
    GIFT_CERTIFICATE,
    FREE,
}
