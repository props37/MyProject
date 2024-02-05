package ru.zarina.zarina.ui.model.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.zarina.zarina.domain.rework.common.Sorting

@Parcelize
enum class SortingParcelable : Parcelable {
    NEW,
    POPULAR,
    DISCOUNT,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW;

    fun toSorting(): Sorting = when (this) {
        NEW -> Sorting.NEW
        POPULAR -> Sorting.POPULAR
        DISCOUNT -> Sorting.DISCOUNT
        PRICE_LOW_TO_HIGH -> Sorting.PRICE_LOW_TO_HIGH
        PRICE_HIGH_TO_LOW -> Sorting.PRICE_HIGH_TO_LOW
    }

    companion object {
        fun from(sorting: Sorting): SortingParcelable = when (sorting) {
            Sorting.NEW -> NEW
            Sorting.POPULAR -> POPULAR
            Sorting.DISCOUNT -> DISCOUNT
            Sorting.PRICE_LOW_TO_HIGH -> PRICE_LOW_TO_HIGH
            Sorting.PRICE_HIGH_TO_LOW -> PRICE_HIGH_TO_LOW
        }
    }
}
