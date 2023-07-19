package ru.zarina.zarina.data.favorites

import ru.zarina.zarina.domain.Product

interface IFavoritesRepository {
    suspend fun setIsFavorite(product: Product, isFavorite: Boolean)
}

