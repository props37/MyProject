package ru.livetyping.zarina.presentation.common.util.domain

import androidx.compose.runtime.Stable
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.Sorting

@Stable
val Sorting.nameResId: Int
    get() = when (this) {
        Sorting.NEW -> R.string.sorting_new
        Sorting.POPULAR -> R.string.sorting_popular
        Sorting.DISCOUNT -> R.string.sorting_discount
        Sorting.PRICE_LOW_TO_HIGH -> R.string.sorting_price_low_to_high
        Sorting.PRICE_HIGH_TO_LOW -> R.string.sorting_price_high_to_low
    }
