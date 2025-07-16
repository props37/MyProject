package ru.livetyping.zarina.data.wishlist.remote

import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.data.wishlist.remote.api.WishlistApi
import javax.inject.Inject

internal class WishlistRemoteDataSourceImpl @Inject constructor(
    private val api: WishlistApi,
) : WishlistRemoteDataSource {
    override suspend fun getWishlistProductIds(): Set<Product.Id> {
        val productIds = api.getWishlistProductIds().toProductIds()
        return productIds
    }

    override suspend fun getFavoriteProductPage(page: Int): Page<List<ProductShort>> {
        return api.getWishlistProducts(page).toProductPage()
    }

    override suspend fun addProductToWishlist(productId: Product.Id) {
        api.addProductToWishlist(productId)
    }

    override suspend fun removeProductFromWishlist(productId: Product.Id) {
        api.removeProductFromWishlist(productId)
    }

    override suspend fun clearWishlist() {
        api.clearWishlist()
    }
}
