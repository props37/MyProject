package ru.zarina.zarina.domain

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Filtration(
    val priceLimits: PriceRange? = null,
    val price: PriceRange? = priceLimits,
    val categories: ListFilter? = null,
    val colors: ListFilter? = null,
    val attributes: ListFilter? = null,
    val materials: ListFilter? = null,
    val sizes: ListFilter? = null,
    val isShippingAvailable: Boolean? = null,
    val isPickupAvailable: Boolean? = null,
    val pickupShop: Shop? = null,
) : Parcelable {

    fun isEmpty() = price == priceLimits
            && categories?.isEmpty != true
            && colors?.isEmpty != true
            && attributes?.isEmpty != true
            && materials?.isEmpty != true
            && sizes?.isEmpty != true
            && isShippingAvailable != true
            && isPickupAvailable != true
            && pickupShop == null

    companion object {
        val EMPTY
            get() = Filtration()
    }

}

interface Filter {
    val isEmpty: Boolean
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
    override val isEmpty = items.none { it.isSelected }
}
