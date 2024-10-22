package ru.livetyping.zarina.data.wishlist.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.core.domain.model.common.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductShort
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
        val productIds = remoteDataSource.getWishlistProductIdsFlow().firstOrNull()
        checkNotNull(productIds) { "Failed to fetch wishlist product IDs" }
        localDataSource.setWishlistProductIds(productIds)
        localDataSource.setIsWishlistProductIdsFetched(true)
    }

    override fun isWishlistProductIdsFetched(): Boolean {
        return localDataSource.isWishlistProductIdsFetched()
    }

    override fun getWishlistProductPageFlow(page: Int): Flow<Page<List<ProductShort>>> {
        return remoteDataSource.getFavoriteProductPageFlow(page)
    }

    override suspend fun addProductToWishlist(productId: Product.Id) {
        remoteDataSource.addProductToWishlist(productId)
        localDataSource.addProductToWishlist(productId)
    }

    override suspend fun removeProductFromWishlist(productId: Product.Id) {
        remoteDataSource.removeProductFromWishlist(productId)
        localDataSource.removeProductFromWishlist(productId)
    }
}
