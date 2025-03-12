package ru.livetyping.zarina.core.uicommon

import ru.livetyping.zarina.core.domain.model.product.ProductAvailabilityInStore
import ru.livetyping.zarina.core.resource.R as RCommon

public val ProductAvailabilityInStore.Amount.nameResId: Int
    get() = when (this) {
        ProductAvailabilityInStore.Amount.LAST_CHANCE -> RCommon.string.res_availability_amount_last_chance
        ProductAvailabilityInStore.Amount.LITTLE -> RCommon.string.res_availability_amount_little
        ProductAvailabilityInStore.Amount.ENOUGH -> RCommon.string.res_availability_amount_enough
        ProductAvailabilityInStore.Amount.A_LOT -> RCommon.string.res_availability_amount_a_lot
    }
