package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.Product

public interface WishlistRepository {
    public fun getWishlistProductIdsFlow(): Flow<Set<Product.Id>>

    public suspend fun fetchWishlistProductIds()
}
