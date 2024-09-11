package ru.livetyping.zarina.presentation.screen.checkout.selectedpickuppoint

import ru.livetyping.zarina.usecase.checkout.GetPickupPointDetailsFlowUseCase
import ru.livetyping.zarina.usecase.user.GetUserCityFlowUseCase
import javax.inject.Inject

class SelectedPickupPointInteractor @Inject constructor(
    val getUserCityFlow: GetUserCityFlowUseCase,
    val getPickupPointDetailsFlow: GetPickupPointDetailsFlowUseCase,
)
