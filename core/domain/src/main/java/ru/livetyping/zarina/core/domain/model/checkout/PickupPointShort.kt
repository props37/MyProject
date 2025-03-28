package ru.livetyping.zarina.core.domain.model.checkout

import ru.livetyping.zarina.core.domain.model.common.Location

public data class PickupPointShort(
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
