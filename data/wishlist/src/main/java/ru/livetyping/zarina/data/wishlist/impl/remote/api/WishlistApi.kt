package ru.livetyping.zarina.data.wishlist.impl.remote.api

import ru.livetyping.zarina.data.wishlist.impl.remote.api.dto.WishlistProductIdsDto

internal interface WishlistApi {
    suspend fun getWishlistProductIds(): WishlistProductIdsDto
}
