package ru.zarina.zarina.data.rework.cart.remote

import ru.zarina.zarina.data.rework.cart.remote.api.CartApi
import ru.zarina.zarina.domain.rework.cart.ProductAdditionToCartResult
import ru.zarina.zarina.domain.rework.common.Barcode
import javax.inject.Inject

class CartRemoteDataSource @Inject constructor(
    private val api: CartApi,
) {
    suspend fun addProductToCart(barcode: Barcode, count: Int): ProductAdditionToCartResult {
        return api.addProductToCard(barcode, count).toProductAdditionToCartResult()
    }
}
