package ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist

import ru.livetyping.zarina.core.domain.usecase.location.GetCurrentLocationFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.store.GetStoresFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.permission.PermissionManager
import javax.inject.Inject

internal class StoreListDependencies @Inject constructor(
    val permissionManager: PermissionManager,
    val getStoresFlow: GetStoresFlowUseCase,
    val getCurrentLocationFlow: GetCurrentLocationFlowUseCase,
    val getUserCityFlow: GetUserCityFlowUseCase,
)
