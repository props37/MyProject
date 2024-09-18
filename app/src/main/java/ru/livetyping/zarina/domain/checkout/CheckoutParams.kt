package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.domain.order.DeliveryMethodType

sealed class CheckoutParams(
    open val cartType: CartType,
    open val deliveryMethodType: DeliveryMethodType,
    open val cityKladrId: KladrId,
)
