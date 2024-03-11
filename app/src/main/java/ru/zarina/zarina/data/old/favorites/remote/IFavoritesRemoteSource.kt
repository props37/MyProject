package ru.zarina.zarina.data.old.favorites.remote

import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.Product

interface IFavoritesRemoteSource {
    suspend fun setIsFavorite(product: Product, isFavorite: Boolean)
    suspend fun getFavoritesPage(pageIndex: Int): Page<List<Product>>
}
