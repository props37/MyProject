package ru.livetyping.zarina.data.wishlist.impl.remote.api

import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.data.wishlist.impl.remote.api.dto.WishlistProductIdsDto

internal interface WishlistApi {
    suspend fun getWishlistProductIds(): WishlistProductIdsDto

    suspend fun addProductToWishlist(productId: Product.Id)

    suspend fun removeProductFromWishlist(productId: Product.Id)
}
