package ru.livetyping.zarina.ui.screen.productsubscription

import ru.livetyping.zarina.usecase.product.SubscribeToProductUseCase
import javax.inject.Inject

class ProductSubscriptionInteractor @Inject constructor(
    val subscribeToProduct: SubscribeToProductUseCase,
)
