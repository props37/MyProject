package ru.livetyping.zarina.data.wishlist.impl

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.data.wishlist.impl.local.WishlistLocalDataSource
import ru.livetyping.zarina.data.wishlist.impl.remote.WishlistRemoteDataSource
import javax.inject.Inject

internal class WishlistRepositoryImpl @Inject constructor(
    private val remoteDataSource: WishlistRemoteDataSource,
    private val localDataSource: WishlistLocalDataSource,
) : WishlistRepository {
    override fun getWishlistProductIdsFlow(): Flow<Set<Product.Id>> {
        return localDataSource.getWishlistProductIdsFlow()
    }

    override suspend fun fetchWishlistProductIds() {
        val productIds = remoteDataSource.getWishlistProductIds()
        localDataSource.setWishlistProductIds(productIds)
        localDataSource.setIsWishlistProductIdsFetched(true)
    }
}
