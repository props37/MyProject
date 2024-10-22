package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.Product

public interface WishlistRepository {
    public fun getWishlistProductIdsFlow(): Flow<Set<Product.Id>>

    public suspend fun fetchWishlistProductIds()

    public fun isWishlistProductIdsFetched(): Boolean

    public suspend fun addProductToWishlist(productId: Product.Id)

    public suspend fun removeProductFromWishlist(productId: Product.Id)
}
