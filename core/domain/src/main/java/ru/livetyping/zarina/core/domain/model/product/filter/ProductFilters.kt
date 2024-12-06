package ru.livetyping.zarina.core.domain.model.product.filter

import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductColorFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilter
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductMaterialFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductPickupStoreFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductSizeFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductSortFilterItem

// Marked as stable on config/compose/stability_config.txt
public data class ProductFilters(
    val sorting: ProductListFilter<ProductSortFilterItem>?,
    val price: ProductPriceFilter?,
    val materials: ProductListFilter<ProductMaterialFilterItem>?,
    val sizes: ProductListFilter<ProductSizeFilterItem>?,
    val colors: ProductListFilter<ProductColorFilterItem>?,
    val deliveryAvailability: ProductToggleFilter?,
    val storePickupAvailability: ProductToggleFilter?,
    val pickupStores: ProductListFilter<ProductPickupStoreFilterItem>?,
) : Iterable<ProductFilter<*>> {
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

    val isEmptyIgnoringSorting: Boolean by lazy {
        price?.isEmpty != false
                && materials?.isEmpty != false
                && sizes?.isEmpty != false
                && colors?.isEmpty != false
                && deliveryAvailability?.isEmpty != false
                && storePickupAvailability?.isEmpty != false
                && pickupStores?.isEmpty != false
    }

    override fun iterator(): Iterator<ProductFilter<*>> = iterator {
        if (sorting != null) yield(sorting)
        if (price != null) yield(price)
        if (materials != null) yield(materials)
        if (sizes != null) yield(sizes)
        if (colors != null) yield(colors)
        if (deliveryAvailability != null) yield(deliveryAvailability)
        if (storePickupAvailability != null) yield(storePickupAvailability)
        if (pickupStores != null) yield(pickupStores)
    }

    public fun coerceInAvailable(available: ProductFilters): ProductFilters {
        val sorting = available.sorting?.let { this.sorting?.coerceInAvailable(it) ?: it }
        val price = available.price?.let { this.price?.coerceInAvailable(it) ?: it }
        val materials = available.materials?.let { this.materials?.coerceInAvailable(it) ?: it }
        val sizes = available.sizes?.let { this.sizes?.coerceInAvailable(it) ?: it }
        val colors = available.colors?.let { this.colors?.coerceInAvailable(it) ?: it }
        val deliveryAvailability = available.deliveryAvailability?.let { this.deliveryAvailability?.coerceInAvailable(it) ?: it }
        val storePickupAvailability = available.storePickupAvailability?.let { this.storePickupAvailability?.coerceInAvailable(it) ?: it }
        val pickupStores = if (storePickupAvailability?.isEnabled == true) {
            available.pickupStores?.let { this.pickupStores?.coerceInAvailable(it) ?: it }
        } else null
        return this.copy(
            sorting = sorting,
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
    public fun updateWith(filter: ProductFilter<*>): ProductFilters {
        return when (filter.type) {
            ProductFilter.Type.SORTING -> {
                val castedFilter = checkNotNull(filter as? ProductListFilter<ProductSortFilterItem>) {
                    "Could not cast ${ProductFilter.Type.SORTING} $filter to ListFilter<SortFilterItem>"
                }
                this.copy(sorting = castedFilter)
            }

            ProductFilter.Type.PRICE -> {
                check(filter is ProductPriceFilter) {
                    "Could not cast ${ProductFilter.Type.PRICE} $filter to PriceFilter"
                }
                this.copy(price = filter)
            }

            ProductFilter.Type.MATERIALS -> {
                val castedFilter = checkNotNull(filter as? ProductListFilter<ProductMaterialFilterItem>) {
                    "Could not cast ${ProductFilter.Type.MATERIALS} $filter to ListFilter<MaterialFilterItem>"
                }
                this.copy(materials = castedFilter)
            }

            ProductFilter.Type.SIZES -> {
                val castedFilter = checkNotNull(filter as? ProductListFilter<ProductSizeFilterItem>) {
                    "Could not cast ${ProductFilter.Type.SIZES} $filter to ListFilter<SizeFilterItem>"
                }
                this.copy(sizes = castedFilter)
            }

            ProductFilter.Type.COLORS -> {
                val castedFilter = checkNotNull(filter as? ProductListFilter<ProductColorFilterItem>) {
                    "Could not cast ${ProductFilter.Type.COLORS} $filter to ListFilter<ColorFilterItem>"
                }
                this.copy(colors = castedFilter)
            }

            ProductFilter.Type.DELIVERY_AVAILABILITY -> {
                check(filter is ProductToggleFilter) {
                    "Could not cast ${ProductFilter.Type.DELIVERY_AVAILABILITY} $filter to ToggleFilter"
                }
                this.copy(deliveryAvailability = filter)
            }

            ProductFilter.Type.STORE_PICKUP_AVAILABILITY -> {
                check(filter is ProductToggleFilter) {
                    "Could not cast ${ProductFilter.Type.STORE_PICKUP_AVAILABILITY} $filter to ToggleFilter"
                }
                this.copy(storePickupAvailability = filter)
            }

            ProductFilter.Type.PICKUP_STORES -> {
                val castedFilter = checkNotNull(filter as? ProductListFilter<ProductPickupStoreFilterItem>) {
                    "Could not cast ${ProductFilter.Type.PICKUP_STORES} $filter to ListFilter<PickupStoreFilterItem>"
                }
                this.copy(pickupStores = castedFilter)
            }
        }
    }

    public fun reset(): ProductFilters {
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

    public companion object {
        public val EMPTY: ProductFilters
            get() = ProductFilters(
                sorting = null,
                price = null,
                materials = null,
                sizes = null,
                colors = null,
                deliveryAvailability = null,
                storePickupAvailability = null,
                pickupStores = null,
            )

        public fun create(
            sorting: ProductListFilter<ProductSortFilterItem>? = null,
            price: ProductPriceFilter? = null,
            materials: ProductListFilter<ProductMaterialFilterItem>? = null,
            sizes: ProductListFilter<ProductSizeFilterItem>? = null,
            colors: ProductListFilter<ProductColorFilterItem>? = null,
            availableForDelivery: ProductToggleFilter? = null,
            availableForStorePickup: ProductToggleFilter? = null,
            pickupStores: ProductListFilter<ProductPickupStoreFilterItem>? = null,
        ): ProductFilters = ProductFilters(
            sorting = sorting,
            price = price,
            materials = materials,
            sizes = sizes,
            colors = colors,
            deliveryAvailability = availableForDelivery,
            storePickupAvailability = availableForStorePickup,
            pickupStores = pickupStores,
        )

        public fun getDefaultSorting(
            selected: ProductSorting? = null,
        ): ProductListFilter<ProductSortFilterItem> {
            val items = ProductSorting.entries.map {
                ProductSortFilterItem.from(
                    sorting = it,
                    isSelected = it == selected,
                )
            }
            return ProductListFilter(
                items = items,
                isSingleSelection = true,
                type = ProductFilter.Type.SORTING,
            )
        }
    }
}
