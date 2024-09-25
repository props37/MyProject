package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.domain.store.Store

data class StorePickupCheckoutParams(
    override val cartType: CartType,
    override val deliveryMethodType: DeliveryMethodType,
    override val customer: Customer,
    val city: City,
    val store: Store,
) : CheckoutParams(
    cartType = cartType,
    deliveryMethodType = deliveryMethodType,
    cityKladrId = city.id,
    customer = customer,
)
