package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.common.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductShort

public interface WishlistRepository {
    public fun getWishlistProductIdsFlow(cachePolicy: CachePolicy): Flow<Set<Product.Id>>

    public suspend fun fetchWishlistProductIds()

    public fun isWishlistProductIdsFetched(): Boolean

    public fun getWishlistProductPageFlow(page: Int): Flow<Page<List<ProductShort>>>

    public suspend fun addProductToWishlist(productId: Product.Id)

    public suspend fun removeProductFromWishlist(productId: Product.Id)

    public suspend fun clearWishlist()
}
