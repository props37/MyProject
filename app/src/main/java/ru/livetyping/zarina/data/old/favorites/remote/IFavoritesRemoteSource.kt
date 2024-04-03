package ru.livetyping.zarina.data.old.favorites.remote

import ru.livetyping.zarina.domain.old.Page
import ru.livetyping.zarina.domain.old.Product

interface IFavoritesRemoteSource {
    suspend fun setIsFavorite(product: Product, isFavorite: Boolean)
    suspend fun getFavoritesPage(pageIndex: Int): Page<List<Product>>
}
