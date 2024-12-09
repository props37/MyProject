package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.product.Product

public interface CartRepository {
    public fun getCartProductIdsFlow(cachePolicy: CachePolicy): Flow<Set<Product.Id>>

    public fun areCartProductIdsFetched(): Boolean
}
