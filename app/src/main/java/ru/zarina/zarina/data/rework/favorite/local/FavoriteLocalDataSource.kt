package ru.zarina.zarina.data.rework.favorite.local

import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.domain.rework.product.Product
import javax.inject.Inject

class FavoriteLocalDataSource @Inject constructor(
    private val dataHolder: FavoriteDataHolder,
) {
    val favoriteProductIds: StateFlow<Set<Product.Id>> = dataHolder.favoriteProductIds
    val areFavoriteProductIdsFetched: StateFlow<Boolean> = dataHolder.areFavoriteProductIdsFetched

    fun setFavoriteProductIds(ids: Set<Product.Id>) {
        dataHolder.setFavoriteProductIds(ids)
    }

    fun setAreFavoriteProductIdsFetched(fetched: Boolean) {
        dataHolder.setAreFavoriteProductIdsFetched(fetched)
    }

    fun addProductToFavorites(productId: Product.Id) {
        dataHolder.addProductToFavorites(productId)
    }

    fun removeProductFromFavorites(productId: Product.Id) {
        dataHolder.removeProductFromFavorites(productId)
    }

    fun clear() {
        dataHolder.clear()
    }
}
