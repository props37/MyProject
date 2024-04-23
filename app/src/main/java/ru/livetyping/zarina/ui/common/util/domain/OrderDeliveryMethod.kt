package ru.livetyping.zarina.ui.common.util.domain

import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.order.OrderDeliveryMethod

val OrderDeliveryMethod.nameResId: Int
    get() = when (this) {
        OrderDeliveryMethod.DELIVERY_SERVICE -> R.string.order_delivery_method_express_delivery
        OrderDeliveryMethod.POST -> R.string.order_delivery_method_post
        OrderDeliveryMethod.PICK_UP -> R.string.order_delivery_method_pick_up_point
        OrderDeliveryMethod.RETAIL -> R.string.order_delivery_method_pick_up_from_shop
        OrderDeliveryMethod.YANDEX -> R.string.order_delivery_method_yandex_express
    }
