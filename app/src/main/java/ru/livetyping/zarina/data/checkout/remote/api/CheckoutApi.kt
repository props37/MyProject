package ru.livetyping.zarina.data.checkout.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.livetyping.zarina.data.cart.remote.api.dto.CartTypeDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.DeliveryOptionsDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.DeliveryMethodDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.StoreDto
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.cart.CartType
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

    /*
    https://zarina.ru/api/cart
    ?cart_type=delivery
    &city_kladr_id=7800000000000
    &shipping={
        "shipping_method_type":"express",
        "address":{
            "city_name":"Санкт-Петербург",
            "city_kladr_id":"7800000000000",
            "street_name":"ул.+Ленина",
            "street_kladr_id":"78000000000069000",
            "building_number":"10+А",
            "building_kladr_id":"780000000000690000013",
            "flat":"123фывыфв"
        },
        "payload":{
            "trying_type_level_name":"economy",
            "period_id":2992371039
        }
    }
     */
}
