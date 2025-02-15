package ru.livetyping.zarina.core.uicommon

import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilter
import ru.livetyping.zarina.core.resource.R as RCommon

public val ProductFilter.Type.nameResId: Int
    get() = when (this) {
        ProductFilter.Type.SORTING -> RCommon.string.res_filter_sorting
        ProductFilter.Type.PRICE -> RCommon.string.res_filter_price
        ProductFilter.Type.MATERIALS -> RCommon.string.res_filter_materials
        ProductFilter.Type.SIZES -> RCommon.string.res_filter_size
        ProductFilter.Type.COLORS -> RCommon.string.res_filter_color
        ProductFilter.Type.DELIVERY_AVAILABILITY -> RCommon.string.res_filter_delivery_availability
        ProductFilter.Type.STORE_PICKUP_AVAILABILITY -> RCommon.string.res_filter_store_pickup_availability
        ProductFilter.Type.PICKUP_STORES -> RCommon.string.res_filter_pickup_store
    }
