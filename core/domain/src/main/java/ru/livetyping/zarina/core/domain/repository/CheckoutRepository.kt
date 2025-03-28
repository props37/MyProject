package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointShort
import ru.livetyping.zarina.core.domain.model.checkout.PickupStore
import ru.livetyping.zarina.core.domain.model.geo.KladrId

public interface CheckoutRepository {
    public fun getDeliveryMethodsFlow(
        cartType: CartType,
        cityKladrId: KladrId,
    ): Flow<List<DeliveryMethod>>

    public fun getPickupPointsFlow(cityKladrId: KladrId): Flow<List<PickupPointShort>>

    public fun getPickupStoresFlow(
        cityKladrId: KladrId,
        deliveryMethodType: DeliveryMethodType,
    ): Flow<List<PickupStore>>
}
