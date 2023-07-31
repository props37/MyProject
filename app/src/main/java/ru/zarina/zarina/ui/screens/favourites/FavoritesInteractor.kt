package ru.zarina.zarina.ui.screens.favourites

import org.koin.core.annotation.Factory
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.usecase.favorites.GetFavoriteIdsUseCase
import ru.zarina.zarina.usecase.favorites.GetFavoritesPageUseCase
import ru.zarina.zarina.usecase.favorites.SetIsFavoriteUseCase
import ru.zarina.zarina.utils.clean.invoke

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
