package ru.livetyping.zarina.data.checkout.impl.remote.api

import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.DeliveryMethodDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.PickupPointDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.PickupStoreDto

internal interface CheckoutApi {
    suspend fun getDeliveryMethods(
        cartType: CartType,
        cityKladrId: KladrId,
    ): List<DeliveryMethodDto>

    suspend fun getPickupPoints(cityKladrId: KladrId): List<PickupPointDto>

    suspend fun getPickupStores(
        cityKladrId: KladrId,
        deliveryMethodType: DeliveryMethodType,
    ): List<PickupStoreDto>
}
