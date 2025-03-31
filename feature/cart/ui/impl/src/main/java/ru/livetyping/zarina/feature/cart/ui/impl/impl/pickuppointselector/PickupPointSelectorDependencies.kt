package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector

import ru.livetyping.zarina.core.domain.usecase.checkout.GetPickupPointsFlowUseCase
import javax.inject.Inject

internal class PickupPointSelectorDependencies @Inject constructor(
    val getPickupPointsFlow: GetPickupPointsFlowUseCase,
)
