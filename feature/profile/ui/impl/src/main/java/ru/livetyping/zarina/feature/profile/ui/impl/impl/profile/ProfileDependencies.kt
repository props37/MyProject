package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile

import ru.livetyping.zarina.core.buildutil.AppVersionName
import ru.livetyping.zarina.core.buildutil.BuildType
import ru.livetyping.zarina.core.buildutil.MindboxDeviceUuidProvider
import ru.livetyping.zarina.core.domain.usecase.user.GetLoyaltyCardFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SetUserCityUseCase
import javax.inject.Inject

internal class ProfileDependencies @Inject constructor(
    val getUserFlow: GetUserFlowUseCase,
    val getLoyaltyCardFlow: GetLoyaltyCardFlowUseCase,
    val getUserCityFlow: GetUserCityFlowUseCase,
    val setUserCity: SetUserCityUseCase,
    @AppVersionName
    val appVersionName: String,
    val appBuildType: BuildType,
    val mindboxDeviceUuidProvider: MindboxDeviceUuidProvider,
)
