package ru.zarina.zarina.data.cart.local

import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.domain.product.Product
import javax.inject.Inject

class CartLocalDataSource @Inject constructor(
    private val dataHolder: CartDataHolder,
) {
    val cartProductIds: StateFlow<Set<Product.Id>> = dataHolder.cartProductIds
    val areCartProductIdsFetched: StateFlow<Boolean> = dataHolder.areCartProductIdsFetched
    val cartProductCount: StateFlow<Int> = dataHolder.cartProductCount

    fun setCartProductIds(ids: Set<Product.Id>) {
        dataHolder.setCartProductIds(ids)
    }

    fun setAreCartProductIdsFetched(fetched: Boolean) {
        dataHolder.setAreCartProductIdsFetched(fetched)
    }

    fun addProductToCart(productId: Product.Id) {
        dataHolder.addProductToCart(productId)
    }

    fun setCartProductCount(count: Int) {
        dataHolder.setCartProductCount(count)
    }

    fun clear() {
        dataHolder.clear()
    }
}
