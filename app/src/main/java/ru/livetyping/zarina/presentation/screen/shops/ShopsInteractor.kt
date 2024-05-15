package ru.livetyping.zarina.presentation.screen.shops

import ru.livetyping.zarina.presentation.common.permissionmanager.PermissionManager
import ru.livetyping.zarina.usecase.location.GetCurrentLocationFlowUseCase
import ru.livetyping.zarina.usecase.shop.GetShopsFlowUseCase
import ru.livetyping.zarina.usecase.user.GetUserCityFlowUseCase
import javax.inject.Inject

class ShopsInteractor @Inject constructor(
    val permissionManager: PermissionManager,
    val getCurrentLocationFlow: GetCurrentLocationFlowUseCase,
    val getShopsFlow: GetShopsFlowUseCase,
    val getUserCityFlow: GetUserCityFlowUseCase,
)
