package ru.livetyping.zarina.domain.filter

import ru.livetyping.zarina.domain.common.Sorting

data class Filters(
    val sorting: ListFilter<SortFilterItem>?,
    val price: PriceFilter?,
    val materials: ListFilter<MaterialFilterItem>?,
    val sizes: ListFilter<SizeFilterItem>?,
    val colors: ListFilter<ColorFilterItem>?,
    val deliveryAvailability: ToggleFilter?,
    val storePickupAvailability: ToggleFilter?,
    val pickupStores: ListFilter<PickupStoreFilterItem>?,
) : Iterable<Filter> {
    override fun iterator(): Iterator<Filter> = iterator {
        if (sorting != null) yield(sorting)
        if (price != null) yield(price)
        if (materials != null) yield(materials)
        if (sizes != null) yield(sizes)
        if (colors != null) yield(colors)
        if (deliveryAvailability != null) yield(deliveryAvailability)
        if (storePickupAvailability != null) yield(storePickupAvailability)
        if (pickupStores != null) yield(pickupStores)
    }

    val appliedFilterCount: Int by lazy {
        var result = 0
        if (price?.isApplied == true) result++
        if (materials?.isApplied == true) result += materials.selectedItems.size
        if (sizes?.isApplied == true) result += sizes.selectedItems.size
        if (colors?.isApplied == true) result += colors.selectedItems.size
        if (deliveryAvailability?.isApplied == true) result++
        if (storePickupAvailability?.isApplied == true) result++
        if (pickupStores?.isApplied == true) result++
        result
    }

    val hasAppliedIgnoringSorting: Boolean by lazy {
        price?.isApplied == true
                || materials?.isApplied == true
                || sizes?.isApplied == true
                || colors?.isApplied == true
                || deliveryAvailability?.isApplied == true
                || storePickupAvailability?.isApplied == true
                || pickupStores?.isApplied == true
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
                pickupStores = null,
            )

        fun create(
            sorting: ListFilter<SortFilterItem>? = null,
            price: PriceFilter? = null,
            materials: ListFilter<MaterialFilterItem>? = null,
            sizes: ListFilter<SizeFilterItem>? = null,
            colors: ListFilter<ColorFilterItem>? = null,
            availableForDelivery: ToggleFilter? = null,
            availableForStorePickup: ToggleFilter? = null,
            pickupStores: ListFilter<PickupStoreFilterItem>? = null,
        ): Filters = Filters(
            sorting = sorting,
            price = price,
            materials = materials,
            sizes = sizes,
            colors = colors,
            deliveryAvailability = availableForDelivery,
            storePickupAvailability = availableForStorePickup,
            pickupStores = pickupStores,
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
    val pickupStores = if (storePickupAvailability?.isEnabled == true) {
        available.pickupStores?.let { this.pickupStores?.coerceInAvailable(it) ?: it }
    } else null
    return this.copy(
        price = price,
        materials = materials,
        sizes = sizes,
        colors = colors,
        deliveryAvailability = deliveryAvailability,
        storePickupAvailability = storePickupAvailability,
        pickupStores = pickupStores,
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

        Filter.Type.PICKUP_STORES -> {
            val castedFilter = checkNotNull(filter as? ListFilter<PickupStoreFilterItem>) {
                "Could not cast ${Filter.Type.PICKUP_STORES} $filter to ListFilter<PickupStoreFilterItem>"
            }
            this.copy(pickupStores = castedFilter)
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
