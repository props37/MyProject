package ru.zarina.zarina.ui.screens.pickup.selectshopcity

import ru.zarina.zarina.usecase.shop.GetPickupCitiesUseCase
import ru.zarina.zarina.utils.clean.invoke
import javax.inject.Inject

class SelectPickupCityInteractor @Inject constructor(
    private val getPickupCitiesUseCase: GetPickupCitiesUseCase,
) {

    suspend fun getPickupCities() = getPickupCitiesUseCase()

}
