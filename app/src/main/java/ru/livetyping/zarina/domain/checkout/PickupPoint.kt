package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.location.Location

data class PickupPoint(
    val id: Id,
    val title: String,
    val address: String,
    val location: Location,
    val isFittingAvailable: Boolean,
    val isPaymentByCardAvailable: Boolean,
) {
    @JvmInline
    value class Id(val value: Long)
}
