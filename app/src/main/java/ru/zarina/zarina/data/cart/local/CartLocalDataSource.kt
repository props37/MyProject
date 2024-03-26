package ru.zarina.zarina.data.cart.local

import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.domain.cart.CartSize
import ru.zarina.zarina.domain.product.Product
import javax.inject.Inject

class CartLocalDataSource @Inject constructor(
    private val dataHolder: CartDataHolder,
) {
    val cartProductIds: StateFlow<Set<Product.Id>> = dataHolder.cartProductIds
    val areCartProductIdsFetched: StateFlow<Boolean> = dataHolder.areCartProductIdsFetched
    val cartSize: StateFlow<CartSize> = dataHolder.cartSize

    fun setCartProductIds(ids: Set<Product.Id>) {
        dataHolder.setCartProductIds(ids)
    }

    fun setAreCartProductIdsFetched(fetched: Boolean) {
        dataHolder.setAreCartProductIdsFetched(fetched)
    }

    fun addProductToCart(productId: Product.Id) {
        dataHolder.addProductToCart(productId)
    }

    fun removeProductFromCart(productId: Product.Id) {
        dataHolder.removeProductFromCart(productId)
    }

    fun setCartSize(size: CartSize) {
        dataHolder.setCartSize(size)
    }

    fun setCartTotalProductCount(count: Int) {
        dataHolder.setCartTotalProductCount(count)
    }

    fun clear() {
        dataHolder.clear()
    }
}
