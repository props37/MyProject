package ru.livetyping.zarina.presentation.screen.shops

import ru.livetyping.zarina.usecase.shop.GetUserCityShopsFlowUseCase
import javax.inject.Inject

class ShopsInteractor @Inject constructor(
    val getUserCityShopsFlow: GetUserCityShopsFlowUseCase,
)
