package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector

import ru.livetyping.zarina.core.domain.usecase.cart.GetCartFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.checkout.GetPickupStoresFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import javax.inject.Inject

internal class PickupStoreSelectorDependencies @Inject constructor(
    val getUserCityFlow: GetUserCityFlowUseCase,
    val getCartFlow: GetCartFlowUseCase,
    val getPickupStoresFlow: GetPickupStoresFlowUseCase,
)
