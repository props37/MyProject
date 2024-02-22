package ru.zarina.zarina.data.rework.cart.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import ru.zarina.zarina.data.rework.cart.remote.api.dto.AddProductToCartRequestBody
import ru.zarina.zarina.data.rework.cart.remote.api.dto.CartProductCountDto
import ru.zarina.zarina.data.rework.cart.remote.api.dto.CartProductIdsDto
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.common.Barcode
import ru.zarina.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject

class CartApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getCartProductIds(): CartProductIdsDto {
        return httpClient.get("/api/v1/cart/id").body()
    }

    suspend fun addProductToCard(barcode: Barcode, count: Int): CartProductCountDto {
        val body = AddProductToCartRequestBody(barcode.value, count)
        return httpClient.post("/api/cart/item/") {
            setJsonBody(body)
        }.body()
    }
}
