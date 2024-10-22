package ru.livetyping.zarina.data.wishlist.impl.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.livetyping.zarina.core.domain.model.product.Product
import timber.log.Timber
import javax.inject.Inject

internal class WishlistDataHolderImpl @Inject constructor() : WishlistDataHolder {
    private val wishlistProductIds = MutableStateFlow<Set<Product.Id>>(emptySet())

    private var isWishlistProductIdsFetched = false

    override fun getWishlistProductIdsFlow(): Flow<Set<Product.Id>> {
        return wishlistProductIds
    }

    override fun setWishlistProductIds(ids: Set<Product.Id>) {
        wishlistProductIds.value = ids
        Timber.tag(TAG).v("Wishlist product IDs set: $ids")
    }

    override fun isWishlistProductIdsFetched(): Boolean {
        return isWishlistProductIdsFetched
    }

    override fun setIsWishlistProductIdsFetched(isFetched: Boolean) {
        isWishlistProductIdsFetched = isFetched
        Timber.tag(TAG).v("Wishlist product IDs fetched set to $isFetched")
    }

    private companion object {
        private const val TAG = "WishlistDataHolderImpl"
    }
}
