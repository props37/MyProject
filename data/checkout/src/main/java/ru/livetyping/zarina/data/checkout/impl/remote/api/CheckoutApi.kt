package ru.livetyping.zarina.data.checkout.impl.remote.api

import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.DeliveryMethodDto

internal interface CheckoutApi {
    suspend fun getDeliveryMethods(
        cartType: CartType,
        cityKladrId: KladrId,
    ): List<DeliveryMethodDto>
}
