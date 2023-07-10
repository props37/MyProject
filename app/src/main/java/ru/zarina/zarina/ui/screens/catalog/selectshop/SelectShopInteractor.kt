package ru.zarina.zarina.ui.screens.catalog.selectshop

import org.koin.core.annotation.Factory
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.usecase.shop.GetShopsUseCase
import ru.zarina.zarina.usecase.user.GetCityUseCase
import ru.zarina.zarina.utils.clean.invoke

@Factory
class SelectShopInteractor(
    private val getCityUseCase: GetCityUseCase,
    private val getShopsUseCase: GetShopsUseCase
) {
    suspend fun getUserCity() = getCityUseCase()
    suspend fun getShops(city: City) = getShopsUseCase(GetShopsUseCase.Params(city))
}