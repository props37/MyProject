package ru.zarina.zarina.data.cart.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zarina.zarina.data.cart.remote.api.CartApi
import ru.zarina.zarina.domain.cart.CartProductIds
import ru.zarina.zarina.domain.cart.ProductAdditionToCartResult
import ru.zarina.zarina.domain.rework.common.Barcode
import javax.inject.Inject

class CartRemoteDataSource @Inject constructor(
    private val api: CartApi,
) {
    fun getCartProductIds(): Flow<CartProductIds> = flow {
        emit(api.getCartProductIds().toCartProductIds())
    }

    suspend fun addProductToCart(barcode: Barcode, count: Int): ProductAdditionToCartResult {
        return api.addProductToCard(barcode, count).toProductAdditionToCartResult()
    }

    suspend fun clearCart() {
        api.clearCart()
    }
}
