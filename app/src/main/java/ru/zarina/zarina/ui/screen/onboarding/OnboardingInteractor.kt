package ru.zarina.zarina.ui.screen.onboarding

import ru.zarina.zarina.data.permissionmanager.PermissionManager
import ru.zarina.zarina.usecase.rework.geography.DetectCityUseCase
import javax.inject.Inject

class OnboardingInteractor @Inject constructor(
    val permissionManager: PermissionManager,
    val detectCity: DetectCityUseCase,
)
