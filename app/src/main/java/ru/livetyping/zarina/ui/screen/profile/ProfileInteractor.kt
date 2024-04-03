package ru.livetyping.zarina.ui.screen.profile

import ru.livetyping.zarina.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.usecase.user.SetUserCityUseCase
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
    val getUserCityFlow: GetUserCityFlowUseCase,
    val setUserCity: SetUserCityUseCase,
)
