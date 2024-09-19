package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.order.DeliveryMethodType

// TODO: [High] Rename
data class CourierDeliveryCheckoutParams(
    override val cartType: CartType,
    override val deliveryMethodType: DeliveryMethodType,
    val address: CheckoutAddress,
    val deliveryOptionId: DeliveryOption.Id,
    val dateTimePeriodId: DeliveryOption.DateTimePeriod.Id,
) : CheckoutParams(
    cartType = cartType,
    deliveryMethodType = deliveryMethodType,
    cityKladrId = address.city.id,
)
