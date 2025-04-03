package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector

import ru.livetyping.zarina.core.domain.usecase.geo.GetCityStreetsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.geo.GetStreetBuildingsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import javax.inject.Inject

internal class DeliveryAddressSelectorDependencies @Inject constructor(
    val getUserCityFlow: GetUserCityFlowUseCase,
    val getCityStreetsFlow: GetCityStreetsFlowUseCase,
    val getStreetBuildingsFlow: GetStreetBuildingsFlowUseCase,
)
