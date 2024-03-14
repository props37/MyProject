package ru.zarina.zarina.data.cart.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zarina.zarina.data.cart.remote.api.CartApi
import ru.zarina.zarina.domain.cart.CartProductIds
import ru.zarina.zarina.domain.cart.ProductAdditionToCartResult
import ru.zarina.zarina.domain.common.Barcode
import javax.inject.Inject

class CartRemoteDataSource @Inject constructor(
    private val api: CartApi,
) {
    fun getCartProductIds(): Flow<CartProductIds> = flow {
        val cartProductIds = api.getCartProductIds().toCartProductIds()
        emit(cartProductIds)
    }

    suspend fun addProductToCart(barcode: Barcode, count: Int): ProductAdditionToCartResult {
        return api.addProductToCard(barcode, count).toProductAdditionToCartResult()
    }

    suspend fun clearCart() {
        api.clearCart()
    }
}
