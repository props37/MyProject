package ru.livetyping.zarina.data.checkout.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.core.network.zarina.dto.CartTypeDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.DeliveryMethodDto
import javax.inject.Inject

internal class CheckoutApiImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) : CheckoutApi {
    override suspend fun getDeliveryMethods(
        cartType: CartType,
        cityKladrId: KladrId,
    ): List<DeliveryMethodDto> {
        return httpClient.get("/api/shipping-methods") {
            parameter("cart_type", CartTypeDto.from(cartType).value)
            parameter("address_kladr", cityKladrId.value)
        }.body()
    }
}
