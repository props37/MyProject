package ru.zarina.zarina.ui.screen.productsubscription

import ru.zarina.zarina.usecase.rework.product.SubscribeToProductUseCase
import javax.inject.Inject

class ProductSubscriptionInteractor @Inject constructor(
    val subscribeToProduct: SubscribeToProductUseCase,
)
