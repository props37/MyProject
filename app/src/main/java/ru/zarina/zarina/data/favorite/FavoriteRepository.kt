package ru.zarina.zarina.data.favorite

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import ru.zarina.zarina.data.favorite.local.FavoriteLocalDataSource
import ru.zarina.zarina.data.favorite.remote.FavoriteRemoteDataSource
import ru.zarina.zarina.domain.common.Page
import ru.zarina.zarina.domain.product.Product
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val localDataSource: FavoriteLocalDataSource,
    private val remoteDataSource: FavoriteRemoteDataSource,
) {
    val favoriteProductIds: StateFlow<Set<Product.Id>> = localDataSource.favoriteProductIds
    val areFavoriteProductIdsFetched: StateFlow<Boolean> =
        localDataSource.areFavoriteProductIdsFetched

    suspend fun fetchFavoriteProductIds(): Set<Product.Id> {
        val favoriteProductIds = remoteDataSource.getFavoriteProductIdsFlow().first()
        localDataSource.setFavoriteProductIds(favoriteProductIds)
        localDataSource.setAreFavoriteProductIdsFetched(true)
        return favoriteProductIds
    }

    fun getFavoriteProductPageFlow(page: Int): Flow<Page<List<Product>>> {
        return remoteDataSource.getFavoriteProductPageFlow(page)
    }

    suspend fun addProductToFavorites(productId: Product.Id) {
        remoteDataSource.addProductToFavorites(productId)
        localDataSource.addProductToFavorites(productId)
    }

    suspend fun removeProductFromFavorites(productId: Product.Id) {
        remoteDataSource.removeProductFromFavorites(productId)
        localDataSource.removeProductFromFavorites(productId)
    }

    fun clear() {
        localDataSource.clear()
    }
}
