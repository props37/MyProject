package ru.zarina.zarina.data.rework.favorite.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zarina.zarina.domain.rework.product.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteDataHolder @Inject constructor() {
    private val _favoriteProductIds = MutableStateFlow<Set<Product.Id>?>(null)
    val favoriteProductIds: StateFlow<Set<Product.Id>?> = _favoriteProductIds.asStateFlow()

    fun setFavoriteProductIds(ids: Set<Product.Id>) {
        _favoriteProductIds.value = ids
    }

    fun addProductToFavorites(productId: Product.Id) {
        _favoriteProductIds.update { it?.plus(productId) ?: setOf(productId) }
    }

    fun removeProductFromFavorites(productId: Product.Id) {
        _favoriteProductIds.update { it?.minus(productId) }
    }

    fun clear() {
        _favoriteProductIds.value = emptySet()
    }
}
