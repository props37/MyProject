package ru.livetyping.zarina.core.domain.model.checkout

import ru.livetyping.zarina.core.domain.model.common.Location

public sealed class PickupPoint(
    public open val id: Id,
    public open val title: String,
    public open val address: String,
    public open val location: Location,
    public open val isFittingAvailable: Boolean,
    public open val isPaymentByCardAvailable: Boolean,
    public open val availablePaymentMethods: Set<PaymentMethod>,
) {
    @JvmInline
    public value class Id(public val value: String)

    public enum class PaymentMethod { CASH, CARD }
}
