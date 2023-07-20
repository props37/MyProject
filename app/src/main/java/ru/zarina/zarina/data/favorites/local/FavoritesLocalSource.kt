package ru.zarina.zarina.data.favorites.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Factory
import ru.zarina.zarina.domain.Product

// TODO persistence?
@Factory
class FavoritesLocalSource : IFavoritesLocalSource {

    private val ids = MutableStateFlow(setOf<Product.Id>())

    override fun getIds() = ids.asStateFlow()

    override suspend fun setIsFavorite(product: Product, isFavorite: Boolean) {
        if (isFavorite) {
            ids.update { it + product.id }
        } else {
            ids.update { it - product.id }
        }
    }
}
