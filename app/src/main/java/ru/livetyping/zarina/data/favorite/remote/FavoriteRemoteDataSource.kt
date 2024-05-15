package ru.livetyping.zarina.data.favorite.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.data.favorite.remote.api.FavoriteApi
import ru.livetyping.zarina.domain.common.Page
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductItem
import javax.inject.Inject

class FavoriteRemoteDataSource @Inject constructor(
    private val api: FavoriteApi,
) {
    fun getFavoriteProductPageFlow(page: Int): Flow<Page<List<ProductItem>>> = flow {
        val productPage = api.getFavoriteProducts(page).toProductPage()
        emit(productPage)
    }

    fun getFavoriteProductIdsFlow(): Flow<Set<Product.Id>> = flow {
        val favoriteProductIds = api.getFavoriteProductIds().toFavoriteProductIds()
        emit(favoriteProductIds)
    }

    suspend fun addProductToFavorites(productId: Product.Id) {
        api.addProductToFavorites(productId)
    }

    suspend fun removeProductFromFavorites(productId: Product.Id) {
        api.removeProductFromFavorites(productId)
    }

    suspend fun clearFavoriteProducts() {
        api.clearFavoriteProducts()
    }
}
