package ru.livetyping.zarina.core.uimodel.product.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilter

@Serializable
@Parcelize
public enum class ProductFilterTypeParcelable : Parcelable {
    SORTING,
    PRICE,
    MATERIALS,
    SIZES,
    COLORS,
    DELIVERY_AVAILABILITY,
    STORE_PICKUP_AVAILABILITY,
    PICKUP_STORES;

    public fun toFilterType(): ProductFilter.Type = when (this) {
        SORTING -> ProductFilter.Type.SORTING
        PRICE -> ProductFilter.Type.PRICE
        MATERIALS -> ProductFilter.Type.MATERIALS
        SIZES -> ProductFilter.Type.SIZES
        COLORS -> ProductFilter.Type.COLORS
        DELIVERY_AVAILABILITY -> ProductFilter.Type.DELIVERY_AVAILABILITY
        STORE_PICKUP_AVAILABILITY -> ProductFilter.Type.STORE_PICKUP_AVAILABILITY
        PICKUP_STORES -> ProductFilter.Type.PICKUP_STORES
    }

    public companion object {
        public fun from(type: ProductFilter.Type): ProductFilterTypeParcelable = when (type) {
            ProductFilter.Type.SORTING -> SORTING
            ProductFilter.Type.PRICE -> PRICE
            ProductFilter.Type.MATERIALS -> MATERIALS
            ProductFilter.Type.SIZES -> SIZES
            ProductFilter.Type.COLORS -> COLORS
            ProductFilter.Type.DELIVERY_AVAILABILITY -> DELIVERY_AVAILABILITY
            ProductFilter.Type.STORE_PICKUP_AVAILABILITY -> STORE_PICKUP_AVAILABILITY
            ProductFilter.Type.PICKUP_STORES -> PICKUP_STORES
        }
    }
}
