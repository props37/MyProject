package ru.livetyping.zarina.data.cart.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import ru.livetyping.zarina.data.cart.remote.api.dto.AddProductToCartRequestBody
import ru.livetyping.zarina.data.cart.remote.api.dto.ApplyMyCardToCartRequestBody
import ru.livetyping.zarina.data.cart.remote.api.dto.ApplyPromoCodeRequestBody
import ru.livetyping.zarina.data.cart.remote.api.dto.BonusWriteOffRequestBody
import ru.livetyping.zarina.data.cart.remote.api.dto.CartDto
import ru.livetyping.zarina.data.cart.remote.api.dto.CartProductCountDto
import ru.livetyping.zarina.data.cart.remote.api.dto.CartProductIdsDto
import ru.livetyping.zarina.data.cart.remote.api.dto.CartTypeDto
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject

class CartApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getCartProductIds(): CartProductIdsDto {
        return httpClient.get("/api/v1/cart-list").body()
    }

    suspend fun getCart(cartType: CartType, cityKladrId: KladrId?): CartDto {
        return httpClient.get("/api/cart") {
            parameter("cart_type", CartTypeDto.from(cartType).value)
            parameter("city_kladr_id", cityKladrId?.value)
        }.body()
    }

    suspend fun applyMyCardToCart(cartType: CartType, productsFirstPriceSum: Int) {
        val body = ApplyMyCardToCartRequestBody(
            cartType = CartTypeDto.from(cartType),
            productsFirstPriceSum = productsFirstPriceSum,
        )
        httpClient.post("/api/cart/my-card") {
            setJsonBody(body)
        }
    }

    suspend fun removeMyCardFromCart(cartType: CartType) {
        httpClient.delete("/api/cart/my-card") {
            parameter("cart_type", CartTypeDto.from(cartType).value)
        }
    }

    suspend fun applyPromoCode(promoCode: String) {
        val body = ApplyPromoCodeRequestBody(promoCode)
        httpClient.post("/api/cart/promocode") {
            setJsonBody(body)
        }
    }

    suspend fun removePromoCode() {
        httpClient.delete("/api/cart/promocode")
    }

    suspend fun applyBonusWriteOff(cartType: CartType, bonusCount: Int) {
        val body = BonusWriteOffRequestBody(
            cartType = CartTypeDto.from(cartType),
            bonusCountToWriteOff = bonusCount,
            isWriteOffApplied = true,
        )
        httpClient.post("/api/cart/bonuses") {
            setJsonBody(body)
        }
    }

    suspend fun removeBonusWriteOff(cartType: CartType) {
        val body = BonusWriteOffRequestBody(
            cartType = CartTypeDto.from(cartType),
            isWriteOffApplied = false,
            bonusCountToWriteOff = 0,
        )
        httpClient.post("/api/cart/bonuses") {
            setJsonBody(body)
        }
    }

    suspend fun addProductToCard(barcode: Barcode, count: Int): CartProductCountDto {
        val body = AddProductToCartRequestBody(barcode.value, count)
        return httpClient.post("/api/cart/item") {
            setJsonBody(body)
        }.body()
    }

    suspend fun removeProductFromCart(barcode: Barcode): CartProductCountDto {
        return httpClient.delete("/api/cart/item/${barcode.value}").body()
    }

    suspend fun changeProductCountInCart(barcode: Barcode, count: Int, cartType: CartType) {
        httpClient.post("/api/cart/item/update") {
            parameter("barcode", barcode.value)
            parameter("quantity", count)
            parameter("cart_type", CartTypeDto.from(cartType).value)
        }
    }

    suspend fun clearCart() {
        httpClient.delete("/api/cart")
    }
}
