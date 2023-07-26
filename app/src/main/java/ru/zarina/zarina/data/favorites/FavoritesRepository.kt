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

    override suspend fun update(products: List<Product>) {
        local.update(products)
    }

    override suspend fun setIsFavorite(product: Product, isFavorite: Boolean) {
        local.setIsFavorite(product, isFavorite)
        try {
            remote.setIsFavorite(product, isFavorite)
        } catch (e: Exception) {
            local.setIsFavorite(product, product.isFavorite)
            throw e
        }
    }
}