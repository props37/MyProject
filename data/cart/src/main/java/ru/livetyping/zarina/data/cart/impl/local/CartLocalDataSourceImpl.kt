package ru.livetyping.zarina.data.cart.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.Product
import javax.inject.Inject

internal class CartLocalDataSourceImpl @Inject constructor(
    private val dataHolder: CartDataHolder,
) : CartLocalDataSource {
    override fun getCartProductIdsFlow(): Flow<Set<Product.Id>> {
        return dataHolder.getCartProductIdsFlow()
    }

    override fun setCartProductIds(ids: Set<Product.Id>) {
        dataHolder.setCartProductIds(ids)
    }

    override fun getCartProductCountFlow(): Flow<Int> {
        return dataHolder.getCartProductCountFlow()
    }

    override fun setCartProductCount(count: Int) {
        dataHolder.setCartProductCount(count)
    }

    override fun areCartProductIdsFetched(): Boolean {
        return dataHolder.areCartProductIdsFetched()
    }

    override fun setAreCartProductIdsFetched(fetched: Boolean) {
        dataHolder.setAreCartProductIdsFetched(fetched)
    }

    override fun addProductToCart(productId: Product.Id) {
        dataHolder.addProductToCart(productId)
    }

    override fun removeProductFromCart(productId: Product.Id) {
        dataHolder.removeProductFromCart(productId)
    }

    override fun clear() {
        dataHolder.clear()
    }
}
