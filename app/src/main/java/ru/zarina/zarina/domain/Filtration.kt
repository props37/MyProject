package ru.zarina.zarina.domain

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Filtration(
    val priceLimits: PriceRange? = null,
    val price: PriceRange? = priceLimits,
    val categories: TreeFilter? = null,
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

@Parcelize
data class TreeFilter(
    val items: List<Item>,
    override val isSingleSelection: Boolean = false,
) : Filter, Parcelable {

    @Parcelize
    data class Item(
        val id: String,
        val name: String,
        val isExplicitSelected: Boolean,
        val color: Color? = null,
        val children: List<Item> = emptyList(),
    ) : Parcelable {

        @IgnoredOnParcel
        val isSelected: Boolean by lazy { if (children.isNotEmpty()) children.all { it.isSelected } else isExplicitSelected }

        fun getFlattenedChildren(): List<Item> =
            children + children.flatMap { it.getFlattenedChildren() }

    }

    @IgnoredOnParcel
    override val isEmpty =
        items.none { item -> item.isSelected || item.getFlattenedChildren().any { it.isSelected } }

}
