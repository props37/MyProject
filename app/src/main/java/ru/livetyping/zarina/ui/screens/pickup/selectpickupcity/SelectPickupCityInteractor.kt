package ru.livetyping.zarina.ui.screens.pickup.selectpickupcity

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.usecase.old.shop.GetPickupCitiesUseCase
import ru.livetyping.zarina.util.base.usecase.invoke

@Factory
class SelectPickupCityInteractor(
    private val getPickupCitiesUseCase: GetPickupCitiesUseCase,
) {

    suspend fun getPickupCities() = getPickupCitiesUseCase()

}
