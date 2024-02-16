package ru.zarina.zarina.data.rework.favorite.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zarina.zarina.data.rework.favorite.remote.api.FavoriteApi
import ru.zarina.zarina.domain.rework.common.Page
import ru.zarina.zarina.domain.rework.product.Product
import javax.inject.Inject

class FavoriteRemoteDataSource @Inject constructor(
    private val api: FavoriteApi,
) {
    fun getFavoriteProductPage(page: Int): Flow<Page<List<Product>>> = flow {
        val productPage = api.getFavoriteProducts(page).toProductPage()
        emit(productPage)
    }

    suspend fun addProductToFavorites(productId: Product.Id) {
        api.addProductToFavorites(productId)
    }

    suspend fun removeProductFromFavorites(productId: Product.Id) {
        api.removeProductFromFavorites(productId)
    }
}
