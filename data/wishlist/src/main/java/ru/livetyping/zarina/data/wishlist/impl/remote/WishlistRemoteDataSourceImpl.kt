package ru.livetyping.zarina.data.wishlist.impl.remote

import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.data.wishlist.impl.remote.api.WishlistApi
import javax.inject.Inject

internal class WishlistRemoteDataSourceImpl @Inject constructor(
    private val api: WishlistApi,
) : WishlistRemoteDataSource {
    override suspend fun getWishlistProductIds(): Set<Product.Id> {
        return api.getWishlistProductIds().toProductIds()
    }
}
