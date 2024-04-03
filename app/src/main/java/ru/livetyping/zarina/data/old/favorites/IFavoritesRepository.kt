package ru.livetyping.zarina.data.old.favorites

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.old.Page
import ru.livetyping.zarina.domain.old.Product

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

    suspend fun getFavorites(pageIndex: Int): Page<List<Product>>
}

