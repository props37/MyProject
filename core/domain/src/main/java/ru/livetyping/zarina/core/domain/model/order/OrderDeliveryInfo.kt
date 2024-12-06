package ru.livetyping.zarina.core.domain.model.order

import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType

// Marked as stable on config/compose/stability_config.txt
public data class OrderDeliveryInfo(
    val type: DeliveryMethodType,
)
