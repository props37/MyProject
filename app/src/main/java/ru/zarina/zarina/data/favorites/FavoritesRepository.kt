package ru.zarina.zarina.data.favorites

import org.koin.core.annotation.Factory
import ru.zarina.zarina.domain.Product

@Factory
class FavoritesRepository : IFavoritesRepository {
    override suspend fun setIsFavorite(product: Product, isFavorite: Boolean) {
        TODO("Not yet implemented")
    }
}