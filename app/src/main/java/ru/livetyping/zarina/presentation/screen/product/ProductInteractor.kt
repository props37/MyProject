package ru.livetyping.zarina.presentation.screen.product

import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.livetyping.zarina.usecase.product.GetProductFlowUseCase
import ru.livetyping.zarina.usecase.product.GetProductSimilarFlowUseCase
import ru.livetyping.zarina.usecase.product.GetProductTotalLookFlowUseCase
import javax.inject.Inject

class ProductInteractor @Inject constructor(
    val getProductFlow: GetProductFlowUseCase,
    val toggleProductPresenceInFavorites: ToggleProductPresenceInFavoritesUseCase,
    val getProductTotalLookFlow: GetProductTotalLookFlowUseCase,
    val getProductSimilarFlow: GetProductSimilarFlowUseCase,
)
