package ru.zarina.zarina.data.cart.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import ru.zarina.zarina.data.cart.remote.api.dto.AddProductToCartRequestBody
import ru.zarina.zarina.data.cart.remote.api.dto.CartDto
import ru.zarina.zarina.data.cart.remote.api.dto.CartProductCountDto
import ru.zarina.zarina.data.cart.remote.api.dto.CartProductIdsDto
import ru.zarina.zarina.data.cart.remote.api.dto.DeliveryTypeDto
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.cart.DeliveryType
import ru.zarina.zarina.domain.common.Barcode
import ru.zarina.zarina.domain.geography.KladrId
import ru.zarina.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject

class CartApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getCartProductIds(): CartProductIdsDto {
        return httpClient.get("/api/v1/cart-list").body()
    }

    suspend fun getCart(deliveryType: DeliveryType, cityKladrId: KladrId?): CartDto {
        return httpClient.get("/api/cart") {
            parameter("cart_type", DeliveryTypeDto.fromDeliveryType(deliveryType))
            parameter("city_kladr_id", cityKladrId?.value)
        }.body()
    }

    suspend fun addProductToCard(barcode: Barcode, count: Int): CartProductCountDto {
        val body = AddProductToCartRequestBody(barcode.value, count)
        return httpClient.post("/api/cart/item/") {
            setJsonBody(body)
        }.body()
    }

    suspend fun clearCart() {
        httpClient.delete("/api/cart")
    }
}
