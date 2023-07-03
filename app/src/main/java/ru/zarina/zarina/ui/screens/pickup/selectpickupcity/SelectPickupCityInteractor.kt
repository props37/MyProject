package ru.zarina.zarina.ui.screens.pickup.selectpickupcity

import org.koin.core.annotation.Factory
import ru.zarina.zarina.usecase.shop.GetPickupCitiesUseCase
import ru.zarina.zarina.utils.clean.invoke

@Factory
class SelectPickupCityInteractor(
    private val getPickupCitiesUseCase: GetPickupCitiesUseCase,
) {

    suspend fun getPickupCities() = getPickupCitiesUseCase()

}
