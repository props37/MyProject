package ru.livetyping.zarina.data.old.favorites

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.data.old.favorites.local.IFavoritesLocalSource
import ru.livetyping.zarina.data.old.favorites.remote.IFavoritesRemoteSource
import ru.livetyping.zarina.domain.old.Page
import ru.livetyping.zarina.domain.old.Product

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
        try {
            remote.setIsFavorite(product, isFavorite)
            local.setIsFavorite(product, isFavorite)
        } catch (e: Exception) {
            local.setIsFavorite(product, product.isFavorite)
            throw e
        }
    }

    override suspend fun getFavorites(pageIndex: Int): Page<List<Product>> {
        val page = remote.getFavoritesPage(pageIndex)
        update(page.value)
        return page
    }
}
