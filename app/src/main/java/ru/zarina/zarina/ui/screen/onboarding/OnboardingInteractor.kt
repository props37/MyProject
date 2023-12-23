package ru.zarina.zarina.ui.screen.onboarding

import ru.zarina.zarina.data.permissionmanager.PermissionManager
import javax.inject.Inject

class OnboardingInteractor @Inject constructor(
    val permissionManager: PermissionManager,
)
