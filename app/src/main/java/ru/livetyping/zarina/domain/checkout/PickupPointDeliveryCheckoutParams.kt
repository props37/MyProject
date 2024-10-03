package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.order.DeliveryMethodType

data class PickupPointDeliveryCheckoutParams(
    override val cartType: CartType,
    override val deliveryMethodType: DeliveryMethodType,
    override val customer: Customer,
    val city: City,
    val pickupPoint: PickupPointDetails,
    val deliveryType: PickupPointDetails.DeliveryType,
    val dateTimePeriod: PickupPointDetails.DeliveryType.DateTimePeriod,
) : CheckoutParams(
    cartType = cartType,
    deliveryMethodType = deliveryMethodType,
    cityKladrId = city.id,
    customer = customer,
)
