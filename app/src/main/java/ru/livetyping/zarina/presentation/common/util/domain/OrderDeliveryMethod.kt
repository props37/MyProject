package ru.livetyping.zarina.presentation.common.util.domain

import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.order.DeliveryMethodType

val DeliveryMethodType.nameResId: Int
    get() = when (this) {
        DeliveryMethodType.DELIVERY_SERVICE, DeliveryMethodType.EXPRESS -> {
            R.string.order_delivery_method_express_delivery
        }

        DeliveryMethodType.POST -> R.string.order_delivery_method_post
        DeliveryMethodType.PICKUP -> R.string.order_delivery_method_pick_up_point
        DeliveryMethodType.RETAIL, DeliveryMethodType.PICKUP_IN_STORE -> {
            R.string.order_delivery_method_pick_up_from_store
        }

        DeliveryMethodType.YANDEX -> R.string.order_delivery_method_yandex_express
    }
