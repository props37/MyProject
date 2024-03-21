package ru.zarina.zarina.data.cart.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zarina.zarina.data.cart.remote.api.CartApi
import ru.zarina.zarina.domain.cart.Cart
import ru.zarina.zarina.domain.cart.CartProductCount
import ru.zarina.zarina.domain.cart.CartProductIds
import ru.zarina.zarina.domain.cart.DeliveryType
import ru.zarina.zarina.domain.common.Barcode
import ru.zarina.zarina.domain.geography.KladrId
import javax.inject.Inject

class CartRemoteDataSource @Inject constructor(
    private val api: CartApi,
) {
    fun getCartProductIdsFlow(): Flow<CartProductIds> = flow {
        val cartProductIds = api.getCartProductIds().toCartProductIds()
        emit(cartProductIds)
    }

    fun getCartFlow(deliveryType: DeliveryType, cityKladrId: KladrId?): Flow<Cart> = flow {
        val cart = api.getCart(deliveryType, cityKladrId).toCart()
        emit(cart)
    }

    suspend fun addProductToCart(barcode: Barcode, count: Int): CartProductCount {
        return api.addProductToCard(barcode, count).toCartProductCount()
    }

    suspend fun removeProductFromCart(barcode: Barcode): CartProductCount {
        return api.removeProductFromCart(barcode).toCartProductCount()
    }

    suspend fun changeProductCountInCart(barcode: Barcode, count: Int) {
        api.changeProductCountInCart(barcode, count)
    }

    suspend fun clearCart() {
        api.clearCart()
    }
}
