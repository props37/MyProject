package ru.livetyping.zarina.ui.screen.product

import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.livetyping.zarina.usecase.product.GetProductFlowUseCase
import ru.livetyping.zarina.usecase.product.GetProductTotalLookFlowUseCase
import javax.inject.Inject

class ProductInteractor @Inject constructor(
    val getProductFlow: GetProductFlowUseCase,
    val getProductTotalLookFlow: GetProductTotalLookFlowUseCase,
    val toggleProductPresenceInFavorites: ToggleProductPresenceInFavoritesUseCase,
)
