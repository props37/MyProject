package ru.zarina.zarina.ui.common.util.domain

import androidx.compose.runtime.Stable
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.common.Sorting

@Stable
val Sorting.stringResId: Int
    get() = when (this) {
        Sorting.NEW -> R.string.sorting_new
        Sorting.POPULAR -> R.string.sorting_popular
        Sorting.DISCOUNT -> R.string.sorting_discount
        Sorting.PRICE_LOW_TO_HIGH -> R.string.sorting_price_low_to_high
        Sorting.PRICE_HIGH_TO_LOW -> R.string.sorting_price_high_to_low
    }
