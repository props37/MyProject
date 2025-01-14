package ru.livetyping.zarina.data.cart.impl.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.data.cart.impl.model.CartProductCount
import ru.livetyping.zarina.data.cart.impl.model.CartProductIds
import ru.livetyping.zarina.data.cart.impl.remote.api.CartApi
import javax.inject.Inject

internal class CartRemoteDataSourceImpl @Inject constructor(
    private val api: CartApi,
) : CartRemoteDataSource {
    override fun getCartProductIdsFlow(): Flow<CartProductIds> = flow {
        val cartProductIds = api.getCartProductIds().toCartProductIds()
        emit(cartProductIds)
    }

    override suspend fun addProductToCart(barcode: Barcode, count: Int): CartProductCount {
        return api.addProductToCart(barcode, count).toCartProductCount()
    }

    override suspend fun remoteProductFromCart(barcode: Barcode): CartProductCount {
        return api.remoteProductFromCart(barcode).toCartProductCount()
    }

    override suspend fun clearCart() {
        api.clearCart()
    }
}
