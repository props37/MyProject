package ru.livetyping.zarina.data.wishlist.impl.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.model.common.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.data.wishlist.impl.remote.api.WishlistApi
import javax.inject.Inject

internal class WishlistRemoteDataSourceImpl @Inject constructor(
    private val api: WishlistApi,
) : WishlistRemoteDataSource {
    override suspend fun getWishlistProductIds(): Set<Product.Id> {
        return api.getWishlistProductIds().toProductIds()
    }

    override fun getFavoriteProductPageFlow(page: Int): Flow<Page<List<ProductShort>>> = flow {
        val dto = api.getWishlistProducts(page)
        val productPage = dto.toProductPage()
        emit(productPage)
    }

    override suspend fun addProductToWishlist(productId: Product.Id) {
        api.addProductToWishlist(productId)
    }

    override suspend fun removeProductFromWishlist(productId: Product.Id) {
        api.removeProductFromWishlist(productId)
    }
}
