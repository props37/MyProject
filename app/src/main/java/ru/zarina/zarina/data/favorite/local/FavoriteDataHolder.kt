package ru.zarina.zarina.data.favorite.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zarina.zarina.domain.product.Product
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteDataHolder @Inject constructor() {
    private val _favoriteProductIds = MutableStateFlow<Set<Product.Id>>(emptySet())
    val favoriteProductIds: StateFlow<Set<Product.Id>> = _favoriteProductIds.asStateFlow()

    private val _areFavoriteProductIdsFetched = MutableStateFlow(false)
    val areFavoriteProductIdsFetched: StateFlow<Boolean> =
        _areFavoriteProductIdsFetched.asStateFlow()

    fun setFavoriteProductIds(ids: Set<Product.Id>) {
        Timber.v("Set favorite product IDs: $ids")
        _favoriteProductIds.value = ids
    }

    fun setAreFavoriteProductIdsFetched(fetched: Boolean) {
        Timber.v("Set favorite product IDs fetched: $fetched")
        _areFavoriteProductIdsFetched.value = fetched
    }

    fun addProductToFavorites(productId: Product.Id) {
        Timber.v("Add product $productId to favorites")
        _favoriteProductIds.update { it + productId }
    }

    fun removeProductFromFavorites(productId: Product.Id) {
        Timber.v("Remove product $productId from favorites")
        _favoriteProductIds.update { it - productId }
    }

    fun clear() {
        Timber.v("Clear favorite products data")
        _favoriteProductIds.value = emptySet()
        _areFavoriteProductIdsFetched.value = false
    }
}
