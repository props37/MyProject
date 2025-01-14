package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart

import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductCountFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import javax.inject.Inject

internal class CartDeps @Inject constructor(
    val getCartProductIdsFlow: GetCartProductIdsFlowUseCase,
    val getCartProductCountFlow: GetCartProductCountFlowUseCase,
)
