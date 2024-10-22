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

    override fun isWishlistProductIdsFetched(): Boolean {
        return dataHolder.isWishlistProductIdsFetched()
    }

    override fun setIsWishlistProductIdsFetched(isFetched: Boolean) {
        dataHolder.setIsWishlistProductIdsFetched(isFetched)
    }
}
