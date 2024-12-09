package ru.livetyping.zarina.data.wishlist.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.Product
import javax.inject.Inject

internal class WishlistLocalDataSourceImpl @Inject constructor(
    private val dataHolder: WishlistDataHolder,
) : WishlistLocalDataSource {
    override fun getWishlistProductIdsFlow(): Flow<Set<Product.Id>> {
        return dataHolder.getWishlistProductIdsFlow()
    }

    override fun setWishlistProductIds(ids: Set<Product.Id>) {
        dataHolder.setWishlistProductIds(ids)
    }

    override fun areWishlistProductIdsFetched(): Boolean {
        return dataHolder.areWishlistProductIdsFetched()
    }

    override fun setAreWishlistProductIdsFetched(fetched: Boolean) {
        dataHolder.setAreWishlistProductIdsFetched(fetched)
    }

    override fun addProductToWishlist(productId: Product.Id) {
        dataHolder.addProductToWishlist(productId)
    }

    override fun removeProductFromWishlist(productId: Product.Id) {
        dataHolder.removeProductFromWishlist(productId)
    }

    override fun clear() {
        dataHolder.clear()
    }
}
