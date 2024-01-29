package ru.zarina.zarina.domain.rework.filter

import ru.zarina.zarina.domain.rework.common.Sorting

data class Filters(
    val sorting: ListFilter<SortFilterItem>?,
    val price: PriceFilter?,
    val materials: ListFilter<MaterialFilterItem>?,
    val sizes: ListFilter<SizeFilterItem>?,
    val colors: ListFilter<ColorFilterItem>?,
    val deliveryAvailability: ToggleFilter?,
    val storePickupAvailability: ToggleFilter?,
) : Iterable<Filter> {
    override fun iterator(): Iterator<Filter> = iterator {
        if (sorting != null) yield(sorting)
        if (price != null) yield(price)
        if (materials != null) yield(materials)
        if (sizes != null) yield(sizes)
        if (colors != null) yield(colors)
        if (deliveryAvailability != null) yield(deliveryAvailability)
        if (storePickupAvailability != null) yield(storePickupAvailability)
    }

    // Ignore sorting
    val isEmpty: Boolean
        get() = price?.isEmpty != false
                && materials?.isEmpty != false
                && sizes?.isEmpty != false
                && colors?.isEmpty != false
                && deliveryAvailability?.isEmpty != false
                && storePickupAvailability?.isEmpty != false

    companion object {
        val EMPTY: Filters
            get() = Filters(
                sorting = null,
                price = null,
                materials = null,
                sizes = null,
                colors = null,
                deliveryAvailability = null,
                storePickupAvailability = null,
            )

        fun create(
            sorting: ListFilter<SortFilterItem>? = null,
            price: PriceFilter? = null,
            materials: ListFilter<MaterialFilterItem>? = null,
            sizes: ListFilter<SizeFilterItem>? = null,
            colors: ListFilter<ColorFilterItem>? = null,
            availableForDelivery: ToggleFilter? = null,
            availableForStorePickup: ToggleFilter? = null,
        ): Filters = Filters(
            sorting = sorting,
            price = price,
            materials = materials,
            sizes = sizes,
            colors = colors,
            deliveryAvailability = availableForDelivery,
            storePickupAvailability = availableForStorePickup,
        )

        fun getDefaultSorting(selected: Sorting? = null): ListFilter<SortFilterItem> {
            val items = Sorting.entries.map {
                SortFilterItem.from(
                    sorting = it,
                    isSelected = it == selected,
                )
            }
            return ListFilter(items = items, isSingleSelection = true, type = Filter.Type.SORTING)
        }
    }
}

fun Filters.combineWith(availableFilters: Filters): Filters {
    val price = availableFilters.price?.let { this.price?.combineWith(it) ?: it }
    val materials = availableFilters.materials?.let { this.materials?.combineWith(it) ?: it }
    val sizes = availableFilters.sizes?.let { this.sizes?.combineWith(it) ?: it }
    val colors = availableFilters.colors?.let { this.colors?.combineWith(it) ?: it }
    val deliveryAvailability = this.deliveryAvailability ?: availableFilters.deliveryAvailability
    val storePickupAvailability =
        this.storePickupAvailability ?: availableFilters.storePickupAvailability
    return this.copy(
        price = price,
        materials = materials,
        sizes = sizes,
        colors = colors,
        deliveryAvailability = deliveryAvailability,
        storePickupAvailability = storePickupAvailability,
    )
}

@Suppress("UNCHECKED_CAST")
fun Filters.updateWith(filter: Filter): Filters {
    return when (filter.type) {
        Filter.Type.SORTING -> {
            val castedFilter = checkNotNull(filter as? ListFilter<SortFilterItem>) {
                "Could not cast ${Filter.Type.SORTING} $filter to ListFilter<SortFilterItem>"
            }
            this.copy(sorting = castedFilter)
        }

        Filter.Type.PRICE -> {
            check(filter is PriceFilter) {
                "Could not cast ${Filter.Type.PRICE} $filter to PriceFilter"
            }
            this.copy(price = filter)
        }

        Filter.Type.MATERIALS -> {
            val castedFilter = checkNotNull(filter as? ListFilter<MaterialFilterItem>) {
                "Could not cast ${Filter.Type.MATERIALS} $filter to ListFilter<MaterialFilterItem>"
            }
            this.copy(materials = castedFilter)
        }

        Filter.Type.SIZES -> {
            val castedFilter = checkNotNull(filter as? ListFilter<SizeFilterItem>) {
                "Could not cast ${Filter.Type.SIZES} $filter to ListFilter<SizeFilterItem>"
            }
            this.copy(sizes = castedFilter)
        }

        Filter.Type.COLORS -> {
            val castedFilter = checkNotNull(filter as? ListFilter<ColorFilterItem>) {
                "Could not cast ${Filter.Type.COLORS} $filter to ListFilter<ColorFilterItem>"
            }
            this.copy(colors = castedFilter)
        }

        Filter.Type.DELIVERY_AVAILABILITY -> {
            check(filter is ToggleFilter) {
                "Could not cast ${Filter.Type.DELIVERY_AVAILABILITY} $filter to ToggleFilter"
            }
            this.copy(deliveryAvailability = filter)
        }

        Filter.Type.STORE_PICKUP_AVAILABILITY -> {
            check(filter is ToggleFilter) {
                "Could not cast ${Filter.Type.STORE_PICKUP_AVAILABILITY} $filter to ToggleFilter"
            }
            this.copy(storePickupAvailability = filter)
        }
    }
}
