package ru.zarina.zarina.data.old.favorites.remote

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.old.favorites.remote.api.IZarinaFavoritesApi
import ru.zarina.zarina.domain.Page
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

    override suspend fun getFavoritesPage(pageIndex: Int): Page<List<Product>> {
        val response =
            api.getFavoritesPage(
                // adjust page index, because it starts from 1 on the backend
                pageIndex = pageIndex + 1
            )
        val pagination = response.toPagination()
        val products = response.items?.mapNotNull { it.toDomain() }.orEmpty()
        return Page(pagination, products)
    }
}
