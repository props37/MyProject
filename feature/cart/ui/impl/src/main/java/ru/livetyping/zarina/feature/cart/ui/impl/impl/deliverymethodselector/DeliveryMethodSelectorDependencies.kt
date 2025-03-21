package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector

import ru.livetyping.zarina.core.domain.usecase.checkout.GetDeliveryMethodsFlowUseCase
import javax.inject.Inject

internal class DeliveryMethodSelectorDependencies @Inject constructor(
    val getDeliveryMethodsFlow: GetDeliveryMethodsFlowUseCase,
)
