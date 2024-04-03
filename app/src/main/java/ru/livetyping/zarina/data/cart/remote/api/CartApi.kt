package ru.livetyping.zarina.data.cart.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import ru.livetyping.zarina.data.cart.remote.api.dto.AddProductToCartRequestBody
import ru.livetyping.zarina.data.cart.remote.api.dto.CartDto
import ru.livetyping.zarina.data.cart.remote.api.dto.CartProductCountDto
import ru.livetyping.zarina.data.cart.remote.api.dto.CartProductIdsDto
import ru.livetyping.zarina.data.cart.remote.api.dto.DeliveryTypeDto
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.cart.DeliveryType
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.util.library.ktor.setJsonBody
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
            parameter("cart_type", DeliveryTypeDto.fromDeliveryType(deliveryType).value)
            parameter("city_kladr_id", cityKladrId?.value)
        }.body()
    }

    suspend fun addProductToCard(barcode: Barcode, count: Int): CartProductCountDto {
        val body = AddProductToCartRequestBody(barcode.value, count)
        return httpClient.post("/api/cart/item/") {
            setJsonBody(body)
        }.body()
    }

    suspend fun removeProductFromCart(barcode: Barcode): CartProductCountDto {
        return httpClient.delete("/api/cart/item/${barcode.value}").body()
    }

    suspend fun changeProductCountInCart(barcode: Barcode, count: Int, deliveryType: DeliveryType) {
        httpClient.post("/api/cart/item/update") {
            parameter("barcode", barcode.value)
            parameter("quantity", count)
            parameter("cart_type", DeliveryTypeDto.fromDeliveryType(deliveryType).value)
        }
    }

    suspend fun clearCart() {
        httpClient.delete("/api/cart")
    }
}
