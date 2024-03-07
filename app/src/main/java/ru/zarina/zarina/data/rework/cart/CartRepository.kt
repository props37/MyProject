package ru.zarina.zarina.data.rework.cart

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import ru.zarina.zarina.data.rework.cart.local.CartLocalDataSource
import ru.zarina.zarina.data.rework.cart.remote.CartRemoteDataSource
import ru.zarina.zarina.domain.rework.cart.CartProductIds
import ru.zarina.zarina.domain.rework.cart.ProductAdditionToCartResult
import ru.zarina.zarina.domain.rework.common.Barcode
import ru.zarina.zarina.domain.rework.product.Product
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val localDataSource: CartLocalDataSource,
    private val remoteDataSource: CartRemoteDataSource,
) {
    val cartProductIds: StateFlow<Set<Product.Id>> = localDataSource.cartProductIds
    val areCartProductIdsFetched: StateFlow<Boolean> = localDataSource.areCartProductIdsFetched
    val cartProductCount: StateFlow<Int> = localDataSource.cartProductCount

    suspend fun fetchCartProductIds(): CartProductIds {
        val cartProductIds = remoteDataSource.getCartProductIds().first()
        localDataSource.setCartProductIds(cartProductIds.cartProductIds)
        localDataSource.setCartProductCount(cartProductIds.cartProductCount)
        localDataSource.setAreCartProductIdsFetched(true)
        return cartProductIds
    }

    suspend fun addProductToCart(
        productId: Product.Id,
        barcode: Barcode,
        count: Int,
    ): ProductAdditionToCartResult {
        val result = remoteDataSource.addProductToCart(barcode, count)
        localDataSource.addProductToCart(productId)
        return result
    }

    fun setCartProductCount(count: Int) {
        localDataSource.setCartProductCount(count)
    }

    suspend fun clearCart() {
        remoteDataSource.clearCart()
        localDataSource.setCartProductIds(emptySet())
        localDataSource.setCartProductCount(0)
    }

    fun clear() {
        localDataSource.clear()
    }
}
