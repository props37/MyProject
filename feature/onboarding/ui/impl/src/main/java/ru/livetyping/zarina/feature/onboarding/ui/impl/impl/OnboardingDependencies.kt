package ru.livetyping.zarina.feature.onboarding.ui.impl.impl

import ru.livetyping.zarina.core.domain.usecase.geo.GetCurrentCityByLocationFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.onboarding.GetOnboardingBannerUrlFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.onboarding.SetIsOnboardingCompletedUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SetLocalUserCityUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SetUserCityUseCase
import ru.livetyping.zarina.core.permission.PermissionManager
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model.OnboardingStepsBuilder
import javax.inject.Inject

internal class OnboardingDependencies @Inject constructor(
    val permissionManager: PermissionManager,
    val onboardingStepsBuilder: OnboardingStepsBuilder,
    val getOnboardingBannerUrlFlow: GetOnboardingBannerUrlFlowUseCase,
    val getCurrentCityByLocationFlow: GetCurrentCityByLocationFlowUseCase,
    val setIsOnboardingCompleted: SetIsOnboardingCompletedUseCase,
    val setUserCity: SetUserCityUseCase,
    val setLocalUserCity: SetLocalUserCityUseCase,
)
