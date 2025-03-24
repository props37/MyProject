package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector

import ru.livetyping.zarina.core.domain.usecase.cart.GetCartFlowUseCase
import javax.inject.Inject

internal class PickupStoreSelectorDependencies @Inject constructor(
    val getCartFlow: GetCartFlowUseCase,
)
