package ru.zarina.zarina.ui.screen.profile

import ru.zarina.zarina.usecase.user.GetUserCityFlowUseCase
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
    val getUserCityFlow: GetUserCityFlowUseCase,
)
