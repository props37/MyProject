package ru.livetyping.zarina.ui.screen.productsubscription

import ru.livetyping.zarina.usecase.product.SubscribeToProductUseCase
import ru.livetyping.zarina.usecase.user.GetUserFlowUseCase
import javax.inject.Inject

class ProductSubscriptionInteractor @Inject constructor(
    val getUserFlow: GetUserFlowUseCase,
    val subscribeToProduct: SubscribeToProductUseCase,
)
