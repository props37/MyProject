package ru.livetyping.zarina.core.domain.analytics

import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.analytics.model.DeliveryMethodType as AppMetricaDeliveryMethodType

public fun DeliveryMethodType.toAppMetricaDeliveryMethodType(): AppMetricaDeliveryMethodType {
    return when (this) {
        DeliveryMethodType.COURIER -> AppMetricaDeliveryMethodType.COURIER
        DeliveryMethodType.POST -> AppMetricaDeliveryMethodType.POST
        DeliveryMethodType.PICKUP_FROM_PICKUP_POINT -> AppMetricaDeliveryMethodType.PICKUP_FROM_PICKUP_POINT
        DeliveryMethodType.PICKUP_FROM_STORE_WAREHOUSE -> AppMetricaDeliveryMethodType.PICKUP_FROM_STORE_WAREHOUSE
        DeliveryMethodType.PICKUP_FROM_STORE -> AppMetricaDeliveryMethodType.PICKUP_FROM_STORE
        DeliveryMethodType.YANDEX_EXPRESS -> AppMetricaDeliveryMethodType.YANDEX_EXPRESS
        DeliveryMethodType.COURIER_EXPRESS -> AppMetricaDeliveryMethodType.COURIER_EXPRESS
    }
}
