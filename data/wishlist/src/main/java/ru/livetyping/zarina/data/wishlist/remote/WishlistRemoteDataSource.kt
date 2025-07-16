package ru.livetyping.zarina.data.wishlist.remote

import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductShort

internal interface WishlistRemoteDataSource {
    suspend fun getWishlistProductIds(): Set<Product.Id>

    suspend fun getFavoriteProductPage(page: Int): Page<List<ProductShort>>

    suspend fun addProductToWishlist(productId: Product.Id)

    suspend fun removeProductFromWishlist(productId: Product.Id)

    suspend fun clearWishlist()
}
