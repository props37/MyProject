package ru.livetyping.zarina.data.wishlist.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.Product

internal interface WishlistDataHolder {
    fun getWishlistProductIdsFlow(): Flow<Set<Product.Id>>

    fun setWishlistProductIds(ids: Set<Product.Id>)

    fun isWishlistProductIdsFetched(): Boolean

    fun setIsWishlistProductIdsFetched(isFetched: Boolean)
}
