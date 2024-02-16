package ru.zarina.zarina.data.rework.favorite

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import ru.zarina.zarina.data.rework.favorite.local.FavoriteLocalDataSource
import ru.zarina.zarina.data.rework.favorite.remote.FavoriteRemoteDataSource
import ru.zarina.zarina.domain.rework.common.Page
import ru.zarina.zarina.domain.rework.product.Product
import timber.log.Timber
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val localDataSource: FavoriteLocalDataSource,
    private val remoteDataSource: FavoriteRemoteDataSource,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getFavoriteProductIds(): Flow<Set<Product.Id>> {
        return localDataSource.favoriteProductIds.mapLatest { cached ->
            if (cached != null) {
                Timber.v("Get cached favorite product IDs")
                cached
            } else {
                fetchFavoriteProductIds()
                emptySet()
            }
        }
    }

    fun getFavoriteProductPage(page: Int): Flow<Page<List<Product>>> {
        return remoteDataSource.getFavoriteProductPage(page)
    }

    suspend fun fetchFavoriteProductIds() {
        val favoriteProductIds = remoteDataSource.getFavoriteProductIds().first()
        localDataSource.setFavoriteProductIds(favoriteProductIds)
    }

    suspend fun addProductToFavorites(productId: Product.Id) {
        if (localDataSource.favoriteProductIds.value == null) fetchFavoriteProductIds()
        remoteDataSource.addProductToFavorites(productId)
        localDataSource.addProductToFavorites(productId)
    }

    suspend fun removeProductFromFavorites(productId: Product.Id) {
        if (localDataSource.favoriteProductIds.value == null) fetchFavoriteProductIds()
        remoteDataSource.removeProductFromFavorites(productId)
        localDataSource.removeProductFromFavorites(productId)
    }

    fun clear() {
        localDataSource.clear()
    }
}
