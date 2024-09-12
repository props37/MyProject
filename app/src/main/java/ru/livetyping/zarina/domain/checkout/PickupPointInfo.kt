package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.location.Location

data class PickupPointInfo(
    override val id: Id,
    override val title: String,
    override val address: String,
    override val location: Location,
    override val isFittingAvailable: Boolean,
    override val isPaymentByCardAvailable: Boolean,
    override val availablePaymentMethods: Set<PaymentMethod>,
) : PickupPoint(
    id = id,
    title = title,
    address = address,
    location = location,
    isFittingAvailable = isFittingAvailable,
    isPaymentByCardAvailable = isPaymentByCardAvailable,
    availablePaymentMethods = availablePaymentMethods,
)
