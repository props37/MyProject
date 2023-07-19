package ru.zarina.zarina.data.favorites.remote

import ru.zarina.zarina.domain.Product

interface IFavoritesRemoteSource {
    suspend fun setIsFavorite(product: Product, isFavorite: Boolean)
}