package ru.zarina.zarina.ui.screen.favorites

import ru.zarina.zarina.usecase.cart.GetCartProductIdsFlowUseCase
import ru.zarina.zarina.usecase.favorite.GetFavoriteProductIdsFlowUseCase
import ru.zarina.zarina.usecase.favorite.GetFavoriteProductPagingDataFlowUseCase
import javax.inject.Inject

class FavoritesInteractor @Inject constructor(
    val getFavoriteProductPagingDataFlow: GetFavoriteProductPagingDataFlowUseCase,
    val getFavoriteProductIdsFlow: GetFavoriteProductIdsFlowUseCase,
    val getCartProductIdsFlow: GetCartProductIdsFlowUseCase,
)
