package ru.zarina.zarina.data.old.favorites.remote

import ru.zarina.zarina.domain.old.Page
import ru.zarina.zarina.domain.old.Product

interface IFavoritesRemoteSource {
    suspend fun setIsFavorite(product: Product, isFavorite: Boolean)
    suspend fun getFavoritesPage(pageIndex: Int): Page<List<Product>>
}
