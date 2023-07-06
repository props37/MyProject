package ru.zarina.zarina.domain

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Filtration(
    val priceLimits: PriceRange?,
    val price: PriceRange? = priceLimits,
    val colors: ListFilter<ColorFilterItem>? = null,
) : Parcelable {

    fun isEmpty() = price == priceLimits
            && colors?.isApplied != true

}

interface Filter {
    val isApplied: Boolean
    val isSingleSelection: Boolean
}

@Parcelize
open class ListFilterItem(
    open val id: String,
    open val name: String,
    open val isSelected: Boolean,
) : Parcelable

data class ColorFilterItem(
    val color: Color,
    override val isSelected: Boolean,
) : ListFilterItem(color.id, color.name, isSelected)

@Parcelize
data class ListFilter<T : ListFilterItem>(
    val items: List<T>,
    override val isSingleSelection: Boolean,
) : Filter, Parcelable {
    @IgnoredOnParcel
    override val isApplied = items.any { it.isSelected }
}
