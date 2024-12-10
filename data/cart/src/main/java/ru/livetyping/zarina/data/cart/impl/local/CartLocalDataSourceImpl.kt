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

    override fun areCartProductIdsFetched(): Boolean {
        return dataHolder.areCartProductIdsFetched()
    }
}
