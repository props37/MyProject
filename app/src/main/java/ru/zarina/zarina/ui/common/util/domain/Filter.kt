package ru.zarina.zarina.ui.common.util.domain

import androidx.compose.runtime.Stable
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.filter.Filter

@Stable
val Filter.Type.nameResId: Int
    get() = when (this) {
        Filter.Type.SORTING -> R.string.sorting
        Filter.Type.PRICE -> R.string.price
        Filter.Type.MATERIALS -> R.string.composition
        Filter.Type.SIZES -> R.string.size
        Filter.Type.COLORS -> R.string.color
        Filter.Type.DELIVERY_AVAILABILITY -> R.string.available_for_delivery
        Filter.Type.STORE_PICKUP_AVAILABILITY -> R.string.available_for_store_pickup
    }
