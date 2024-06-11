package ru.livetyping.zarina.presentation.screen.loyaltyprogram

import ru.livetyping.zarina.usecase.user.GetLoyaltyCardFlowUseCase
import javax.inject.Inject

class LoyaltyProgramInteractor @Inject constructor(
    val getLoyaltyCardFlow: GetLoyaltyCardFlowUseCase,
)
