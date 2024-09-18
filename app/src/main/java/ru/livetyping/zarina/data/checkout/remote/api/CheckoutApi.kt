package ru.livetyping.zarina.data.checkout.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.livetyping.zarina.data.cart.remote.api.dto.CartTypeDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.DeliveryMethodDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.DeliveryOptionsDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.PickupPointDetailsDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.PickupPointDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.StoreDto
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.PickupPoint
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

    suspend fun getDeliveryMethods(
        cartType: CartType,
        cityKladrId: KladrId,
    ): List<DeliveryMethodDto> {
        return httpClient.get("/api/shipping-methods") {
            parameter("cart_type", CartTypeDto.from(cartType).value)
            parameter("address_kladr", cityKladrId.value)
        }.body()
    }

    suspend fun getCourierDeliveryOptions(buildingKladrId: KladrId): DeliveryOptionsDto {
        return httpClient.get("/api/shipping-methods/express") {
            parameter("address_kladr", buildingKladrId.value)
        }.body()
    }

    suspend fun getPostDeliveryOptions(buildingKladrId: KladrId): DeliveryOptionsDto {
        return httpClient.get("/api/shipping-methods/post") {
            parameter("address_kladr", buildingKladrId.value)
        }.body()
    }

    suspend fun getPickupPoints(cityKladrId: KladrId): List<PickupPointDto> {
        return httpClient.get("/api/shipping-methods/pickup_points") {
            parameter("city_kladr_id", cityKladrId.value)
        }.body()
    }

    suspend fun getPickupPointDetails(
        cityKladrId: KladrId,
        pickupPointId: PickupPoint.Id,
    ): PickupPointDetailsDto {
        // TODO: [High] Always use payment_method=paytureinpay?
        return httpClient.get(
            "/api/shipping-methods/cities/${cityKladrId.value}/pickup_points/" +
                    "${pickupPointId.value}/?payment_method=paytureinpay"
        ).body()
    }
}
