package ru.livetyping.zarina.data.cart.impl

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.repository.CartRepository
import javax.inject.Inject

internal class CartRepositoryImpl @Inject constructor(
) : CartRepository {
    override fun getCartProductIdsFlow(cachePolicy: CachePolicy): Flow<Set<Product.Id>> {
        TODO("Not yet implemented")
    }

    override fun areCartProductIdsFetched(): Boolean {
        TODO("Not yet implemented")
    }
}
