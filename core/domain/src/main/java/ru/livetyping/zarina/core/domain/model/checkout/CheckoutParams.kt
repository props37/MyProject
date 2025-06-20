package ru.livetyping.zarina.core.domain.model.checkout

import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.geo.Address
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.geo.FiasId
import ru.livetyping.zarina.core.domain.model.store.Store

public sealed class CheckoutParams {
    public abstract val cartType: CartType
    public abstract val deliveryMethod: DeliveryMethod
    public abstract val cityFiasId: FiasId
    public abstract val recipient: Recipient
}

public data class CourierDeliveryCheckoutParams(
    override val cartType: CartType,
    override val deliveryMethod: DeliveryMethod,
    override val recipient: Recipient,
    val address: Address,
    val deliveryOption: DeliveryOption,
    val dateTimePeriod: DeliveryOption.DateTimePeriod,
) : CheckoutParams() {
    override val cityFiasId: FiasId get() = address.city.id
}

public data class PostDeliveryCheckoutParams(
    override val cartType: CartType,
    override val deliveryMethod: DeliveryMethod,
    override val recipient: Recipient,
    val address: Address,
    val deliveryOption: DeliveryOption,
    val dateTimePeriod: DeliveryOption.DateTimePeriod,
) : CheckoutParams() {
    override val cityFiasId: FiasId get() = address.city.id
}

public data class PickupFromPickupPointCheckoutParams(
    override val cartType: CartType,
    override val deliveryMethod: DeliveryMethod,
    override val recipient: Recipient,
    val city: City,
    val pickupPoint: PickupPointDetailed,
    val deliveryType: PickupPointDetailed.DeliveryType?,
    val dateTimePeriod: PickupPointDetailed.DeliveryType.DateTimePeriod?,
) : CheckoutParams() {
    override val cityFiasId: FiasId get() = city.id
}

public data class PickupFromStoreCheckoutParams(
    override val cartType: CartType,
    override val deliveryMethod: DeliveryMethod,
    override val recipient: Recipient,
    val city: City,
    val store: Store,
) : CheckoutParams() {
    override val cityFiasId: FiasId get() = city.id
}
