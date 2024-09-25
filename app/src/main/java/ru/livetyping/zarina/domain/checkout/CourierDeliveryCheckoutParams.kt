package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.order.DeliveryMethodType

data class CourierDeliveryCheckoutParams(
    override val cartType: CartType,
    override val deliveryMethodType: DeliveryMethodType,
    override val customer: Customer,
    val address: CheckoutAddress,
    val deliveryOption: DeliveryOption,
    val dateTimePeriod: DeliveryOption.DateTimePeriod,
) : CheckoutParams(
    cartType = cartType,
    deliveryMethodType = deliveryMethodType,
    cityKladrId = address.city.id,
    customer = customer,
)
