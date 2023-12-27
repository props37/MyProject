package ru.zarina.zarina.ui.rework

import ru.zarina.zarina.usecase.rework.device.GetIsOnboardingCompletedUseCase
import javax.inject.Inject

class AppInteractor @Inject constructor(
    val getIsOnboardingCompleted: GetIsOnboardingCompletedUseCase,
)
