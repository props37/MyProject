package ru.livetyping.zarina.ui.screens.favourites

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.domain.old.Product
import ru.livetyping.zarina.usecase.old.favorites.GetFavoriteIdsUseCase
import ru.livetyping.zarina.usecase.old.favorites.GetFavoritesPageUseCase
import ru.livetyping.zarina.usecase.old.favorites.SetIsFavoriteUseCase
import ru.livetyping.zarina.util.base.usecase.invoke

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
