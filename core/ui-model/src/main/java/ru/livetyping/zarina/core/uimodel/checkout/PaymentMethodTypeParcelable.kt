package ru.livetyping.zarina.core.uimodel.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethodType

@Serializable
@Parcelize
public enum class PaymentMethodTypeParcelable : Parcelable {
    SBER,
    SBER_SBP,
    PAYTURE_IN_PAY,
    PAYTURE_WALLET,
    SBP,
    PODELI,
    PREPAID,
    POSTPAID,
    GIFT_CERTIFICATE,
    FREE;

    public fun toPaymentMethodType(): PaymentMethodType = when (this) {
        SBER -> PaymentMethodType.SBER
        SBER_SBP -> PaymentMethodType.SBER_SBP
        PAYTURE_IN_PAY -> PaymentMethodType.PAYTURE_IN_PAY
        PAYTURE_WALLET -> PaymentMethodType.PAYTURE_WALLET
        SBP -> PaymentMethodType.SBP
        PODELI -> PaymentMethodType.PODELI
        POSTPAID -> PaymentMethodType.POSTPAID
        PREPAID -> PaymentMethodType.PREPAID
        GIFT_CERTIFICATE -> PaymentMethodType.GIFT_CERTIFICATE
        FREE -> PaymentMethodType.FREE
    }

    public companion object {
        public fun from(type: PaymentMethodType): PaymentMethodTypeParcelable = when (type) {
            PaymentMethodType.SBER -> SBER
            PaymentMethodType.SBER_SBP -> SBER_SBP
            PaymentMethodType.PAYTURE_IN_PAY -> PAYTURE_IN_PAY
            PaymentMethodType.PAYTURE_WALLET -> PAYTURE_WALLET
            PaymentMethodType.SBP -> SBP
            PaymentMethodType.PODELI -> PODELI
            PaymentMethodType.PREPAID -> PREPAID
            PaymentMethodType.POSTPAID -> POSTPAID
            PaymentMethodType.GIFT_CERTIFICATE -> GIFT_CERTIFICATE
            PaymentMethodType.FREE -> FREE
        }
    }
}
