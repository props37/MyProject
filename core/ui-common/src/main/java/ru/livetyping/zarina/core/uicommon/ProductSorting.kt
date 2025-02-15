package ru.livetyping.zarina.core.uicommon

import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.resource.R as RCommon

public val ProductSorting.nameResId: Int
    get() = when (this) {
        ProductSorting.NEW -> RCommon.string.res_sorting_new
        ProductSorting.POPULAR -> RCommon.string.res_sorting_popular
        ProductSorting.DISCOUNT -> RCommon.string.res_sorting_discount
        ProductSorting.PRICE_LOW_TO_HIGH -> RCommon.string.res_sorting_price_low_to_high
        ProductSorting.PRICE_HIGH_TO_LOW -> RCommon.string.res_sorting_price_high_to_low
    }
