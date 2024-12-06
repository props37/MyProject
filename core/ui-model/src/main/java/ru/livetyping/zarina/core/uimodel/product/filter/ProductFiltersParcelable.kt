package ru.livetyping.zarina.core.uimodel.product.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilter
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.product.filter.ProductToggleFilter
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilter

@Serializable
@Parcelize
public data class ProductFiltersParcelable(
    val sorting: ProductListFilterParcelable?,
    val price: ProductPriceFilterParcelable?,
    val materials: ProductListFilterParcelable?,
    val sizes: ProductListFilterParcelable?,
    val colors: ProductListFilterParcelable?,
    val deliveryAvailability: Boolean?,
    val storePickupAvailability: Boolean?,
    val pickupStores: ProductListFilterParcelable?,
) : Parcelable {
    public fun toFilters(): ProductFilters {
        val sorting = sorting?.let { sorting ->
            ProductListFilter(
                items = sorting.items.map { it.toSortFilterItem() },
                isSingleSelection = sorting.isSingleSelection,
                type = sorting.type.toFilterType(),
            )
        }
        val materials = materials?.let { materials ->
            ProductListFilter(
                items = materials.items.map { it.toMaterialFilterItem() },
                isSingleSelection = materials.isSingleSelection,
                type = materials.type.toFilterType(),
            )
        }
        val sizes = sizes?.let { sizes ->
            ProductListFilter(
                items = sizes.items.map { it.toSizeFilterItem() },
                isSingleSelection = sizes.isSingleSelection,
                type = sizes.type.toFilterType(),
            )
        }
        val colors = colors?.let { colors ->
            ProductListFilter(
                items = colors.items.map { it.toColorFilterItem() },
                isSingleSelection = colors.isSingleSelection,
                type = colors.type.toFilterType(),
            )
        }
        val deliveryAvailability = deliveryAvailability?.let {
            ProductToggleFilter(isEnabled = it, type = ProductFilter.Type.DELIVERY_AVAILABILITY)
        }
        val storePickupAvailability = storePickupAvailability?.let {
            ProductToggleFilter(isEnabled = it, type = ProductFilter.Type.STORE_PICKUP_AVAILABILITY)
        }
        val pickupStores = pickupStores?.let {
            ProductListFilter(
                items = pickupStores.items.map { it.toPickupStoreFilterItem() },
                isSingleSelection = pickupStores.isSingleSelection,
                type = pickupStores.type.toFilterType(),
            )
        }
        return ProductFilters(
            sorting = sorting,
            price = price?.toPriceFilter(),
            materials = materials,
            sizes = sizes,
            colors = colors,
            deliveryAvailability = deliveryAvailability,
            storePickupAvailability = storePickupAvailability,
            pickupStores = pickupStores,
        )
    }

    public companion object {
        public fun from(filters: ProductFilters): ProductFiltersParcelable {
            return ProductFiltersParcelable(
                sorting = filters.sorting?.let { ProductListFilterParcelable.from(it) },
                price = filters.price?.let { ProductPriceFilterParcelable.from(it) },
                materials = filters.materials?.let { ProductListFilterParcelable.from(it) },
                sizes = filters.sizes?.let { ProductListFilterParcelable.from(it) },
                colors = filters.colors?.let { ProductListFilterParcelable.from(it) },
                deliveryAvailability = filters.deliveryAvailability?.isEnabled,
                storePickupAvailability = filters.storePickupAvailability?.isEnabled,
                pickupStores = filters.pickupStores?.let { ProductListFilterParcelable.from(it) },
            )
        }
    }
}
