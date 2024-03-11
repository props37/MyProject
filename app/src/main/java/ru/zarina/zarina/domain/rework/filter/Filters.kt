package ru.zarina.zarina.domain.rework.filter

import ru.zarina.zarina.domain.common.Sorting

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

    val appliedFilterCount: Int by lazy {
        var result = 0
        if (price?.isEmpty == false) result++
        if (materials?.selectedItems != null) result += materials.selectedItems.size
        if (sizes?.selectedItems != null) result += sizes.selectedItems.size
        if (colors?.selectedItems != null) result += colors.selectedItems.size
        if (deliveryAvailability?.isEmpty == false) result++
        if (storePickupAvailability?.isEmpty == false) result++
        result
    }

    val isEmptyIgnoringSorting: Boolean by lazy {
        price?.isEmpty != false
                && materials?.isEmpty != false
                && sizes?.isEmpty != false
                && colors?.isEmpty != false
                && deliveryAvailability?.isEmpty != false
                && storePickupAvailability?.isEmpty != false
    }

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

fun Filters.coerceInAvailable(available: Filters): Filters {
    val price = available.price?.let { this.price?.coerceInAvailable(it) ?: it }
    val materials = available.materials?.let { this.materials?.coerceInAvailable(it) ?: it }
    val sizes = available.sizes?.let { this.sizes?.coerceInAvailable(it) ?: it }
    val colors = available.colors?.let { this.colors?.coerceInAvailable(it) ?: it }
    val deliveryAvailability = this.deliveryAvailability ?: available.deliveryAvailability
    val storePickupAvailability = this.storePickupAvailability ?: available.storePickupAvailability
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

fun Filters.reset(): Filters {
    return this.copy(
        price = this.price?.copy(min = null, max = null),
        materials = this.materials?.copy(
            items = this.materials.items.map { it.copy(isSelected = false) },
        ),
        sizes = this.sizes?.copy(
            items = this.sizes.items.map { it.copy(isSelected = false) },
        ),
        colors = this.colors?.copy(
            items = this.colors.items.map { it.copy(isSelected = false) },
        ),
        deliveryAvailability = this.deliveryAvailability?.copy(isEnabled = false),
        storePickupAvailability = this.storePickupAvailability?.copy(isEnabled = false),
    )
}
