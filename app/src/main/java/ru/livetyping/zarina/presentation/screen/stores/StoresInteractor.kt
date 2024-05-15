package ru.livetyping.zarina.presentation.screen.stores

import ru.livetyping.zarina.presentation.common.permissionmanager.PermissionManager
import ru.livetyping.zarina.usecase.location.GetCurrentLocationFlowUseCase
import ru.livetyping.zarina.usecase.store.GetStoresFlowUseCase
import ru.livetyping.zarina.usecase.user.GetUserCityFlowUseCase
import javax.inject.Inject

class StoresInteractor @Inject constructor(
    val permissionManager: PermissionManager,
    val getCurrentLocationFlow: GetCurrentLocationFlowUseCase,
    val getStoresFlow: GetStoresFlowUseCase,
    val getUserCityFlow: GetUserCityFlowUseCase,
)
