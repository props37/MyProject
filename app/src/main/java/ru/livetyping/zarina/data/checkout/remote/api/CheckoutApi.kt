package ru.livetyping.zarina.data.checkout.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.livetyping.zarina.data.checkout.remote.api.dto.StoreDto
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.geography.KladrId
import javax.inject.Inject

class CheckoutApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getPickupStores(cityKladrId: KladrId): List<StoreDto> {
        return httpClient.get("/api/v1/shipping-methods/shops") {
            parameter("city_kladr_id", cityKladrId.value)
        }.body()
    }
}
