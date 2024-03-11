package ru.zarina.zarina.data.favorite.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import ru.zarina.zarina.data.favorite.remote.api.FavoriteApi
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

    fun getFavoriteProductIds(): Flow<Set<Product.Id>> = flow {
        val favoriteProductIds = mutableSetOf<Product.Id>()
        var currentFavoriteProductPage = 1
        var favoriteProductPageCount = Int.MAX_VALUE
        while (currentFavoriteProductPage <= favoriteProductPageCount) {
            val favoriteProductPage = getFavoriteProductPage(currentFavoriteProductPage).first()
            favoriteProductIds.addAll(favoriteProductPage.data.map { it.id })
            currentFavoriteProductPage++
            favoriteProductPageCount = favoriteProductPage.paginationInfo.pageCount
        }
        emit(favoriteProductIds)
    }

    suspend fun addProductToFavorites(productId: Product.Id) {
        api.addProductToFavorites(productId)
    }

    suspend fun removeProductFromFavorites(productId: Product.Id) {
        api.removeProductFromFavorites(productId)
    }
}
