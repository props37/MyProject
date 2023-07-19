package ru.zarina.zarina.data.favorites

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.favorites.remote.IFavoritesRemoteSource
import ru.zarina.zarina.domain.Product

@Factory
class FavoritesRepository(
    private val remote: IFavoritesRemoteSource,
) : IFavoritesRepository {
    override suspend fun setIsFavorite(product: Product, isFavorite: Boolean) =
        remote.setIsFavorite(product, isFavorite)
}