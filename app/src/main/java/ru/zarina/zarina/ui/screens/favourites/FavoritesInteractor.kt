package ru.zarina.zarina.ui.screens.favourites

import org.koin.core.annotation.Factory
import ru.zarina.zarina.usecase.favorites.GetFavoritesPageUseCase

@Factory
class FavoritesInteractor(
    val getFavoritesPageUseCase: GetFavoritesPageUseCase,
)
