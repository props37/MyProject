package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.location.Location

sealed class PickupPoint(
    open val id: Id,
    open val title: String,
    open val address: String,
    open val location: Location,
    open val isFittingAvailable: Boolean,
    open val isPaymentByCardAvailable: Boolean,
    open val availablePaymentMethods: Set<PaymentMethod>,
) {
    @JvmInline
    value class Id(val value: Long)

    enum class PaymentMethod { CASH, CARD }
}
