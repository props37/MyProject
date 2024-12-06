package ru.livetyping.zarina.core.uimodel.product.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.filter.ProductPriceFilter
import ru.livetyping.zarina.core.uimodel.common.PriceRangeParcelable

@Serializable
@Parcelize
public data class ProductPriceFilterParcelable(
    val min: Int?,
    val max: Int?,
    val limits: PriceRangeParcelable,
) : Parcelable {
    public fun toPriceFilter(): ProductPriceFilter = ProductPriceFilter(
        min = min,
        max = max,
        limits = limits.toPriceRange(),
    )

    public companion object {
        public fun from(priceFilter: ProductPriceFilter): ProductPriceFilterParcelable {
            return ProductPriceFilterParcelable(
                min = priceFilter.min,
                max = priceFilter.max,
                limits = PriceRangeParcelable.from(priceFilter.limits),
            )
        }
    }
}
