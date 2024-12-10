package ru.livetyping.zarina.data.cart.impl

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.data.cart.impl.local.CartLocalDataSource
import ru.livetyping.zarina.data.cart.impl.remote.CartRemoteDataSource
import javax.inject.Inject

internal class CartRepositoryImpl @Inject constructor(
    private val remoteDataSource: CartRemoteDataSource,
    private val localDataSource: CartLocalDataSource,
) : CartRepository {
    override fun getCartProductIdsFlow(cachePolicy: CachePolicy): Flow<Set<Product.Id>> {
        TODO("Not yet implemented")
    }

    override fun areCartProductIdsFetched(): Boolean {
        return localDataSource.areCartProductIdsFetched()
    }
}
