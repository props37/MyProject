package ru.zarina.zarina.ui.screen.onboarding

import ru.zarina.zarina.ui.common.permissionmanager.PermissionManager
import ru.zarina.zarina.usecase.content.GetOnboardingBannerUrlUseCase
import ru.zarina.zarina.usecase.device.SetIsOnboardingCompletedUseCase
import ru.zarina.zarina.usecase.geography.GetCurrentCityFlowUseCase
import ru.zarina.zarina.usecase.user.SetDefaultUserCityUseCase
import ru.zarina.zarina.usecase.user.SetUserCityUseCase
import javax.inject.Inject

class OnboardingInteractor @Inject constructor(
    val permissionManager: PermissionManager,
    val getOnboardingBannerUrl: GetOnboardingBannerUrlUseCase,
    val getCurrentCityFlow: GetCurrentCityFlowUseCase,
    val setIsOnboardingCompleted: SetIsOnboardingCompletedUseCase,
    val setUserCity: SetUserCityUseCase,
    val setDefaultUserCity: SetDefaultUserCityUseCase,
)
