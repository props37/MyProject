package ru.livetyping.zarina.core.uicommon

import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.resource.R as RCommon

public val DeliveryMethodType.nameResId: Int
    get() = when (this) {
        DeliveryMethodType.COURIER, DeliveryMethodType.COURIER_EXPRESS -> {
            RCommon.string.res_delivery_method_courier
        }

        DeliveryMethodType.POST -> RCommon.string.res_delivery_method_post
        DeliveryMethodType.PICKUP_FROM_PICKUP_POINT -> RCommon.string.res_delivery_method_pickup_from_pickup_point
        DeliveryMethodType.PICKUP_FROM_STORE_WAREHOUSE -> RCommon.string.res_delivery_method_pickup_from_store_warehouse
        DeliveryMethodType.PICKUP_FROM_STORE -> RCommon.string.res_delivery_method_pickup_from_store
        DeliveryMethodType.YANDEX_EXPRESS -> RCommon.string.res_delivery_method_yandex_express
    }
