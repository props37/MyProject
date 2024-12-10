package ru.livetyping.zarina.data.cart.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.Product

internal interface CartDataHolder {
    fun getCartProductIdsFlow(): Flow<Set<Product.Id>>

    fun setCartProductIds(ids: Set<Product.Id>)

    fun setCartProductCount(count: Int)

    fun getCartProductCountFlow(): Flow<Int>

    fun areCartProductIdsFetched(): Boolean

    fun setAreCartProductIdsFetched(fetched: Boolean)

    fun addProductToCart(productId: Product.Id)

    fun removeProductFromCart(productId: Product.Id)

    fun clear()
}
