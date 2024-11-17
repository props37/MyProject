package ru.livetyping.zarina.data.wishlist.impl.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.livetyping.zarina.core.domain.model.product.Product
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class WishlistDataHolderImpl @Inject constructor() : WishlistDataHolder {
    private val wishlistProductIds = MutableStateFlow<Set<Product.Id>>(emptySet())

    private var isWishlistProductIdsFetched = AtomicBoolean(false)

    override fun getWishlistProductIdsFlow(): Flow<Set<Product.Id>> {
        return wishlistProductIds
    }

    override fun setWishlistProductIds(ids: Set<Product.Id>) {
        wishlistProductIds.value = ids
        Timber.tag(TAG).v("Wishlist product IDs set: $ids")
    }

    override fun isWishlistProductIdsFetched(): Boolean {
        return isWishlistProductIdsFetched.get()
    }

    override fun setIsWishlistProductIdsFetched(isFetched: Boolean) {
        isWishlistProductIdsFetched.compareAndSet(
            /* expectedValue = */ isWishlistProductIdsFetched.get(),
            /* newValue = */ isFetched,
        )
        Timber.tag(TAG).v("Wishlist product IDs fetched set to $isFetched")
    }

    override fun addProductToWishlist(productId: Product.Id) {
        wishlistProductIds.update { it + productId }
        Timber.tag(TAG).v("Product $productId added to wishlist")
    }

    override fun removeProductFromWishlist(productId: Product.Id) {
        wishlistProductIds.update { it - productId }
        Timber.tag(TAG).v("Product $productId removed from wishlist")
    }

    private companion object {
        private const val TAG = "WishlistDataHolderImpl"
    }
}
