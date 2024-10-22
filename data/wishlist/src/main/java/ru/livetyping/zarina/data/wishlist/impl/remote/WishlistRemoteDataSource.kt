package ru.livetyping.zarina.data.wishlist.impl.remote

import ru.livetyping.zarina.core.domain.model.product.Product

internal interface WishlistRemoteDataSource {
    suspend fun getWishlistProductIds(): Set<Product.Id>

    suspend fun addProductToWishlist(productId: Product.Id)

    suspend fun removeProductFromWishlist(productId: Product.Id)
}
