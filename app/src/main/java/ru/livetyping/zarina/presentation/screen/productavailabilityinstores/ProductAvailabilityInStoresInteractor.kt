package ru.livetyping.zarina.presentation.screen.productavailabilityinstores

import ru.livetyping.zarina.usecase.product.GetProductAvailabilityInStoresFlowUseCase
import ru.livetyping.zarina.usecase.user.GetUserCityFlowUseCase
import javax.inject.Inject

class ProductAvailabilityInStoresInteractor @Inject constructor(
    val getProductAvailabilityInStoresFlow: GetProductAvailabilityInStoresFlowUseCase,
    val getUserCityFlow: GetUserCityFlowUseCase,
)
