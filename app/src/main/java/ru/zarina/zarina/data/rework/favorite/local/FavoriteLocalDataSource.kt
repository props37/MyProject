package ru.zarina.zarina.data.rework.favorite.local

import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.domain.rework.product.Product
import javax.inject.Inject

class FavoriteLocalDataSource @Inject constructor(
    private val dataHolder: FavoriteDataHolder,
) {
    val favoriteProductIds: StateFlow<Set<Product.Id>?> = dataHolder.favoriteProductIds

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
