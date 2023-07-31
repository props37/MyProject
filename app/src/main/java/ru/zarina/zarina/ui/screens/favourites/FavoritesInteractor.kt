package ru.zarina.zarina.ui.screens.favourites

import org.koin.core.annotation.Factory
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.usecase.favorites.GetFavoritesPageUseCase
import ru.zarina.zarina.usecase.favorites.SetIsFavoriteUseCase

@Factory
class FavoritesInteractor(
    val getFavoritesPageUseCase: GetFavoritesPageUseCase,
    private val setIsFavoriteUseCase: SetIsFavoriteUseCase,
) {
    suspend fun setIsFavorite(product: Product, isFavorite: Boolean) =
        setIsFavoriteUseCase(SetIsFavoriteUseCase.Params(product, isFavorite))
}
