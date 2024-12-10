package ru.livetyping.zarina.data.cart.impl.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.livetyping.zarina.core.domain.model.product.Product
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

internal class CartDataHolderImpl @Inject constructor() : CartDataHolder {
    private val cartProductIds = MutableStateFlow<Set<Product.Id>>(emptySet())

    private val cartProductCount = MutableStateFlow(0)

    private var areCartProductIdsFetched = AtomicBoolean(false)

    override fun getCartProductIdsFlow(): Flow<Set<Product.Id>> {
        return cartProductIds
    }

    override fun setCartProductIds(ids: Set<Product.Id>) {
        cartProductIds.value = ids
        Timber.tag(TAG).v("Cart product IDs set: $ids")
    }

    override fun setCartProductCount(count: Int) {
        cartProductCount.value = count
        Timber.tag(TAG).v("Cart product count set to $count")
    }

    override fun getCartProductCountFlow(): Flow<Int> {
        return cartProductCount
    }

    override fun areCartProductIdsFetched(): Boolean {
        return areCartProductIdsFetched.get()
    }

    override fun setAreCartProductIdsFetched(fetched: Boolean) {
        areCartProductIdsFetched.compareAndSet(
            /* expectedValue = */ areCartProductIdsFetched.get(),
            /* newValue = */ fetched,
        )
        Timber.tag(TAG).v("Cart product IDs fetched set to $fetched")
    }

    override fun addProductToCart(productId: Product.Id) {
        cartProductIds.update { it + productId }
        Timber.tag(TAG).v("Product $productId added to cart")
    }

    override fun removeProductFromCart(productId: Product.Id) {
        cartProductIds.update { it - productId }
        Timber.tag(TAG).v("Product $productId removed from cart")
    }

    override fun clear() {
        cartProductIds.value = emptySet()
        cartProductCount.value = 0
        areCartProductIdsFetched.set(false)
        Timber.tag(TAG).v("Cart product IDs cleared")
    }

    private companion object {
        private const val TAG = "CartDataHolderImpl"
    }
}
