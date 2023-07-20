package ru.zarina.zarina.data.favorites.local

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.Product

interface IFavoritesLocalSource {
    fun getIds(): Flow<Set<Product.Id>>
    suspend fun setIsFavorite(product: Product, isFavorite: Boolean)
}
