package ru.livetyping.zarina.presentation.common.util

import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.product.ProductAvailabilityInStore

val ProductAvailabilityInStore.Amount.nameResId: Int
    get() = when (this) {
        ProductAvailabilityInStore.Amount.LAST_CHANCE -> R.string.last_chance
        ProductAvailabilityInStore.Amount.LITTLE -> R.string.amount_little
        ProductAvailabilityInStore.Amount.ENOUGH -> R.string.amount_enough
        ProductAvailabilityInStore.Amount.A_LOT -> R.string.amount_a_lot
    }
