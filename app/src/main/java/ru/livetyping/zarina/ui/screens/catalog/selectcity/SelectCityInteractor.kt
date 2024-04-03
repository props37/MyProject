package ru.livetyping.zarina.ui.screens.catalog.selectcity

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.domain.old.City
import ru.livetyping.zarina.usecase.old.shop.GetPickupCitiesUseCase
import ru.livetyping.zarina.util.base.usecase.invoke

@Factory
class SelectCityInteractor(
    private val getPickupCitiesUseCase: GetPickupCitiesUseCase
) {
    suspend fun getPickupCities(): Result<List<City>> = getPickupCitiesUseCase()
}
