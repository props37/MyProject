package ru.livetyping.zarina.core.domain.model.checkout

import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.model.store.Store

public sealed class CheckoutParams(
    public open val cartType: CartType,
    public open val deliveryMethod: DeliveryMethod,
    public open val cityKladrId: KladrId,
    public open val recipient: Recipient,
)

public data class PickupFromStoreCheckoutParams(
    override val cartType: CartType,
    override val deliveryMethod: DeliveryMethod,
    override val recipient: Recipient,
    val city: City,
    val store: Store,
) : CheckoutParams(
    cartType = cartType,
    deliveryMethod = deliveryMethod,
    cityKladrId = city.id,
    recipient = recipient,
)
