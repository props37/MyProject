package ru.zarina.zarina.domain

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Filtration(
    val priceLimits: PriceRange?,
    val price: PriceRange? = priceLimits,
    val colors: ListFilter? = null,
) : Parcelable {

    fun isEmpty() = price == priceLimits
            && colors?.isApplied != true

}

interface Filter {
    val isApplied: Boolean
    val isSingleSelection: Boolean
}

@Parcelize
data class ListFilter(
    val items: List<Item>,
    override val isSingleSelection: Boolean,
) : Filter, Parcelable {

    @Parcelize
    data class Item(
        val id: String,
        val name: String,
        val isSelected: Boolean,
        val color: Color? = null,
    ) : Parcelable

    @IgnoredOnParcel
    override val isApplied = items.any { it.isSelected }
}
