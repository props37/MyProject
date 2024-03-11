package ru.zarina.zarina.ui.screen.onboarding

import ru.zarina.zarina.data.permissionmanager.PermissionManager
import ru.zarina.zarina.usecase.rework.content.GetOnboardingBannerUrlUseCase
import ru.zarina.zarina.usecase.rework.device.SetIsOnboardingCompletedUseCase
import ru.zarina.zarina.usecase.rework.geography.GetCurrentCityFlowUseCase
import ru.zarina.zarina.usecase.rework.user.SetDefaultUserCityUseCase
import ru.zarina.zarina.usecase.rework.user.SetUserCityUseCase
import javax.inject.Inject

class OnboardingInteractor @Inject constructor(
    val permissionManager: PermissionManager,
    val getOnboardingBannerUrl: GetOnboardingBannerUrlUseCase,
    val getCurrentCityFlow: GetCurrentCityFlowUseCase,
    val setIsOnboardingCompleted: SetIsOnboardingCompletedUseCase,
    val setUserCity: SetUserCityUseCase,
    val setDefaultUserCity: SetDefaultUserCityUseCase,
)
