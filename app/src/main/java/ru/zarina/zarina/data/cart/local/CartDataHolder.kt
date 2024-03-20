package ru.zarina.zarina.data.cart.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zarina.zarina.domain.cart.CartSize
import ru.zarina.zarina.domain.product.Product
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartDataHolder @Inject constructor() {
    private val _cartProductIds = MutableStateFlow<Set<Product.Id>>(emptySet())
    val cartProductIds: StateFlow<Set<Product.Id>> = _cartProductIds.asStateFlow()

    private val _areCartProductIdsFetched = MutableStateFlow(false)
    val areCartProductIdsFetched: StateFlow<Boolean> = _areCartProductIdsFetched.asStateFlow()

    private val _cartSize = MutableStateFlow(CartSize.EMPTY)
    val cartSize: StateFlow<CartSize> = _cartSize.asStateFlow()

    fun setCartProductIds(ids: Set<Product.Id>) {
        Timber.v("Set cart product IDs: $ids")
        _cartProductIds.value = ids
    }

    fun setAreCartProductIdsFetched(fetched: Boolean) {
        Timber.v("Set cart product IDs fetched: $fetched")
        _areCartProductIdsFetched.value = fetched
    }

    fun addProductToCart(productId: Product.Id) {
        Timber.v("Add product $productId to cart")
        _cartProductIds.update { it + productId }
    }

    fun setCartSize(size: CartSize) {
        Timber.v("Set cart size: $size")
        _cartSize.value = size
    }

    fun setCartTotalProductCount(count: Int) {
        Timber.v("Set cart total product count: $count")
        _cartSize.update { it.copy(totalProductCount = count) }
    }

    fun clear() {
        Timber.v("Clear cart data")
        _cartProductIds.value = emptySet()
        _areCartProductIdsFetched.value = false
        _cartSize.value = CartSize.EMPTY
    }
}
