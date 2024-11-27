package ru.livetyping.zarina.core.domain.model.order

import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType

public data class OrderDeliveryInfo(
    val type: DeliveryMethodType,
)
