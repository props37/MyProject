package ru.livetyping.zarina.presentation.activity

import ru.livetyping.zarina.core.domain.usecase.auth.GetBearerTokensFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import javax.inject.Inject

class MainDependencies @Inject constructor(
    val getBearerTokensFlow: GetBearerTokensFlowUseCase,
    val getUserFlow: GetUserFlowUseCase,
    val getUserCityFlow: GetUserCityFlowUseCase,
    val getWishlistProductIdsFlow: GetWishlistProductIdsFlowUseCase,
    val getCartProductIdsFlow: GetCartProductIdsFlowUseCase,
)
