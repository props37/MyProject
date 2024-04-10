package ru.livetyping.zarina.ui.screen.onboarding

import ru.livetyping.zarina.ui.common.permissionmanager.PermissionManager
import ru.livetyping.zarina.usecase.content.GetOnboardingBannerUrlUseCase
import ru.livetyping.zarina.usecase.device.SetIsOnboardingCompletedUseCase
import ru.livetyping.zarina.usecase.geography.GetCurrentCityFlowUseCase
import ru.livetyping.zarina.usecase.user.SetDefaultUserCityUseCase
import ru.livetyping.zarina.usecase.user.SetUserCityUseCase
import javax.inject.Inject

class OnboardingInteractor @Inject constructor(
    val permissionManager: PermissionManager,
    val getOnboardingBannerUrl: GetOnboardingBannerUrlUseCase,
    val getCurrentCityFlow: GetCurrentCityFlowUseCase,
    val setIsOnboardingCompleted: SetIsOnboardingCompletedUseCase,
    val setUserCity: SetUserCityUseCase,
    val setDefaultUserCity: SetDefaultUserCityUseCase,
)
