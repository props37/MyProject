package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.order.DeliveryMethodType

data class PickupPointDeliveryCheckoutParams(
    override val cartType: CartType,
    override val deliveryMethodType: DeliveryMethodType,
    override val customer: Customer,
    val city: City,
    val pickupPointId: PickupPoint.Id,
    val deliveryTypeId: PickupPointDetails.DeliveryType.Id,
    val dateTimePeriodId: PickupPointDetails.DeliveryType.DateTimePeriod.Id,
) : CheckoutParams(
    cartType = cartType,
    deliveryMethodType = deliveryMethodType,
    cityKladrId = city.id,
    customer = customer,
)
