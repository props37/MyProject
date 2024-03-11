package ru.zarina.zarina.ui.screens.catalog.selectcity

import org.koin.core.annotation.Factory
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.usecase.old.shop.GetPickupCitiesUseCase
import ru.zarina.zarina.util.base.usecase.invoke

@Factory
class SelectCityInteractor(
    private val getPickupCitiesUseCase: GetPickupCitiesUseCase
) {
    suspend fun getPickupCities(): Result<List<City>> = getPickupCitiesUseCase()
}
