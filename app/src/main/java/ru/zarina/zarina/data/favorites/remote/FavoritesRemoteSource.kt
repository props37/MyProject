package ru.zarina.zarina.data.favorites.remote

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.favorites.remote.api.IZarinaFavoritesApi
import ru.zarina.zarina.domain.Product

@Factory
class FavoritesRemoteSource(
    private val api: IZarinaFavoritesApi,
) : IFavoritesRemoteSource {
    override suspend fun setIsFavorite(product: Product, isFavorite: Boolean) {
        if (isFavorite) {
            api.add(product.id.value)
        } else {
            api.remove(product.id.value)
        }
    }

}