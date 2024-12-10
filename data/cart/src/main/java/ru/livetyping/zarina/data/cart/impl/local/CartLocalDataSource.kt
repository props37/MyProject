package ru.livetyping.zarina.data.cart.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.Product

internal interface CartLocalDataSource {
    fun getCartProductIdsFlow(): Flow<Set<Product.Id>>

    fun areCartProductIdsFetched(): Boolean
}
