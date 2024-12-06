package ru.livetyping.zarina.core.uimodel.product.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilter
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilterItem

@Serializable
@Parcelize
public data class ProductListFilterParcelable(
    val items: List<ProductListFilterItemParcelable>,
    val isSingleSelection: Boolean,
    val type: ProductFilterTypeParcelable,
) : Parcelable {
    public fun toListFilter(): ProductListFilter<ProductListFilterItem> {
        val items = when (type) {
            ProductFilterTypeParcelable.SORTING -> items.map { it.toSortFilterItem() }
            ProductFilterTypeParcelable.MATERIALS -> items.map { it.toMaterialFilterItem() }
            ProductFilterTypeParcelable.SIZES -> items.map { it.toSizeFilterItem() }
            ProductFilterTypeParcelable.COLORS -> items.map { it.toColorFilterItem() }

            ProductFilterTypeParcelable.PRICE -> {
                error("Could not map ${ProductFilterTypeParcelable.PRICE} filter to ListFilter")
            }

            ProductFilterTypeParcelable.DELIVERY_AVAILABILITY -> {
                error("Could not map ${ProductFilterTypeParcelable.DELIVERY_AVAILABILITY} filter to ListFilter")
            }

            ProductFilterTypeParcelable.STORE_PICKUP_AVAILABILITY -> {
                error("Could not map ${ProductFilterTypeParcelable.STORE_PICKUP_AVAILABILITY} filter to ListFilter")
            }

            ProductFilterTypeParcelable.PICKUP_STORES -> items.map { it.toPickupStoreFilterItem() }
        }
        return ProductListFilter(
            items = items,
            isSingleSelection = isSingleSelection,
            type = type.toFilterType(),
        )
    }

    public companion object {
        public fun from(listFilter: ProductListFilter<*>): ProductListFilterParcelable {
            return ProductListFilterParcelable(
                items = listFilter.items.map { ProductListFilterItemParcelable.from(it) },
                isSingleSelection = listFilter.isSingleSelection,
                type = ProductFilterTypeParcelable.from(listFilter.type),
            )
        }
    }
}
