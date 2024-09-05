package ru.livetyping.zarina.presentation.screen.checkout.pickuppointdelivery

import ru.livetyping.zarina.presentation.common.permissionmanager.PermissionManager
import ru.livetyping.zarina.usecase.checkout.GetPickupPointsFlowUseCase
import ru.livetyping.zarina.usecase.location.GetCurrentLocationFlowUseCase
import ru.livetyping.zarina.usecase.user.GetUserCityFlowUseCase
import javax.inject.Inject

class CheckoutPickupPointDeliveryInteractor @Inject constructor(
    val permissionManager: PermissionManager,
    val getPickupPointsFlow: GetPickupPointsFlowUseCase,
    val getUserCityFlow: GetUserCityFlowUseCase,
    val getCurrentLocationFlow: GetCurrentLocationFlowUseCase,
)
