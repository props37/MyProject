package ru.livetyping.zarina.data.cart

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import ru.livetyping.zarina.data.cart.local.CartLocalDataSource
import ru.livetyping.zarina.data.cart.remote.CartRemoteDataSource
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.cart.CartProductCount
import ru.livetyping.zarina.domain.cart.CartProductIds
import ru.livetyping.zarina.domain.cart.CartSize
import ru.livetyping.zarina.domain.cart.DeliveryType
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.domain.product.Product
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

    suspend fun applyMyCardToCart(deliveryType: DeliveryType, productsFirstPriceSum: Int) {
        remoteDataSource.applyMyCardToCart(deliveryType, productsFirstPriceSum)
    }

    suspend fun removeMyCardFromCart(deliveryType: DeliveryType) {
        remoteDataSource.removeMyCardFromCart(deliveryType)
    }

    suspend fun applyPromoCode(promoCode: String) {
        remoteDataSource.applyPromoCode(promoCode)
    }

    suspend fun removePromoCode() {
        remoteDataSource.removePromoCode()
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

    suspend fun changeProductCountInCart(barcode: Barcode, count: Int, deliveryType: DeliveryType) {
        remoteDataSource.changeProductCountInCart(barcode, count, deliveryType)
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
