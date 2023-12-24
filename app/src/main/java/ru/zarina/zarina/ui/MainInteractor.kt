package ru.zarina.zarina.ui

import ru.zarina.zarina.usecase.rework.device.GetIsOnboardingCompletedUseCase
import javax.inject.Inject

class MainInteractor @Inject constructor(
    val getIsOnboardingCompleted: GetIsOnboardingCompletedUseCase,
)
