package ru.zarina.zarina.data.old.favorites.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Singleton
import ru.zarina.zarina.domain.Product

@Singleton
class FavoritesLocalSource : IFavoritesLocalSource {

    private val ids = MutableStateFlow(setOf<Product.Id>())

    override fun getIds() = ids.asStateFlow()

    override suspend fun update(products: List<Product>) {
        val (favorite, nonFavorite) = products.partition { it.isFavorite }
        val favoriteSet = favorite.map { it.id }.toSet()
        val nonFavoriteSet = nonFavorite.map { it.id }.toSet()
        ids.update {
            it - nonFavoriteSet + favoriteSet
        }
    }

    override suspend fun setIsFavorite(product: Product, isFavorite: Boolean) {
        if (isFavorite) {
            ids.update { it + product.id }
        } else {
            ids.update { it - product.id }
        }
    }
}
