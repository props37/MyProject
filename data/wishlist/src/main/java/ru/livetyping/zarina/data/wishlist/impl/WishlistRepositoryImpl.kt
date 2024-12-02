package ru.livetyping.zarina.data.wishlist.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.core.domain.cache.CacheExpirationPolicy
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.cache.CacheUpdatePolicy
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.data.wishlist.impl.local.WishlistLocalDataSource
import ru.livetyping.zarina.data.wishlist.impl.remote.WishlistRemoteDataSource
import timber.log.Timber
import javax.inject.Inject

internal class WishlistRepositoryImpl @Inject constructor(
    private val remoteDataSource: WishlistRemoteDataSource,
    private val localDataSource: WishlistLocalDataSource,
) : WishlistRepository {
    override fun getWishlistProductIdsFlow(cachePolicy: CachePolicy): Flow<Set<Product.Id>> {
        return when (cachePolicy) {
            CachePolicy.LocalOnly -> localDataSource.getWishlistProductIdsFlow()
            is CachePolicy.LocalFirstThenRemote -> {
                getWishlistProductIdsFlowLocalFirstThenRemote(cachePolicy)
            }

            is CachePolicy.Remote -> getWishlistProductIdsFlowRemote(cachePolicy)
        }
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

    override suspend fun clearWishlist() {
        remoteDataSource.clearWishlist()
        localDataSource.setWishlistProductIds(emptySet())
        localDataSource.setIsWishlistProductIdsFetched(false)
    }

    override fun clear() {
        localDataSource.clear()
    }

    private fun getWishlistProductIdsFlowLocalFirstThenRemote(
        cachePolicy: CachePolicy.LocalFirstThenRemote,
    ): Flow<Set<Product.Id>> {
        // TODO: [Low] Add support for CacheExpirationPolicy
        Timber.tag(TAG).w("Wishlist product IDs CacheExpirationPolicy is not supported, fallback to ${CacheExpirationPolicy.UNLIMITED}")
        return localDataSource.getWishlistProductIdsFlow()
            .map { cached ->
                if (!localDataSource.isWishlistProductIdsFetched()) {
                    val productIds = remoteDataSource.getWishlistProductIdsFlow().firstOrNull()
                    checkNotNull(productIds) { "Failed to fetch wishlist product IDs" }
                    wishlistProductIdsCacheUpdatePolicyImpl(productIds, cachePolicy.updatePolicy)
                    productIds
                } else {
                    cached
                }
            }
    }

    private fun getWishlistProductIdsFlowRemote(
        cachePolicy: CachePolicy.Remote,
    ): Flow<Set<Product.Id>> {
        return remoteDataSource.getWishlistProductIdsFlow()
            .onEach { productIds ->
                wishlistProductIdsCacheUpdatePolicyImpl(productIds, cachePolicy.updatePolicy)
            }
    }

    private fun wishlistProductIdsCacheUpdatePolicyImpl(
        productIds: Set<Product.Id>,
        policy: CacheUpdatePolicy,
    ) {
        when (policy) {
            CacheUpdatePolicy.NONE -> Unit
            CacheUpdatePolicy.CLEAR -> clearLocalWishlistProductIds()
            CacheUpdatePolicy.UPDATE -> setLocalFetchedWishlistProductIds(productIds)
        }
    }

    private fun setLocalFetchedWishlistProductIds(productIds: Set<Product.Id>) {
        localDataSource.setWishlistProductIds(productIds)
        localDataSource.setIsWishlistProductIdsFetched(true)
    }

    private fun clearLocalWishlistProductIds() {
        localDataSource.setWishlistProductIds(emptySet())
        localDataSource.setIsWishlistProductIdsFetched(false)
    }

    private companion object {
        private const val TAG = "WishlistRepositoryImpl"
    }
}
