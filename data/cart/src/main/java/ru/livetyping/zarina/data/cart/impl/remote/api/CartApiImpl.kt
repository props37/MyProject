package ru.livetyping.zarina.data.cart.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.core.network.util.setJsonBody
import ru.livetyping.zarina.data.cart.impl.remote.api.dto.AddProductToCartRequestBody
import ru.livetyping.zarina.data.cart.impl.remote.api.dto.ApplyMyCardRequestBody
import ru.livetyping.zarina.data.cart.impl.remote.api.dto.ApplyPromoCodeRequestBody
import ru.livetyping.zarina.data.cart.impl.remote.api.dto.CartDto
import ru.livetyping.zarina.data.cart.impl.remote.api.dto.CartProductCountDto
import ru.livetyping.zarina.data.cart.impl.remote.api.dto.CartProductIdsDto
import ru.livetyping.zarina.data.cart.impl.remote.api.dto.CartTypeDto
import javax.inject.Inject

internal class CartApiImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) : CartApi {
    override suspend fun getCart(cartType: CartType, cityKladrId: KladrId?): CartDto {
        return httpClient.get("/api/cart") {
            parameter("cart_type", CartTypeDto.from(cartType).value)
            parameter("city_kladr_id", cityKladrId?.value)
        }.body()
    }

    override suspend fun getCartProductIds(): CartProductIdsDto {
        return httpClient.get("/api/v1/cart-list").body()
    }

    override suspend fun addProductToCart(barcode: Barcode, count: Int): CartProductCountDto {
        val body = AddProductToCartRequestBody(barcode.value, count)
        return httpClient.post("/api/cart/item") {
            setJsonBody(body)
        }.body()
    }

    override suspend fun remoteProductFromCart(barcode: Barcode): CartProductCountDto {
        return httpClient.delete("/api/cart/item/${barcode.value}").body()
    }

    override suspend fun applyMyCard(cartType: CartType, productsFirstPriceSum: Int) {
        val body = ApplyMyCardRequestBody(
            cartType = CartTypeDto.from(cartType),
            productsFirstPriceSum = productsFirstPriceSum,
        )
        httpClient.post("/api/cart/my-card") {
            setJsonBody(body)
        }
    }

    override suspend fun withdrawMyCard(cartType: CartType) {
        httpClient.delete("/api/cart/my-card") {
            parameter("cart_type", CartTypeDto.from(cartType).value)
        }
    }

    override suspend fun applyPromoCode(promoCode: String) {
        val body = ApplyPromoCodeRequestBody(promoCode)
        httpClient.post("/api/cart/promocode") {
            setJsonBody(body)
        }
    }

    override suspend fun withdrawPromoCode() {
        httpClient.delete("/api/cart/promocode")
    }

    override suspend fun clearCart() {
        httpClient.delete("/api/cart")
    }
}
