package ru.zarina.zarina.ui.app

import ru.zarina.zarina.usecase.rework.device.GetIsOnboardingCompletedUseCase
import javax.inject.Inject

class AppInteractor @Inject constructor(
    val getIsOnboardingCompleted: GetIsOnboardingCompletedUseCase,
)
