package ru.livetyping.zarina.data.wishlist.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductShort

internal interface WishlistRemoteDataSource {
    // TODO: [Medium] Rework to return Flow
    suspend fun getWishlistProductIds(): Set<Product.Id>

    fun getFavoriteProductPageFlow(page: Int): Flow<Page<List<ProductShort>>>

    suspend fun addProductToWishlist(productId: Product.Id)

    suspend fun removeProductFromWishlist(productId: Product.Id)
}
