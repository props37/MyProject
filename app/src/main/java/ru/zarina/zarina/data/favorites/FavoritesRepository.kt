package ru.zarina.zarina.data.favorites

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.favorites.local.IFavoritesLocalSource
import ru.zarina.zarina.data.favorites.remote.IFavoritesRemoteSource
import ru.zarina.zarina.domain.Product

@Factory
class FavoritesRepository(
    private val remote: IFavoritesRemoteSource,
    private val local: IFavoritesLocalSource,
) : IFavoritesRepository {

    override fun getIds() = local.getIds()

    override suspend fun setIsFavorite(product: Product, isFavorite: Boolean) {
        remote.setIsFavorite(product, isFavorite)
        local.setIsFavorite(product, isFavorite)
    }
}