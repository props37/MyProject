package ru.livetyping.zarina.ui.screen.profile

import ru.livetyping.zarina.usecase.user.FetchLoyaltyCardUseCase
import ru.livetyping.zarina.usecase.user.GetLoyaltyCardFlowUseCase
import ru.livetyping.zarina.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.usecase.user.SetUserCityUseCase
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
    val getUserFlow: GetUserFlowUseCase,
    val getLoyaltyCardFlow: GetLoyaltyCardFlowUseCase,
    val fetchLoyaltyCard: FetchLoyaltyCardUseCase,
    val getUserCityFlow: GetUserCityFlowUseCase,
    val setUserCity: SetUserCityUseCase,
)
