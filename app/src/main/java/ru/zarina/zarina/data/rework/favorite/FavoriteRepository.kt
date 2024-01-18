package ru.zarina.zarina.data.rework.favorite

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.zarina.zarina.domain.rework.product.Product
import javax.inject.Inject

class FavoriteRepository @Inject constructor() {
    // TODO: [Low] Move to LDS
    private val favoriteProductIds = MutableStateFlow<Set<Product.Id>>(emptySet())

    fun getFavoriteProductIds(): Flow<Set<Product.Id>> {
        return favoriteProductIds
    }

    fun addToFavorite(product: Product) {
        favoriteProductIds.update { it + product.id }
    }

    fun removeFromFavorite(product: Product) {
        favoriteProductIds.update { it - product.id }
    }
}
