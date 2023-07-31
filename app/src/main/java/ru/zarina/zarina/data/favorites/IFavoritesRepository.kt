package ru.zarina.zarina.data.favorites

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.Product

interface IFavoritesRepository {
    fun getIds(): Flow<Set<Product.Id>>

    /**
     * Update [products] favorite states locally.
     */
    suspend fun update(products: List<Product>)

    /**
     * Set [product] favorite state to [isFavorite] as a result of user action.
     */
    suspend fun setIsFavorite(product: Product, isFavorite: Boolean)
}

