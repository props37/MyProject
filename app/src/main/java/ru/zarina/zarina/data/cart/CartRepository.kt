package ru.zarina.zarina.data.cart

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import ru.zarina.zarina.data.cart.local.CartLocalDataSource
import ru.zarina.zarina.data.cart.remote.CartRemoteDataSource
import ru.zarina.zarina.domain.cart.Cart
import ru.zarina.zarina.domain.cart.CartProductCount
import ru.zarina.zarina.domain.cart.CartProductIds
import ru.zarina.zarina.domain.cart.CartSize
import ru.zarina.zarina.domain.cart.DeliveryType
import ru.zarina.zarina.domain.common.Barcode
import ru.zarina.zarina.domain.geography.KladrId
import ru.zarina.zarina.domain.product.Product
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val localDataSource: CartLocalDataSource,
    private val remoteDataSource: CartRemoteDataSource,
) {
    val cartProductIds: StateFlow<Set<Product.Id>> = localDataSource.cartProductIds
    val areCartProductIdsFetched: StateFlow<Boolean> = localDataSource.areCartProductIdsFetched
    val cartSize: StateFlow<CartSize> = localDataSource.cartSize

    suspend fun fetchCartProductIds(): CartProductIds {
        val cartProductIds = remoteDataSource.getCartProductIdsFlow().first()
        localDataSource.setCartProductIds(cartProductIds.cartProductIds)
        localDataSource.setCartTotalProductCount(cartProductIds.cartProductCount)
        localDataSource.setAreCartProductIdsFetched(true)
        return cartProductIds
    }

    fun getCartFlow(deliveryType: DeliveryType, cityKladrId: KladrId?): Flow<Cart> {
        return remoteDataSource.getCartFlow(deliveryType, cityKladrId)
    }

    suspend fun addProductToCart(
        productId: Product.Id,
        barcode: Barcode,
        count: Int,
    ): CartProductCount {
        val cartProductCount = remoteDataSource.addProductToCart(barcode, count)
        localDataSource.addProductToCart(productId)
        return cartProductCount
    }

    suspend fun removeProductFromCart(
        productId: Product.Id,
        barcode: Barcode,
    ): CartProductCount {
        val cartProductCount = remoteDataSource.removeProductFromCart(barcode)
        localDataSource.removeProductFromCart(productId)
        return cartProductCount
    }

    suspend fun changeProductCountInCart(barcode: Barcode, count: Int) {
        remoteDataSource.changeProductCountInCart(barcode, count)
    }

    fun setCartSize(size: CartSize) {
        localDataSource.setCartSize(size)
    }

    fun setCartTotalProductCount(count: Int) {
        localDataSource.setCartTotalProductCount(count)
    }

    suspend fun clearCart() {
        remoteDataSource.clearCart()
        localDataSource.setCartProductIds(emptySet())
        localDataSource.setCartSize(CartSize.EMPTY)
    }

    fun clear() {
        localDataSource.clear()
    }
}
