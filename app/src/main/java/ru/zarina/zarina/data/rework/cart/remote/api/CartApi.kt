package ru.zarina.zarina.data.rework.cart.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import ru.zarina.zarina.data.rework.cart.remote.api.dto.AddProductToCartRequestBody
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.common.Barcode
import ru.zarina.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject

class CartApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun addProductToCard(barcode: Barcode, count: Int) {
        val body = AddProductToCartRequestBody(barcode.value, count)
        httpClient.post("/api/cart/item/") {
            setJsonBody(body)
        }
    }
}
