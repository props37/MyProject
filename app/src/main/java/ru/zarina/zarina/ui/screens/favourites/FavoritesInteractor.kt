package ru.zarina.zarina.ui.screens.favourites

import org.koin.core.annotation.Factory
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.usecase.old.favorites.GetFavoriteIdsUseCase
import ru.zarina.zarina.usecase.old.favorites.GetFavoritesPageUseCase
import ru.zarina.zarina.usecase.old.favorites.SetIsFavoriteUseCase
import ru.zarina.zarina.util.base.usecase.invoke

@Factory
class FavoritesInteractor(
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    val getFavoritesPageUseCase: GetFavoritesPageUseCase,
    private val setIsFavoriteUseCase: SetIsFavoriteUseCase,
) {
    fun getFavoriteIds() = getFavoriteIdsUseCase()
    suspend fun setIsFavorite(product: Product, isFavorite: Boolean) =
        setIsFavoriteUseCase(SetIsFavoriteUseCase.Params(product, isFavorite))
}
