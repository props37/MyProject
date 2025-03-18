package ru.livetyping.zarina.core.uicommon

import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.resource.R as RCommon

public val DeliveryMethodType.nameResId: Int
    get() = when (this) {
        DeliveryMethodType.DELIVERY_SERVICE, DeliveryMethodType.EXPRESS -> {
            RCommon.string.res_delivery_method_express_delivery
        }

        DeliveryMethodType.POST -> RCommon.string.res_delivery_method_post
        DeliveryMethodType.PICKUP -> RCommon.string.res_delivery_method_pick_up_point
        DeliveryMethodType.PICKUP_IN_STORE -> RCommon.string.res_delivery_method_pick_up_in_store
        DeliveryMethodType.RETAIL -> RCommon.string.res_delivery_method_pick_up_from_store
        DeliveryMethodType.YANDEX -> RCommon.string.res_delivery_method_yandex_express
    }
