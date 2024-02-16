package ru.zarina.zarina.data.rework.favorite.remote

import ru.zarina.zarina.data.rework.favorite.remote.api.FavoriteApi
import ru.zarina.zarina.domain.rework.product.Product
import javax.inject.Inject

class FavoriteRemoteDataSource @Inject constructor(
    private val api: FavoriteApi,
) {
    suspend fun addProductToFavorites(productId: Product.Id) {
        api.addProductToFavorites(productId)
    }

    suspend fun removeProductFromFavorites(productId: Product.Id) {
        api.removeProductFromFavorites(productId)
    }
}
