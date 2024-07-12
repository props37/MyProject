package ru.livetyping.zarina.presentation.common.util.domain

import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.order.OrderDeliveryMethod

val OrderDeliveryMethod.nameResId: Int
    get() = when (this) {
        OrderDeliveryMethod.DELIVERY_SERVICE, OrderDeliveryMethod.EXPRESS -> {
            R.string.order_delivery_method_express_delivery
        }

        OrderDeliveryMethod.POST -> R.string.order_delivery_method_post
        OrderDeliveryMethod.PICKUP -> R.string.order_delivery_method_pick_up_point
        OrderDeliveryMethod.RETAIL -> R.string.order_delivery_method_pick_up_from_store
        OrderDeliveryMethod.YANDEX -> R.string.order_delivery_method_yandex_express
    }
