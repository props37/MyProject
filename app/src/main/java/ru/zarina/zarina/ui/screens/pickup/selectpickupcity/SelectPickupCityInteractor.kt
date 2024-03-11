package ru.zarina.zarina.ui.screens.pickup.selectpickupcity

import org.koin.core.annotation.Factory
import ru.zarina.zarina.usecase.old.shop.GetPickupCitiesUseCase
import ru.zarina.zarina.util.base.usecase.invoke

@Factory
class SelectPickupCityInteractor(
    private val getPickupCitiesUseCase: GetPickupCitiesUseCase,
) {

    suspend fun getPickupCities() = getPickupCitiesUseCase()

}
