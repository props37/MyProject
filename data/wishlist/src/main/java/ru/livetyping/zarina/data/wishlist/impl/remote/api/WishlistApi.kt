package ru.livetyping.zarina.data.wishlist.impl.remote.api

import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.data.wishlist.impl.remote.api.dto.WishlistProductIdsDto
import ru.livetyping.zarina.data.wishlist.impl.remote.api.dto.WishlistProductsDto

internal interface WishlistApi {
    suspend fun getWishlistProductIds(): WishlistProductIdsDto

    suspend fun getWishlistProducts(page: Int): WishlistProductsDto

    suspend fun addProductToWishlist(productId: Product.Id)

    suspend fun removeProductFromWishlist(productId: Product.Id)
}
