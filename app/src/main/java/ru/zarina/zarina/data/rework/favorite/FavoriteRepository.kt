package ru.zarina.zarina.data.rework.favorite

import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.data.rework.favorite.local.FavoriteLocalDataSource
import ru.zarina.zarina.data.rework.favorite.remote.FavoriteRemoteDataSource
import ru.zarina.zarina.domain.rework.product.Product
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val localDataSource: FavoriteLocalDataSource,
    private val remoteDataSource: FavoriteRemoteDataSource,
) {
    val favoriteProductIds: StateFlow<Set<Product.Id>?> = localDataSource.favoriteProductIds

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
