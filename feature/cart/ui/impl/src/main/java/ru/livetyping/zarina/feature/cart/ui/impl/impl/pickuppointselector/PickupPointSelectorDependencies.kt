package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector

import ru.livetyping.zarina.core.domain.usecase.checkout.GetPickupPointsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.location.GetCurrentLocationFlowUseCase
import ru.livetyping.zarina.core.permission.PermissionManager
import javax.inject.Inject

internal class PickupPointSelectorDependencies @Inject constructor(
    val permissionManager: PermissionManager,
    val getPickupPointsFlow: GetPickupPointsFlowUseCase,
    val getCurrentLocationFlow: GetCurrentLocationFlowUseCase,
)
