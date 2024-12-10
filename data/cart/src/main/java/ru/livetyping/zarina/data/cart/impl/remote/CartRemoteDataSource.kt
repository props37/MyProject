package ru.livetyping.zarina.data.cart.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.data.cart.impl.model.CartProductCount
import ru.livetyping.zarina.data.cart.impl.model.CartProductIds

internal interface CartRemoteDataSource {
    fun getCartProductIdsFlow(): Flow<CartProductIds>

    suspend fun addProductToCart(barcode: Barcode, count: Int): CartProductCount

    suspend fun remoteProductFromCart(barcode: Barcode): CartProductCount
}
