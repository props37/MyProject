package ru.zarina.zarina.data.rework.cart

import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.data.rework.cart.local.CartLocalDataSource
import ru.zarina.zarina.data.rework.cart.remote.CartRemoteDataSource
import ru.zarina.zarina.domain.rework.cart.ProductAdditionToCartResult
import ru.zarina.zarina.domain.rework.common.Barcode
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val localDataSource: CartLocalDataSource,
    private val remoteDataSource: CartRemoteDataSource,
) {
    val cartProductCount: StateFlow<Int> = localDataSource.cartProductCount

    suspend fun addProductToCart(barcode: Barcode, count: Int): ProductAdditionToCartResult {
        return remoteDataSource.addProductToCart(barcode, count)
    }

    fun setCartProductCount(count: Int) {
        localDataSource.setCartProductCount(count)
    }
}
