package ru.zarina.zarina.data.old.favorites.local

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.Product

interface IFavoritesLocalSource {
    fun getIds(): Flow<Set<Product.Id>>
    suspend fun update(products: List<Product>)
    suspend fun setIsFavorite(product: Product, isFavorite: Boolean)
}
